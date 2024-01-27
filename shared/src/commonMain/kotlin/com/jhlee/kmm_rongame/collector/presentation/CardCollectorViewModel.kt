package com.jhlee.kmm_rongame.collector.presentation

import com.jhlee.kmm_rongame.collector.domain.CardCollectorDataSource
import com.jhlee.kmm_rongame.collector.domain.CardCollectorWantedItem
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class CardCollectorViewModel(private val dbCollectorDataSource: CardCollectorDataSource) :
    ViewModel() {
    companion object {
        const val VIEWMODEL_KEY = "card_collector_view_model"
    }

    private val _state = MutableStateFlow(CardCollectorState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)
    fun addCommentIndex() {
        _state.update { it.copy(commentIndex = it.commentIndex + 1) }
    }

    init {
        getCardCollectorWantedList()
    }

    fun getCardSelectList(wantedItem: CardCollectorWantedItem) {
        dbCollectorDataSource.getSelectList(wantedItem).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update {
                        it.copy(isLoading = false, selectList = result.data ?: emptyList())
                    }
                }

                is Resource.Loading -> {
                    _state.update {
                        it.copy(isLoading = true)
                    }
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(isLoading = false, error = result.message ?: "")
                    }
                }

            }
        }.launchIn(viewModelScope)
    }

    fun selectScreen(screenMode: Int) {
        _state.update {
            it.copy(screenMode = screenMode)
        }
    }

    fun getCardCollectorWantedList() {
        Logger.log("getCardCollectorWantedList()")
        dbCollectorDataSource.getCollectorWantedList().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    Logger.log("Loading")
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            cardCollectorWantedItemList = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                }

                is Resource.Error -> {
                    Logger.log("Error")
                }
            }
        }.launchIn(viewModelScope)
    }
}