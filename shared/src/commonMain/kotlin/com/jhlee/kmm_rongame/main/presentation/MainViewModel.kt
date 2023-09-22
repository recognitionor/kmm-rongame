package com.jhlee.kmm_rongame.main.presentation

import androidx.compose.runtime.Composable
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.main.domain.MainDataSource
import com.jhlee.kmm_rongame.main.domain.UserInfo
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MainViewModel(private val mainDataSource: MainDataSource) : ViewModel() {
    companion object {
        const val VIEWMODEL_KEY = "main_view_model"
    }

    private val _state = MutableStateFlow(MainState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {
        Logger.log("MainViewModel init")
        getUserInfo()
    }

    fun showDialog(dialogIndex: Int, createDialog: @Composable () -> Unit) {
        Logger.log("showDialog $dialogIndex")
        _state.update {
            it.copy(openDialog = dialogIndex, dialog = createDialog)
        }
        Logger.log("showDialog post ${_state.value.openDialog}")
    }

    fun dismissDialog() {
        _state.update {
            it.copy(openDialog = MainState.NO_DIALOG, dialog = null)
        }
    }

    fun getUserInfo() {
        mainDataSource.getUserInfo().onEach { res ->
            when (res) {
                is Resource.Error -> {
                    _state.value = state.value.copy(isLoading = false, error = res.message ?: "")
                }

                is Resource.Loading -> {
                    _state.value = state.value.copy(isLoading = true)
                }

                is Resource.Success -> {
                    _state.value = state.value.copy(userInfo = res.data, isLoading = false)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun registerUser(name: String) {
        Logger.log("registerUser")
        mainDataSource.insertUserInfo(UserInfo(1, name, 1000)).onEach { res ->
            when (res) {
                is Resource.Error -> {
                    Logger.log("registerUser error")
                    _state.value = state.value.copy(isLoading = false, error = res.message ?: "")
                }

                is Resource.Loading -> {
                    Logger.log("registerUser loading")
                    _state.value = state.value.copy(isLoading = true)
                }

                is Resource.Success -> {
                    Logger.log("registerUser success")
                    getUserInfo()
                }
            }
        }.launchIn(viewModelScope)
    }
}