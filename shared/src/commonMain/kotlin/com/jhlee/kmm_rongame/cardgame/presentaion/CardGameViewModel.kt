package com.jhlee.kmm_rongame.cardgame.presentaion

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.cardgame.domain.CardGameDataSource
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CardGameViewModel(private val cardGameDataSource: CardGameDataSource) : ViewModel() {
    companion object {

        const val VIEWMODEL_KEY = "card_game_view_model"

        const val CARD_GAME_DEFAULT_SCREEN = 0

        const val CARD_GAME_ING_SCREEN = 1
    }

    private val _state = MutableStateFlow(CardGameState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {
        getMyCardList()
    }

    private fun getMyCardList() {
        cardGameDataSource.getMyCardList().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update { it.copy(myCardEntry = result.data ?: emptyList()) }
                }

                is Resource.Loading -> {}
                is Resource.Error -> {}
            }

        }.launchIn(viewModelScope)
    }

    fun setScreen(viewMode: Int, index: Int) {
        _state.update { it.copy(selectStageIndex = index, screenMode = viewMode) }
    }

    fun startGame() {
        Logger.log("startGame ${state.value.enemyEntry}")
        viewModelScope.launch {
            _state.update {
                it.copy(gameState = CardGameState.CARD_GAME_STATE_OPEN_CARD,
                    enemyEntry = List<Card?>(3) { null })
            }
        }
    }

    fun exitGame() {
        _state.update {
            it.copy(gameState = CardGameState.CARD_GAME_STATE_PRE,
                enemyEntry = emptyList(),
                resultMap = emptyMap(),
                mySelectedCardEntry = List(3) { null })
        }
    }

    fun getCardGame(index: Int) {
        cardGameDataSource.getEnemyList(index).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Logger.log("getCardGame ${result.data}")
                    _state.update {
                        it.copy(
                            enemySpareEntry = result.data ?: emptyList(),
                            selectStageIndex = index,
                            screenMode = CARD_GAME_ING_SCREEN
                        )
                    }
                }

                is Resource.Loading -> {}
                is Resource.Error -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun selectMyCard(selectCardSlot: Int, card: Card) {
        viewModelScope.launch {
            val tempList = _state.value.mySelectedCardEntry.toMutableList().apply {
                this[selectCardSlot] = card
            }
            _state.update { it.copy(mySelectedCardEntry = tempList) }
        }
    }

    fun openCard(index: Int) {
        Logger.log("openCard $index")
        viewModelScope.launch {
            if (state.value.enemyEntry[index] != null) {
                return@launch
            }

            val tempEnemyEntryList = state.value.enemySpareEntry.toMutableList().filter { card ->
                val isCardInSelectedList = state.value.enemyEntry.any {
                    it == card
                }
                !isCardInSelectedList
            }
            val tempEnemyList = state.value.enemyEntry.toMutableList()
            Logger.log("state.value.enemySpareEntry : ${state.value.enemySpareEntry}")
            Logger.log("state.value.enemyEntry : ${state.value.enemyEntry}")
            Logger.log("tempEnemyEntryList : $tempEnemyEntryList")
            Logger.log("tempEmptyList : $tempEnemyList")
            tempEnemyList[index] = tempEnemyEntryList.random()
            _state.update {
                it.copy(
                    enemyEntry = tempEnemyList,
                    gameState = CardGameState.CARD_GAME_STATE_OPEN_CARD_RESULT,
                    selectCardIndex = index
                )
            }
            delay(300)
            val enemyCard = state.value.enemyEntry[index]!!
            val myCard = state.value.mySelectedCardEntry[index]!!
            var myPower = myCard.power
            var enemyPower = enemyCard.power
            val sb = StringBuilder()
            myCard.type.forEach { myType ->
                enemyCard.type.forEach { enemyType ->
                    val strong = myType.strongList[enemyType.id]
                    if (strong != null) {
                        sb.append("${myType.name} 타입은 ${enemyType.name} 에게 $strong 추가로 공격한다.\n")
                        myPower += strong
                    }
                }
            }

            enemyCard.type.forEach { enemyType ->
                myCard.type.forEach { myType ->
                    val strong = enemyType.strongList[myType.id]
                    if (strong != null) {
                        sb.append("${enemyType.name} 타입은 ${myType.name} 에게 $strong 추가로 공격한다.\n")
                        enemyPower += strong
                    }
                }
            }
            val tempResultMap = state.value.resultMap.toMutableMap()
            tempResultMap[index] = listOf(enemyPower, myPower)
            _state.update {
                it.copy(
                    cardGameResult = sb.toString(),
                    resultMap = tempResultMap,
                    myFinalPower = myPower,
                    enemyFinalPower = enemyPower
                )
            }
        }
    }
}