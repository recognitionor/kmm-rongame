package com.jhlee.kmm_rongame.quiz.presentation

import com.jhlee.kmm_rongame.quiz.domain.Quiz

data class QuizState(
    val isLoading: Boolean = false,
    val isLoadDone: Boolean = false,
    val quizList: List<Quiz> = emptyList(),
    val error: String = "",
)