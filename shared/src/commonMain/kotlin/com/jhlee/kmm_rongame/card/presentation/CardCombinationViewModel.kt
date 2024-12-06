package com.jhlee.kmm_rongame.card.presentation

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardCombinationDataSource
import com.jhlee.kmm_rongame.core.domain.Resource
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CardCombinationViewModel(private val cardCombinationDataSource: CardCombinationDataSource) :
    ViewModel() {
    companion object {
        const val VIEWMODEL_KEY = "card_combination_view_model"
    }

    private val _state = MutableStateFlow(CardCombinationState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {
        getCombinationInfoList()
        getMyCardList()
    }

    private fun getCombinationInfoList() {
        cardCombinationDataSource.getCardCombinationList().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update { it.copy(cardCombinationInfo = result.data ?: emptyList()) }
                }

                is Resource.Error -> {}
                is Resource.Loading -> {}
            }
        }.launchIn(viewModelScope)
    }


    private fun getMyCardList() {
        cardCombinationDataSource.getCardList().onEach { result ->
            _state.update {
                it.copy(myCardList = result.data ?: emptyList())
            }

        }.launchIn(viewModelScope)
    }

    fun openCombine(cardId: Int) {
        cardCombinationDataSource.openCombine(cardId).onEach { result ->
            when (result) {
                is Resource.Error -> {}
                is Resource.Success -> {
                    getCombinationInfoList()
                }

                is Resource.Loading -> {}
            }
        }.launchIn(
            viewModelScope
        )
    }

    fun combinationCard() {
        if (_state.value.isCombining) return // 이미 작업 중이면 무시
        _state.update {
            it.copy(
                isCombining = true, animationMode = CardCombinationState.ANIMATION_DEFAULT
            )
        }
        cardCombinationDataSource.combinationCard(state.value.mySelectedCardEntry)
            .onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.update { it.copy(animationMode = CardCombinationState.ANIMATION_SUCCESS) }
                        _state.update {
                            it.copy(
                                combineCard = result.data
                            )
                        }
                    }

                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                animationMode = CardCombinationState.ANIMATION_FAIL,
                                error = result.message ?: ""
                            )
                        }
                    }

                    is Resource.Loading -> {}
                }

            }.launchIn(viewModelScope)
    }

    fun clearSelectedCard(index: Int) {
        viewModelScope.launch {
            val tempList = _state.value.mySelectedCardEntry.toMutableList().apply {
                this[index] = null
            }
            _state.update { it.copy(mySelectedCardEntry = tempList, error = "") }

        }

    }

    fun selectMyCard(selectCardSlot: Int, card: Card) {
        viewModelScope.launch {
            val tempList = _state.value.mySelectedCardEntry.toMutableList().apply {
                this[selectCardSlot] = card
            }
            _state.update { it.copy(mySelectedCardEntry = tempList, error = "") }
        }
    }

    fun clearAnimation() {
        _state.update { it.copy(isCombining = false, animationMode = CardCombinationState.ANIMATION_DEFAULT) }
    }

    fun clearSelected() {
        _state.update { it.copy(mySelectedCardEntry = List(2) { null }, error = "") }
    }


}