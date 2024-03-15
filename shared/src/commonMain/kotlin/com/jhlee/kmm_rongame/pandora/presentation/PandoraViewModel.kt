package com.jhlee.kmm_rongame.pandora.presentation

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

class PandoraViewModel(
    private val pandoraDataSource: PandoraDataSource,
) : ViewModel() {
    companion object {
        const val VIEWMODEL_KEY = "pandora_view_model"
    }

    private val _state = MutableStateFlow(PandoraState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    fun initCardList(listSize: Int) {
        Logger.log("initCardList : $listSize")
        _state.update {
            _state.value.copy(cardList = List(listSize * listSize) { null })
        }
        Logger.log("post initCardList : ${_state.value.cardList}")
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
        Logger.log("startIndex : $startIndex")
        Logger.log("index : $index")
        _state.update { it.copy(startSelectedIndex = startIndex, afterSelectedIndex = index) }
        Logger.log("cardCombination : ${state.value.cardList.size}")
        Logger.log("startSelectedIndex : ${state.value.startSelectedIndex}")
        Logger.log("afterSelectedIndex : ${state.value.afterSelectedIndex}")
        if (state.value.cardList[startIndex] != null && state.value.cardList[index] != null) {
            val combinationList = listOf(
                state.value.cardList[startIndex]!!, state.value.cardList[index]!!
            )
            viewModelScope.launch {
                delay(300)
                pandoraDataSource.combinationCard(combinationList).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            val tempList = state.value.cardList.toMutableList()
                            tempList[index] = result.data
                            tempList[startIndex] = null
                            _state.update { it.copy(cardList = tempList) }
                            changeSelectMode()
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

    fun gatchaCard(index: Int) {
        _state.update { it.copy(cardGatchaIndex = index) }
        pandoraDataSource.gatchaBasicCard().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Logger.log("success : ${result.data}")
                    val tempList = _state.value.cardList.toMutableList()
                    tempList[index] = result.data
                    _state.update {
                        it.copy(
                            detailCard = result.data,
                            cardList = tempList,
                            cardGatchaLoading = -1,
                            cardGatchaIndex = -1
                        )
                    }
                }

                is Resource.Loading -> {
                    _state.update { it.copy(cardGatchaLoading = it.cardGatchaLoading.plus(1)) }
//                    Logger.log("loading : ${result.data}")
                }

                is Resource.Error -> {
//                    Logger.log("error : ${result.data}")
                    _state.update { it.copy(cardGatchaLoading = -1, cardGatchaIndex = -1) }
                }
            }
        }.launchIn(viewModelScope)
    }
}