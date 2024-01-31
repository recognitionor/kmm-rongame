package com.jhlee.kmm_rongame.cardselector.presentaion

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.constants.CardFilterConst
import com.jhlee.kmm_rongame.core.util.Logger
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CardSelectViewModel(private val list: List<Card>) : ViewModel() {


    companion object {
        const val VIEWMODEL_KEY = "card_select_view_model"
    }

    private val _state = MutableStateFlow(CardSelectState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {
        _state.update { it.copy(originCardList = list, sortedCardList = list) }
        searchSortList()
    }

    fun toggleReverseFilter() {
        _state.update { it.copy(isReverseFilter = !state.value.isReverseFilter) }
        searchSortList()
    }

    fun selectItem(item: Card) {
        val newList = state.value.selectedCardList.toMutableList()
        if (newList.contains(item)) {
            newList.remove(item)
        } else {
            newList.add(item)
        }
        _state.update { it.copy(selectedCardList = newList.toList()) }
    }

    fun selectScreen(screenState: Int, card: Card? = null) {
        _state.update { it.copy(screenState = screenState, detailCardInfo = card) }
    }

    fun searchSortList(
        keyword: String = state.value.search, sortIndex: Int = state.value.sortFilter
    ) {
        _state.update { it.copy(isLoading = true, sortFilter = sortIndex, search = keyword) }
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

                    else -> {
                        -it.cardId
                    }
                }
            }.thenBy {
                it.cardId
            })


            if (state.value.isReverseFilter) {
                _state.update {
                    it.copy(sortedCardList = sortedList.reversed(), isLoading = false)
                }
            } else {
                _state.update {
                    it.copy(sortedCardList = sortedList, isLoading = false)
                }
            }
        }
    }
}