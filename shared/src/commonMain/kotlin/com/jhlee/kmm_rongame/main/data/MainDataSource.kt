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
import com.jhlee.kmm_rongame.constants.DBVersion
import com.jhlee.kmm_rongame.core.data.HttpConst
import com.jhlee.kmm_rongame.core.data.HttpConst.FLATICON_URL
import com.jhlee.kmm_rongame.core.data.ImageStorage
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.main.domain.FlaticonAuth
import com.jhlee.kmm_rongame.main.domain.MainDataSource
import com.jhlee.kmm_rongame.main.domain.UserInfo
import com.jhlee.kmm_rongame.storage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.url
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.Clock
import migrations.CardInfoEntity

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


    private suspend fun initCardInfo(
        isReset: Boolean = false,
        csvString: String,
        totalSize: Int,
        flowCollector: FlowCollector<Resource<Triple<Int, Int, Int>>>,
        isResult: (Boolean) -> Unit,

        ) {
        var result = true
        val list = CardInfoDto.parseJson(csvString)
        list.forEachIndexed { index, it ->
            var cardInfo: CardInfoEntity? = null
            try {
                cardInfo = queries.getCardInfo(it.id.toLong()).executeAsOne()
            } catch (_: Exception) {
            }
            val time = Clock.System.now().epochSeconds
            val image: String? = if (isReset || cardInfo?.image == null || !ImageStorage.existImage(
                    cardInfo.image ?: ""
                )
            ) {
                val imgArray = httpClient.get(it.image).body<ByteArray>()
                ImageStorage.saveImageAsync(imgArray)

            } else {
                cardInfo.image
            }
            try {
                queries.insertCardInfoEntity(
                    it.id.toLong(),
                    it.name,
                    it.nameEng,
                    it.grade.toLong(),
                    image,
                    it.description,
                    it.type
                )
            } catch (e: Exception) {
                result = false
            }
            flowCollector.emit(
                Resource.Loading(
                    Triple(
                        DBVersion.CARD_DB_TYPE, totalSize, index + 1
                    )
                )
            )
        }
        isResult.invoke(result)
    }

    private suspend fun initCardCombination(
        csvString: String,
        totalSize: Int,
        flowCollector: FlowCollector<Resource<Triple<Int, Int, Int>>>,
        isResult: (Boolean) -> Unit
    ) {
        val list = CardCombinationDto.parseJson(csvString)
        var result = true
        list.forEach { dto ->
            val item1Id = queries.getCardInfoFromName(dto.item1).executeAsOne().id
            val item2Id = queries.getCardInfoFromName(dto.item2).executeAsOne().id
            queries.insertCardCombineEntity(
                dto.id.toLong(), item1Id, item2Id, dto.result
            )

            dto.result.split(",").forEachIndexed { index, it ->
                val temp = it.split(":")
                val name = temp[0]
                val percent = temp[1]
                try {
                    val card = queries.getCardInfoFromName(name).executeAsOne()
                    val isExist =
                        queries.existCardPadigree(card.id, item1Id, item2Id).executeAsOne()
                    if (!isExist) {
                        queries.insertCardPadigree(
                            card.id, item1Id, item2Id, percent.toLong(), 0
                        )
                    }
                } catch (_: Exception) {
                    result = false
                }
                flowCollector.emit(
                    Resource.Loading(
                        Triple(
                            DBVersion.CARDCOMBINE_DB_TYPE, totalSize, index + 1
                        )
                    )
                )
            }
        }
        isResult(result)
    }

    private suspend fun initCardType(
        csvString: String,
        totalSize: Int,
        flowCollector: FlowCollector<Resource<Triple<Int, Int, Int>>>,
        isResult: (Boolean) -> Unit
    ) {
        val list = CardTypeDto.parseJson(csvString)
        var result = true
        list.forEachIndexed { index, it ->
            try {
                val strongStr = it.strongList.ifEmpty { "" }
                queries.insertCardTypeEntity(it.id.toLong(), it.name, strongStr)
                CardTypeDto.parseStrongList(it.strongList)
                CardInfoManager.CARD_TYPE_ID_MAP[it.id] = it.name
                CardInfoManager.CARD_TYPE_MAP[it.name] = CardType(
                    it.id, it.name, CardTypeDto.parseStrongList(it.strongList), hashMapOf()
                )
            } catch (e: Exception) {
                result = false
            }
            flowCollector.emit(
                Resource.Loading(
                    Triple(
                        DBVersion.CARDTYPE_DB_TYPE, totalSize, index + 1
                    )
                )
            )
        }
        CardInfoManager.CARD_TYPE_MAP.forEach { entry ->
            entry.value.strongList.forEach { strong ->
                CardInfoManager.CARD_TYPE_MAP[strong.key]?.let {
                    it.weakList[entry.key] = strong.value
                }
            }
        }
        isResult(result)
    }

    override fun initCardWholeData(isReset: Boolean): Flow<Resource<Triple<Int, Int, Int>>> = flow {
        emit(Resource.Loading())
        supervisorScope {
            try {
                initVersion(isReset, this@flow)
            } catch (e: Exception) {
                emit(Resource.Error("data load fail ${e.message}"))
                return@supervisorScope
            }

            emit(Resource.Success(Triple(0, 0, 0)))
        }
    }

    private suspend fun initVersion(
        isReset: Boolean, flowCollector: FlowCollector<Resource<Triple<Int, Int, Int>>>
    ) {
        val time = Clock.System.now().toEpochMilliseconds()
        DBVersion.DB_CARD_FILE_LIST.forEachIndexed { index, path ->
            val versionTemp = Firebase.storage.reference.child(path).child(DBVersion.VERSION)
            val csvTemp = httpClient.get(versionTemp.getDownloadUrl()).body<String>().split(",")
            val version = csvTemp[0]
            val totalSize = csvTemp[1].toInt()
            for (i in 0..version.toInt()) {
                val versionList =
                    queries.getVersion(index.toLong(), version.toLong()).executeAsList()

                if (versionList.isEmpty() || index == 1) {
                    val filePath = "${path}_$version.csv"
                    val url =
                        Firebase.storage.reference.child(path).child(filePath).getDownloadUrl()
                    val csvStr = httpClient.get(url).body<String>()
                    when (index) {
                        0 -> {
                            initCardInfo(isReset, csvStr, totalSize, flowCollector) { result ->
                                queries.insertDBVersion(
                                    version.toLong(), index.toLong(), result
                                )
                            }
                        }

                        1 -> {
                            initCardType(csvStr, totalSize, flowCollector) { result ->
                                queries.insertDBVersion(
                                    version.toLong(), index.toLong(), result
                                )
                            }
                        }

                        2 -> {
                            initCardCombination(
                                csvStr, totalSize, flowCollector
                            ) { result: Boolean ->
                                queries.insertDBVersion(
                                    version.toLong(), index.toLong(), result
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}