package com.jhlee.kmm_rongame.quiz.presentation

import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.quiz.domain.Quiz
import com.jhlee.kmm_rongame.quiz.domain.QuizDataSource
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class QuizViewModel(private val dataSource: QuizDataSource) : ViewModel() {
    private val _state = MutableStateFlow(QuizState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {
        getQuizListFromDB()
    }

    private fun getQuizListFromDB() {
        dataSource.getQuizListsFromDB().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.update {
                        it.copy(isLoading = true)
                    }
                }

                is Resource.Error -> {
                    getQuizListFromFireBase()
                    _state.update {
                        it.copy(isLoading = false)
                    }
                }

                is Resource.Success -> {
                    if ((result.data?.size ?: 0) > 0) {
                        _state.update {
                            it.copy(isLoading = false, quizList = result.data ?: emptyList())
                        }
                    } else {
                        getQuizListFromFireBase()
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getQuizListFromFireBase() {
        dataSource.getQuizListsFromRemote().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    insertQuizListToDB(result.data ?: emptyList())
                }

                is Resource.Loading -> {
                }

                is Resource.Error -> {
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun insertQuizListToDB(list: List<Quiz>) {
        dataSource.insertQuizListToDB(list).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    getQuizListFromDB()
                }

                is Resource.Error -> {

                }

                is Resource.Loading -> {

                }
            }

        }.launchIn(viewModelScope)
    }
}