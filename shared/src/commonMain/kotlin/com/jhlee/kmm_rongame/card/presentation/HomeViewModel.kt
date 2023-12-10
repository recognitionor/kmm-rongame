package com.jhlee.kmm_rongame.card.presentation

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.HomeDataSource
import com.jhlee.kmm_rongame.core.domain.Resource
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel(private val cardDataSource: HomeDataSource) : ViewModel() {
    companion object {
        const val VIEWMODEL_KEY = "card_view_model"
    }

    private val _state = MutableStateFlow(HomeState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {
        getCardList()
    }

    fun getCardList() {
        cardDataSource.getCardList().onEach { result ->
            when (result) {
                is Resource.Error -> {
                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            cardList = result.data ?: emptyList(),
                        )
                    }

                }

                is Resource.Loading -> {
                }
            }
        }.launchIn(viewModelScope)
    }

    fun refreshDoneUserInfo() {
        _state.update {
            it.copy(updateUserInfo = false)
        }
    }

    fun toggleCardInfoDialog(card: Card?) {
        if (state.value.isShowCardInfoDialog) {
            _state.update {
                it.copy(
                    isShowCardInfoDialog = false, detailCardInfo = null
                )
            }
        } else {
            _state.update {
                it.copy(
                    isShowCardInfoDialog = true, detailCardInfo = card
                )
            }
        }
    }

    fun gatchaCard() {
        if (_state.value.isLoading) {
            return
        }
        cardDataSource.gatchaBasicCard().onEach { result ->
            when (result) {
                is Resource.Error -> {
                }

                is Resource.Loading -> {
                    _state.update {
                        it.copy(
                            cardRandomProgress = state.value.cardRandomProgress.plus(1),
                            isLoading = true
                        )
                    }
                }

                is Resource.Success -> {
                    if (result.data != null) {
                        getCardList()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isLoadDone = false,
                                gatchaCard = result.data,
                                updateUserInfo = true
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                isLoadDone = false,
                                error = result.message ?: "card is null"
                            )
                        }
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun selectScreen(homeScreen: Int) {
        _state.update {
            it.copy(homeScreenMode = homeScreen)
        }
    }
}