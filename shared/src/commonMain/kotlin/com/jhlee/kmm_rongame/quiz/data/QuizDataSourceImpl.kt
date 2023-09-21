package com.jhlee.kmm_rongame.quiz.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.Firebase
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.quiz.domain.Quiz
import com.jhlee.kmm_rongame.quiz.domain.QuizDataSource
import com.jhlee.kmm_rongame.storage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.supervisorScope
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class QuizDataSourceImpl(db: AppDatabase, private val httpClient: HttpClient) : QuizDataSource {

    private val queries = db.dbQueries

    override fun getQuizListsFromRemote(): Flow<Resource<List<Quiz>>> = flow {

        Firebase.storage.reference.listAll().items.forEach { it ->
            emit(Resource.Loading())
            supervisorScope {
                try {
                    val list = async {
                        val csvString = httpClient.get(it.getDownloadUrl()).body<String>()
                        QuizDto.parseJson(csvString).map { quiz ->
                            quiz.toQuiz()
                        }
                    }
                    emit(Resource.Success(list.await()))
                } catch (e: Exception) {
                    emit(Resource.Error(e.message.toString()))
                }
            }
        }
        Logger.log("${Firebase.storage.reference.listAll().items.size}")
    }

    override fun getQuizListsFromDB(): Flow<Resource<List<Quiz>>> = flow {
        emit(Resource.Loading())
        supervisorScope {
            try {
                val quizList = async {
                    queries.getQuizList().executeAsList().map {
                        it.toQuiz()
                    }
                }
                emit(Resource.Success(quizList.await()))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    override fun insertQuizListToDB(list: List<Quiz>): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        supervisorScope {
            async {
                try {
                    list.forEach {
                        queries.insertQuizEntity(
                            it.id.toLong(),
                            it.category,
                            it.level.toLong(),
                            it.imageUrl,
                            it.answer.toLong(),
                            it.question,
                            Json.encodeToString(it.choiceList),
                            it.time,
                            it.chance.toLong(),
                            it.reward.toLong(),
                            it.description,
                            it.selected.toLong(),
                            it.durationTime,
                            false
                        )
                    }
                } catch (ignored: Exception) { }
            }.await()
            emit(Resource.Success(Unit))
        }
    }.flowOn(Dispatchers.Default)
}