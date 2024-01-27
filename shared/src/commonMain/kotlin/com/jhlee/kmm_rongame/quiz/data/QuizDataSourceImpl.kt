package com.jhlee.kmm_rongame.quiz.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.Firebase
import com.jhlee.kmm_rongame.constants.DBVersion
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
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class QuizDataSourceImpl(db: AppDatabase, private val httpClient: HttpClient) : QuizDataSource {

    private val queries = db.dbQueries

    override fun getQuizListsFromRemote(): Flow<Resource<List<Quiz>>> = flow {
        emit(Resource.Loading())
        supervisorScope {
            val versionUrl =
                Firebase.storage.reference.child(DBVersion.QUIZ_FB_PATH).child(DBVersion.VERSION)
                    .getDownloadUrl()
            val versionCsv = httpClient.get(versionUrl).body<String>().split(",")
            val version = versionCsv[0].toInt()
            val versionList = queries.getVersion(DBVersion.QUIZ_DB_TYPE.toLong(), version.toLong())
                .executeAsList()
            Logger.log("versionList : $versionList")
            Firebase.storage.reference.child(DBVersion.QUIZ_FB_PATH).listAll().items.forEach {
                if (it.name.startsWith(DBVersion.QUIZ_FB_PATH)) {
                    var result = true
                    val underscoreIndex = it.name.lastIndexOf("_") + 1
                    val extensionIndex = it.name.indexOf(".csv")
                    val version = it.name.substring(underscoreIndex, extensionIndex)
                    val containVersion =
                        versionList.any { anyItem -> anyItem.version == version.toLong() }
                    if (!containVersion) {
                        try {
                            Logger.log("version : $version")
                            val list = async {
                                val csvString = httpClient.get(it.getDownloadUrl()).body<String>()
                                QuizDto.parseJson(csvString).map { quiz ->
                                    quiz.toQuiz()
                                }
                            }.await()
                            list.forEach { quiz ->
                                queries.insertQuizEntity(
                                    quiz.id.toLong(),
                                    quiz.category,
                                    quiz.level.toLong(),
                                    quiz.imageUrl,
                                    quiz.answer.toLong(),
                                    quiz.question,
                                    Json.encodeToString(quiz.choiceList),
                                    quiz.time,
                                    quiz.chance.toLong(),
                                    quiz.reward.toLong(),
                                    quiz.description,
                                    quiz.selected.toLong(),
                                    quiz.durationTime,
                                    false
                                )
                            }
                        } catch (e: Exception) {
                            result = false
                            emit(Resource.Error(e.message.toString()))
                        }

                        queries.insertDBVersion(
                            version.toLong(), DBVersion.QUIZ_DB_TYPE.toLong(), result
                        )
                    }
                    val quizList =
                        queries.getQuizList().executeAsList().map { quiz -> quiz.toQuiz() }
                    emit(Resource.Success(quizList))
                }
            }
        }
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
                } catch (ignored: Exception) {
                }
            }.await()
            emit(Resource.Success(Unit))
        }
    }.flowOn(Dispatchers.Default)
}