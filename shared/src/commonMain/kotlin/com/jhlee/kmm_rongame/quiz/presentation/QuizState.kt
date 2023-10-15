package com.jhlee.kmm_rongame.quiz.presentation

import com.jhlee.kmm_rongame.quiz.domain.Quiz

data class QuizState(
    val quizList: List<Quiz> = emptyList(),
    val quizTime: Long = 0,
    val quizStatus: Int = QUIZ_STATUS_READY,
    val quizTimeProgress: Float = 0f,
    val quizChanceCount: Int = 0,
    val quizIndex: Int = 0,
    val quizTotalCount: Int = 0,
    val quizTotalPoint: Int = 0,
    val quizTotalAnswer: Int = 0,
    val error: String = "",
) {
    companion object {

        const val QUIZ_STATUS_READY = 0

        const val QUIZ_STATUS_LOADING = 1

        const val QUIZ_STATUS_LOAD_DONE = 2

        const val QUIZ_STATUS_ING = 3

        const val QUIZ_STATUS_DONE_FAIL = 4

        const val QUIZ_STATUS_DONE_SUCCESS = 5

        const val QUIZ_STATUS_ALL_FINISH = 6
    }
}