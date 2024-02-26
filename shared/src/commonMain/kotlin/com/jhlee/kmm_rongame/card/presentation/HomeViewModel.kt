package com.jhlee.kmm_rongame.card.presentation

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardDataSource
import com.jhlee.kmm_rongame.constants.CardFilterConst
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val cardDataSource: CardDataSource) : ViewModel() {
    companion object {
        const val VIEWMODEL_KEY = "card_view_model"
    }

    private val _state = MutableStateFlow(HomeState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {
        getMyCardList()
    }


    fun getMyCardList() {
        cardDataSource.getMyCardList().onEach { result ->
            when (result) {
                is Resource.Error -> {
                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            originCardList = result.data ?: emptyList(),
                        )
                    }
                    searchSortCardList()
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
                        getMyCardList()
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

    fun toggleReverseFilter() {
        _state.update { it.copy(isReverseFilter = !state.value.isReverseFilter) }
        searchSortCardList()
    }

    fun searchSortCardList(
        keyword: String = state.value.searchKeyword, sortIndex: Int = state.value.sortFilter
    ) {
        _state.update { it.copy(isLoading = true, sortFilter = sortIndex, searchKeyword = keyword) }
        viewModelScope.launch {
            val sortedList = _state.value.originCardList.filter {
                if (keyword.isEmpty()) {
                    true
                } else {
                    it.name.contains(
                        keyword, ignoreCase = true
                    ) || it.nameEng.contains(keyword, ignoreCase = true)
                }

            }.sortedWith(compareBy<Card> {
                when (state.value.sortFilter) {
                    CardFilterConst.ID -> {
                        it.cardId
                    }

                    CardFilterConst.POWER -> {
                        -it.power
                    }

                    CardFilterConst.POTENTIAL -> {
                        -it.potential
                    }

                    CardFilterConst.GRADE -> {
                        -it.grade
                    }

                    CardFilterConst.UPGRADE -> {
                        -it.upgrade
                    }
                    CardFilterConst.RECENT -> {
                        it.id
                    }

                    else -> {
                        -it.cardId
                    }
                }
            }.thenBy {
                it.cardId
            })


            if (state.value.isReverseFilter) {
                _state.update {
                    it.copy(cardList = sortedList.reversed(), isLoading = false)
                }
            } else {
                _state.update {
                    it.copy(cardList = sortedList, isLoading = false)
                }
            }
        }
    }
}