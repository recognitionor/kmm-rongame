package com.jhlee.kmm_rongame.pandora.presentation

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.pandora.domain.PandoraDataSource
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.round
import kotlin.math.sqrt

class PandoraViewModel(
    private val pandoraDataSource: PandoraDataSource,
) : ViewModel() {
    companion object {
        const val VIEWMODEL_KEY = "pandora_view_model"
    }

    private val _state = MutableStateFlow(PandoraState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {
        getGoalCard()
    }

    private fun getGoalCard() {
        pandoraDataSource.getGoalCard().onEach { result ->
            when (result) {
                is Resource.Error -> {}
                is Resource.Success -> {
                    _state.update { it.copy(goalCard = result.data) }
                }

                is Resource.Loading -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun selectStatus(state: Int) {
        _state.update { it.copy(pandoraState = state) }
    }

    fun tutorialMove(index: Int) {
        _state.update { it.copy(tutorialIndex = index) }
    }

    fun pickCard(card: Card) {
        pandoraDataSource.pickCard(card).onEach {
            _state.update { it.copy(pandoraState = PandoraState.PICK_DONE) }
        }.launchIn(viewModelScope)
    }

    fun selectDetailCard(card: Card?) {
        _state.update { it.copy(detailCard = card) }
    }

    fun initCardList(listSize: Int) {
        val rowSize = min(round(sqrt(listSize.toDouble())).toInt(), 7)
        val colSize = if (listSize % rowSize == 0) listSize / rowSize else listSize / rowSize + 1
        _state.update {
            _state.value.copy(
                cardList = List(listSize) { null },
                cardListSize = listSize,
                colSize = colSize,
                rowSize = rowSize
            )
        }
    }

    fun updateCardListSize(list: MutableList<Card?>) {
        val maxRow = 7
        val listSize = min(maxRow * maxRow, list.size.plus(1))
        val rowSize = min(round(sqrt(listSize.toDouble())).toInt(), maxRow)
        val colSize = if (listSize % rowSize == 0) listSize / rowSize else listSize / rowSize + 1
        val tempList = if (state.value.cardList.isEmpty()) {
            List(listSize) { null }
        } else {
            MutableList(listSize) { index ->
                if (index < state.value.cardList.size) list[index] else null
            }
        }
        _state.update {
            it.copy(
                cardList = tempList,
                cardListSize = listSize,
                colSize = colSize,
                rowSize = rowSize,
                upgradeCount = it.upgradeCount.plus(1)
            )
        }
    }


    fun changeSelectMode(
        isSelectMode: Boolean = false, startSelectIndex: Int = -1, afterSelectIndex: Int = -1
    ) {
        _state.update {
            it.copy(
                isSelectMode = isSelectMode,
                detailCard = if (isSelectMode) state.value.cardList[startSelectIndex] else null,
                startSelectedIndex = startSelectIndex,
                afterSelectedIndex = afterSelectIndex
            )
        }
    }


    fun cardCombination(startIndex: Int, index: Int) {
        _state.update { it.copy(startSelectedIndex = startIndex, afterSelectedIndex = index) }
        if (state.value.cardList[startIndex] != null && state.value.cardList[index] != null) {
            val combinationList = listOf(
                state.value.cardList[startIndex]!!, state.value.cardList[index]!!
            )
            viewModelScope.launch {
                delay(200)
                pandoraDataSource.combinationCard(combinationList).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            val tempList = state.value.cardList.toMutableList()
                            tempList[index] = result.data
                            tempList[startIndex] = null
                            if ((result.data?.upgrade ?: 0) >= (result.data?.potential ?: 0)) {
                                updateCardListSize(tempList)
                            } else {
                                _state.update {
                                    it.copy(
                                        cardList = tempList, upgradeCount = it.upgradeCount.plus(1)
                                    )
                                }
                            }
                            changeSelectMode()
                            checkWin(tempList) { checkWinResult ->
                                if (!checkWinResult) {
                                    checkGameOver(tempList, startIndex) { checkGameOverResult ->
                                        if (!checkGameOverResult) {
                                            checkGameOver(tempList, index)
                                        }
                                    }
                                }
                            }
                        }

                        is Resource.Error -> {
                            changeSelectMode()
                        }

                        is Resource.Loading -> {
                        }
                    }
                }.launchIn(this)
            }
        }
    }

    private fun checkWin(cardList: List<Card?>, callback: (isResult: Boolean) -> Unit) {
        pandoraDataSource.checkWin(cardList).onEach { result ->
            var checkResult = false
            when (result) {
                is Resource.Success -> {
                    if (result.data == true) {
                        _state.update { it.copy(pandoraState = PandoraState.STATE_GAME_WIN) }
                        checkResult = true
                    }
                }

                is Resource.Loading -> {}
                is Resource.Error -> {}
            }
            callback.invoke(checkResult)
        }.launchIn(viewModelScope)
    }

    private fun checkGameOver(
        cardList: List<Card?>, index: Int, callback: ((isResult: Boolean) -> Unit)? = null
    ) {
        pandoraDataSource.checkGameOver(
            cardList, index, state.value.rowSize, state.value.colSize
        ).onEach { result ->
            var isResult = false
            when (result) {
                is Resource.Loading -> {
                }

                is Resource.Success -> {
                    if (result.data == false) {
                        isResult = true
                        _state.update { it.copy(pandoraState = PandoraState.STATE_GAME_OVER) }
                    }
                }

                is Resource.Error -> {
                    Logger.log("Error : $index")
                }
            }
            callback?.invoke(isResult)
        }.launchIn(viewModelScope)
    }

    fun gatchaCard(index: Int) {
        _state.update { it.copy(cardGatchaIndex = index) }
        pandoraDataSource.gatchaBasicCard().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val tempList = _state.value.cardList.toMutableList()
                    tempList[index] = result.data

                    _state.update {
                        it.copy(
                            detailCard = result.data,
                            cardList = tempList,
                            cardGatchaLoading = -1,
                            cardGatchaIndex = -1,
                            openCardCount = it.openCardCount.plus(1)
                        )
                    }
                    checkWin(tempList) {
                        if (!it) {
                            checkGameOver(tempList, index)
                        }
                    }

                }

                is Resource.Loading -> {
                    _state.update { it.copy(cardGatchaLoading = it.cardGatchaLoading.plus(1)) }
                }

                is Resource.Error -> {
                    _state.update { it.copy(cardGatchaLoading = -1, cardGatchaIndex = -1) }
                }
            }
        }.launchIn(viewModelScope)
    }
}