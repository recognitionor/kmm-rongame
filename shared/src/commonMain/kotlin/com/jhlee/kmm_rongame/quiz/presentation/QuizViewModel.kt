package com.jhlee.kmm_rongame.quiz.presentation

import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.quiz.domain.Quiz
import com.jhlee.kmm_rongame.quiz.domain.QuizDataSource
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class QuizViewModel(private val dataSource: QuizDataSource) : ViewModel() {
    private val _state = MutableStateFlow(QuizState())

    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {
        getQuizListFromDB()
    }

    fun clearQuizInfo() {
        _state.update {
            it.copy(quizList = emptyList(), quizIndex = 0)
        }
    }

    fun summitAnswer(index: Int, quiz: Quiz) {
        _state.update {
            it.copy(quizList = it.quizList.toMutableList().apply {
                this[_state.value.quizIndex] = this[_state.value.quizIndex].copy(selected = index + 1)
            })
        }
        if (index + 1 == quiz.answer) {
            _state.update {
                it.copy(
                    quizTime = 0,
                    quizTimeProgress = 0f,
                    quizStatus = QuizState.QUIZ_STATUS_DONE_SUCCESS
                )
            }
        } else {
            if (quiz.chance < _state.value.quizChanceCount - 1) {
                _state.update {
                    it.copy(
                        quizChanceCount = _state.value.quizChanceCount - 1,
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        quizChanceCount = _state.value.quizChanceCount - 1,
                        quizStatus = QuizState.QUIZ_STATUS_DONE_FAIL
                    )
                }
            }
        }
    }

    private fun getQuizListFromDB() {
        dataSource.getQuizListsFromDB().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.update {
                        it.copy(quizStatus = QuizState.QUIZ_STATUS_LOADING)
                    }
                }

                is Resource.Error -> {
                    getQuizListFromFireBase()
                    _state.update {
                        it.copy(quizStatus = QuizState.QUIZ_STATUS_READY)
                    }
                }

                is Resource.Success -> {
                    if ((result.data?.size ?: 0) > 0) {
                        _state.update {
                            it.copy(
                                quizStatus = QuizState.QUIZ_STATUS_ING,
                                quizList = result.data ?: emptyList()
                            )
                        }
                        quizStart()
                    } else {
                        getQuizListFromFireBase()
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun nextStage() {
        if (_state.value.quizList.size - 1 <= _state.value.quizIndex) {
            val quizCount = _state.value.quizList.size
            var quizPoint = 0
            var quizAnswer = 0
            _state.value.quizList.forEach {
                if (it.answer == it.selected) {
                    quizAnswer++
                    quizPoint += it.reward
                }
                Logger.log(it.question + ": " + it.selected)
            }
            _state.update {
                it.copy(
                    quizTotalCount = quizCount,
                    quizTotalAnswer = quizAnswer,
                    quizTotalPoint = quizPoint,
                    quizIndex = _state.value.quizIndex.plus(1),
                    quizStatus = QuizState.QUIZ_STATUS_ALL_FINISH
                )
            }
        } else {
            _state.update { it.copy(quizIndex = _state.value.quizIndex.plus(1)) }
            quizStart()
        }
    }

    private fun quizStart() {
        val quiz = _state.value.quizList[_state.value.quizIndex]
        _state.update {
            it.copy(
                quizStatus = QuizState.QUIZ_STATUS_ING,
                quizTimeProgress = 0f,
                quizChanceCount = quiz.chance
            )
        }
        viewModelScope.launch {
            val timeOffset = Clock.System.now().epochSeconds
            val quizTime = quiz.time
            var progressTime = 0L
            while (quizTime >= progressTime) {
                if (_state.value.quizStatus != QuizState.QUIZ_STATUS_ING) {
                    break
                }

                delay(1000)
                progressTime = Clock.System.now().epochSeconds - timeOffset
                _state.update {
                    it.copy(
                        quizTimeProgress = (progressTime.toFloat() / quizTime),
                        quizTime = progressTime
                    )
                }
                if (quizTime <= progressTime) {
                    break
                }
            }
            if (_state.value.quizStatus == QuizState.QUIZ_STATUS_ING) {
                _state.update {
                    it.copy(
                        quizStatus = QuizState.QUIZ_STATUS_DONE_FAIL,
                    )
                }
            }
        }
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