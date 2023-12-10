package com.jhlee.kmm_rongame.main.presentation

import androidx.compose.runtime.Composable
import com.jhlee.kmm_rongame.core.domain.Resource
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
//        initCommonInfo()
        getUserInfo()
    }

    private fun initCommonInfo() {
        mainDataSource.getFlaticonToken().onEach { result ->
            when (result) {
                is Resource.Success -> {}
                is Resource.Error -> {}
                is Resource.Loading -> {}
            }

        }.launchIn(viewModelScope)
    }

    fun showDialog(dialogIndex: Int, createDialog: @Composable () -> Unit) {
        _state.update {
            it.copy(openDialog = dialogIndex, dialog = createDialog)
        }
    }

    fun setWholeScreen(isWholeScreen: Boolean) {
        _state.update { it.copy(isWholeScreenOpen = isWholeScreen) }
    }

    fun dismissDialog() {
        _state.update {
            it.copy(openDialog = MainState.NO_DIALOG, dialog = null)
        }
    }

    fun updateCardStage() {
        mainDataSource.updateCardStage().onEach { result ->
            when (result) {
                is Resource.Success -> {}
                is Resource.Error -> {}
                is Resource.Loading -> {}
            }

        }.launchIn(viewModelScope)
    }

    fun updateUserMoney(money: Int, callBack: ((isResult: Boolean) -> Unit)? = null) {
        val tempUserInfo = _state.value.userInfo?.let {
            it.copy(money = it.money + money)
        }
        if (tempUserInfo != null && tempUserInfo.money >= 0) {
            tempUserInfo.let { userInfo ->
                mainDataSource.updateUserInfo(userInfo).onEach { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.update {
                                it.copy(userInfo = result.data)
                            }
                            callBack?.invoke(true)
                        }

                        is Resource.Error -> {
                            callBack?.invoke(false)
                        }

                        is Resource.Loading -> {}
                    }
                }.launchIn(viewModelScope)
            }
        } else {
            callBack?.invoke(false)
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
        mainDataSource.insertUserInfo(UserInfo(1, name, 1000, 0)).onEach { res ->
            when (res) {
                is Resource.Error -> {
                    _state.value = state.value.copy(isLoading = false, error = res.message ?: "")
                }

                is Resource.Loading -> {
                    _state.value = state.value.copy(isLoading = true)
                }

                is Resource.Success -> {
                    getUserInfo()
                }
            }
        }.launchIn(viewModelScope)
    }
}