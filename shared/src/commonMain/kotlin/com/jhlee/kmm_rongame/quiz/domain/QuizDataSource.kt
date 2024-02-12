package com.jhlee.kmm_rongame.quiz.domain

import com.jhlee.kmm_rongame.core.domain.Resource
import kotlinx.coroutines.flow.Flow

interface QuizDataSource {
    fun getQuizListsFromRemote(): Flow<Resource<List<Quiz>>>
    fun getQuizListsFromDB(): Flow<Resource<List<Quiz>>>
    fun insertQuizListToDB(list: List<Quiz>): Flow<Resource<Unit>>

    fun updateQuiz(quiz: Quiz): Flow<Resource<Unit>>
}