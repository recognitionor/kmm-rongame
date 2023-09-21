package com.jhlee.kmm_rongame.main.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.main.domain.MainDataSource
import com.jhlee.kmm_rongame.main.domain.UserInfo
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(private val mainDataSource: MainDataSource) : ViewModel() {
    companion object {
        const val VIEWMODEL_KEY = "reward_view_model"
    }

    var newUserName: String by mutableStateOf("")

    private val _state = mutableStateOf(MainState())

    val state: State<MainState> = _state

    init {
        getUserInfo()
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