package com.jhlee.kmm_rongame.test.presentation

import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.di.AppModule
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

class TestViewModel(private val appModule: AppModule) : ViewModel() {

    companion object {
        const val VIEWMODEL_KEY = "test_view_model"
    }

    private val _state = MutableStateFlow(TestState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {
//        getFirebase()
        getQuiz()
    }

    private fun getQuiz() {
        appModule.dbQuizDataSource.getQuizListsFromDB().onEach {  }.launchIn(viewModelScope)
        appModule.dbQuizDataSource.getQuizListsFromRemote().onEach {  }.launchIn(viewModelScope)
    }


    fun getFirebase() {
        appModule.dbTestDataSource.getTestList().onEach { }.launchIn(viewModelScope)

    }

}