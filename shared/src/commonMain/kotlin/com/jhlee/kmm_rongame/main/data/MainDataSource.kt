package com.jhlee.kmm_rongame.main.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.Firebase
import com.jhlee.kmm_rongame.card.data.CardCombinationDto
import com.jhlee.kmm_rongame.card.data.CardInfoDto
import com.jhlee.kmm_rongame.card.data.CardInfoManager
import com.jhlee.kmm_rongame.card.data.CardTypeDto
import com.jhlee.kmm_rongame.card.domain.CardType
import com.jhlee.kmm_rongame.core.data.HttpConst
import com.jhlee.kmm_rongame.core.data.HttpConst.FLATICON_URL
import com.jhlee.kmm_rongame.core.data.ImageStorage
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.main.domain.FlaticonAuth
import com.jhlee.kmm_rongame.main.domain.MainDataSource
import com.jhlee.kmm_rongame.main.domain.UserInfo
import com.jhlee.kmm_rongame.storage
import database.CardInfoEntity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.url
import kotlinx.coroutines.async
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.Clock

class MainDataSourceImpl(
    db: AppDatabase, private val httpClient: HttpClient,
) : MainDataSource {
    private val queries = db.dbQueries

    override fun getFlaticonToken(): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            var token = ""
            var needTokenRefresh = false
            try {
                val commonInfo = queries.getCommonInfo().executeAsOne()
                if (Clock.System.now().epochSeconds >= (commonInfo.expires ?: 0)) {
                    needTokenRefresh = true
                } else {
                    token = queries.getCommonInfo().executeAsOne().token
                }
            } catch (ignored: Exception) {
            }
            if (needTokenRefresh) {
                val auth: FlaticonAuth = httpClient.post {
                    url(FLATICON_URL)
                    parameter(HttpConst.ParamKey.FLATICON_APIKEY, HttpConst.FLATICON_API_KEY)
                }.body()
                queries.insertCommon(auth.data.token, auth.data.expires.toLong())
                token = auth.data.token
            }
            emit(Resource.Success(token))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun getUserInfo(): Flow<Resource<UserInfo>> {
        return flow {
            try {
                emit(Resource.Loading())
                val userInfoList =
                    queries.getUserInfo().asFlow().mapToList(currentCoroutineContext())
                        .firstOrNull()
                val userInfo = userInfoList?.firstOrNull()
                if (userInfo != null) {
                    emit(Resource.Success(userInfo.toUser()))
                } else {
                    emit(Resource.Error("No user found"))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "error getUserInfo"))
            }
        }
    }

    override fun insertUserInfo(userInfo: UserInfo): Flow<Resource<UserInfo>> {
        return flow {
            try {
                emit(Resource.Loading())
                queries.insertUser(userInfo.name, userInfo.money.toLong(), 0)
                emit(Resource.Success(userInfo))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "error getUserInfo"))
            }
        }
    }

    override fun updateUserInfo(userInfo: UserInfo): Flow<Resource<UserInfo>> = flow {
        emit(Resource.Loading())
        queries.updateUser(userInfo.money.toLong())
        val userInfoList =
            queries.getUserInfo().asFlow().mapToList(currentCoroutineContext()).firstOrNull()
        val userInfo = userInfoList?.firstOrNull()
        if (userInfo != null) {
            emit(Resource.Success(userInfo.toUser()))
        } else {
            emit(Resource.Error("No user found"))
        }
    }

    override fun updateCardStage(): Flow<Resource<UserInfo>> = flow {
        emit(Resource.Loading())
        queries.nextCardStage()
        val userInfoList =
            queries.getUserInfo().asFlow().mapToList(currentCoroutineContext()).firstOrNull()
        val userInfo = userInfoList?.firstOrNull()
        if (userInfo != null) {
            emit(Resource.Success(userInfo.toUser()))
        } else {
            emit(Resource.Error("No user found"))
        }
    }


    private suspend fun initCardInfo(isReset: Boolean = false) {
        Logger.log("initCardInfo()")
        Firebase.storage.reference.child("card.csv").let {
            supervisorScope {
                val list = async {
                    val csvString = httpClient.get(it.getDownloadUrl()).body<String>()
                    return@async CardInfoDto.parseJson(csvString)
                }.await()
                list.forEach {
                    var cardInfo: CardInfoEntity? = null
                    try {
                        cardInfo = queries.getCardInfo(it.id.toLong()).executeAsOne()
                    } catch (_: Exception) {
                    }

                    val image: String? =
                        if (isReset || cardInfo?.image == null || !ImageStorage.existImage(
                                cardInfo.image ?: ""
                            )
                        ) {
                            val imgArray = httpClient.get(it.image).body<ByteArray>()
                            ImageStorage.saveImage(imgArray)
                        } else {
                            cardInfo.image
                        }
                    queries.insertCardInfoEntity(
                        it.id.toLong(),
                        it.name,
                        it.nameEng,
                        it.grade.toLong(),
                        image,
                        it.description,
                        it.type
                    )
                }
                queries.getCardInfoList().executeAsList().forEach {
                    Logger.log("cardInfo : $it")
                }
            }
        }
    }

    private suspend fun initCardCombination() {
        Firebase.storage.reference.child("card_combine.csv").let {
            supervisorScope {
                val list = async {
                    val csvString = httpClient.get(it.getDownloadUrl()).body<String>()
                    CardCombinationDto.parseJson(csvString)
                }.await()
                list.forEach {
                    val item1Id = queries.getCardInfoFromName(it.item1).executeAsOne().id
                    val item2Id = queries.getCardInfoFromName(it.item2).executeAsOne().id
                    queries.insertCardCombineEntity(
                        it.id.toLong(), item1Id, item2Id, it.result
                    )
                }
                queries.getCardCombineList().executeAsList().forEach {
                    Logger.log("combine : $it")
                }
            }
        }
    }

    private suspend fun initCardType() {
        Logger.log("initCardType()")
        Firebase.storage.reference.child("card_type.csv").let {
            supervisorScope {
                val list = async {
                    val csvString = httpClient.get(it.getDownloadUrl()).body<String>()
                    CardTypeDto.parseJson(csvString)
                }.await()
                list.forEach {
                    Logger.log("it $it")
                    val strongStr = it.strongList.ifEmpty { "" }
                    queries.insertCardTypeEntity(it.id.toLong(), it.name, strongStr)
                    CardTypeDto.parseStrongList(it.strongList)
                    CardInfoManager.CARD_TYPE_ID_MAP[it.id] = it.name
                    CardInfoManager.CARD_TYPE_MAP[it.name] = CardType(
                        it.id, it.name, CardTypeDto.parseStrongList(it.strongList), hashMapOf()
                    )
                }
                CardInfoManager.CARD_TYPE_MAP.forEach { entry ->
                    entry.value.strongList.forEach { strong ->
                        CardInfoManager.CARD_TYPE_MAP[strong.key]?.let {
                            it.weakList[entry.key] = strong.value
                        }
                    }
                }
            }
        }
    }

    override fun initCardWholeData(isReset: Boolean): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            initCardType()
            initCardInfo(isReset)
            initCardCombination()
        } catch (e: Exception) {
            emit(Resource.Error("data load fail ${e.message}"))
            return@flow
        }
        emit(Resource.Success(true))
    }


}