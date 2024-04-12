package com.jhlee.kmm_rongame.pandora.presentation

import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.pandora.domain.PandoraDataSource
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class PandoraListViewModel(
    private val pandoraDataSource: PandoraDataSource,
) : ViewModel() {
    companion object {
        const val VIEWMODEL_KEY = "pandora_list_view_model"
    }

    private val _state = MutableStateFlow(PandoraListState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {
        getStageList()
    }

    fun test() {
        pandoraDataSource.test().launchIn(viewModelScope)
    }

    fun selectScreen(screenMode: Int) {
        _state.update { it.copy(pandoraScreen = screenMode) }
    }

    fun getStageList() {
        pandoraDataSource.getStageList().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { list ->
                        _state.update { it.copy(stageList = list, isLoading = false) }
                    }
                }

                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }

                is Resource.Error -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }.launchIn(viewModelScope)
    }
}