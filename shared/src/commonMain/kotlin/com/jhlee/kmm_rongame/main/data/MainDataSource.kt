package com.jhlee.kmm_rongame.main.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.File
import com.jhlee.kmm_rongame.Firebase
import com.jhlee.kmm_rongame.card.data.CardCombinationDto
import com.jhlee.kmm_rongame.card.data.CardInfoDto
import com.jhlee.kmm_rongame.card.data.CardInfoManager
import com.jhlee.kmm_rongame.card.data.CardTypeDto
import com.jhlee.kmm_rongame.card.domain.CardType
import com.jhlee.kmm_rongame.constants.DBVersion
import com.jhlee.kmm_rongame.constants.DBVersion.CARDCOMBINE_DB_TYPE
import com.jhlee.kmm_rongame.constants.DBVersion.CARDTYPE_DB_TYPE
import com.jhlee.kmm_rongame.constants.DBVersion.CARD_DB_TYPE
import com.jhlee.kmm_rongame.constants.DBVersion.QUIZ_DB_TYPE
import com.jhlee.kmm_rongame.core.data.HttpConst
import com.jhlee.kmm_rongame.core.data.HttpConst.FLATICON_URL
import com.jhlee.kmm_rongame.core.data.ImageStorage
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.isAndroid
import com.jhlee.kmm_rongame.main.domain.FlaticonAuth
import com.jhlee.kmm_rongame.main.domain.MainDataSource
import com.jhlee.kmm_rongame.main.domain.UserInfo
import com.jhlee.kmm_rongame.quiz.data.QuizDto
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
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withTimeout
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
                queries.insertUser(userInfo.name, userInfo.money.toLong(), 0, 0)
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
        val firstUserInfo = userInfoList?.firstOrNull()
        if (firstUserInfo != null) {
            emit(Resource.Success(firstUserInfo.toUser()))
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
        isResult: (Boolean) -> Unit
    ) {
        Logger.log("initCardInfo")
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
                        CARD_DB_TYPE, totalSize, index + 1
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
                } catch (e: Exception) {
                    Logger.log("error : $e")
                    result = false
                }
                flowCollector.emit(
                    Resource.Loading(
                        Triple(
                            CARDCOMBINE_DB_TYPE, totalSize, index + 1
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
                        CARDTYPE_DB_TYPE, totalSize, index + 1
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
        var isForceReset = isReset
        emit(Resource.Loading())
        try {
            supervisorScope {
                if (isReset) {
                    try {
                        queries.clearDB()
                    } catch (_: Exception) {
                    }
                } else {
                    if (!isAndroid()) {
                        val imageTemp = queries.getCardInfo(0).executeAsOne().image
                        Logger.log("imageTemp : $imageTemp")
                        val temp = ImageStorage.getImage(imageTemp ?: "")
                        Logger.log("temp : $temp")
                        if (temp == null) {
                            Logger.log("clearDB")
                            queries.clearDB()
                            isForceReset = true
                        }
                    }
                }
                initVersion(isForceReset, this@flow)
                emit(Resource.Success(Triple(0, 0, 0)))
            }
        } catch (e: Exception) {
            emit(Resource.Error("data load fail ${e.message}"))
        }
    }

    override fun updateMoney(): Flow<Resource<UserInfo>> = flow {
        try {
            queries.updateUser(77777)
        } catch (_: Exception) {
        }
        val userInfo = queries.getUserInfo().executeAsOne()
        val result = Resource.Success(userInfo.toUser())
        emit(result)
    }

    private suspend fun initQuiz(
        csvStr: String,
        totalSize: Int,
        flowCollector: FlowCollector<Resource<Triple<Int, Int, Int>>>,
        isResult: (Boolean) -> Unit
    ) {
        var result = true
        try {
            QuizDto.parseJson(csvStr).forEachIndexed { index, quiz ->
                queries.updateQuizContent(
                    answer = quiz.answer.toLong(),
                    category = quiz.category,
                    choiceList = Json.encodeToString(quiz.choiceList),
                    description = quiz.description,
                    imageUrl = quiz.imageUrl,
                    level = quiz.level.toLong(),
                    time = quiz.time,
                    question = quiz.question,
                    chance = quiz.chance.toLong(),
                    reward = quiz.reward.toLong(),
                    id = quiz.id.toLong()
                )
                flowCollector.emit(
                    Resource.Loading(
                        Triple(
                            CARDTYPE_DB_TYPE, totalSize, index + 1
                        )
                    )
                )
            }
        } catch (e: Exception) {
            result = false
        }
        isResult(result)
    }

    private suspend fun initVersion(
        isReset: Boolean, flowCollector: FlowCollector<Resource<Triple<Int, Int, Int>>>
    ) {
        try {
            DBVersion.DB_CARD_FILE_LIST.forEachIndexed { index, path ->
                var downloadUrl: String? = null
                try {
                    val versionTemp =
                        Firebase.storage.reference.child(path).child(DBVersion.VERSION)
                    downloadUrl = withTimeout(5000) { // 10초 타임아웃
                        versionTemp.getDownloadUrl()
                    }
                } catch (_: Exception) {
                }
                val csvTemp = httpClient.get(downloadUrl!!).body<String>().split(",")
                val version = csvTemp[0]
                val totalSize = csvTemp[1].toInt()
                for (i in 0..version.toInt()) {
                    val versionList = queries.getVersion(index.toLong(), i.toLong()).executeAsList()
                    if (versionList.isEmpty()) {
                        val filePath = "${path}_$i.csv"
                        val url =
                            Firebase.storage.reference.child(path).child(filePath).getDownloadUrl()

                        val csvStr = httpClient.get(url).body<String>()
                        when (index) {
                            CARD_DB_TYPE -> {
                                initCardInfo(isReset, csvStr, totalSize, flowCollector) { result ->
                                    queries.insertDBVersion(
                                        i.toLong(), index.toLong(), result
                                    )
                                }
                            }

                            CARDTYPE_DB_TYPE -> {
                                initCardType(csvStr, totalSize, flowCollector) { result ->
                                    if (versionList.isEmpty()) {
                                        queries.insertDBVersion(
                                            i.toLong(), index.toLong(), result
                                        )
                                    }
                                }
                            }

                            CARDCOMBINE_DB_TYPE -> {
                                initCardCombination(
                                    csvStr, totalSize, flowCollector
                                ) { result: Boolean ->
                                    queries.insertDBVersion(
                                        i.toLong(), index.toLong(), result
                                    )
                                }
                            }

                            QUIZ_DB_TYPE -> {
                                initQuiz(csvStr, totalSize, flowCollector) {
                                    queries.insertDBVersion(
                                        i.toLong(), index.toLong(), it
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } catch (_: Exception) {
        }
        // CardType Map Init
        queries.getCardTypeList().executeAsList().forEach {
            CardInfoManager.CARD_TYPE_ID_MAP[it.id.toInt()] = it.name
            CardInfoManager.CARD_TYPE_MAP[it.name] = CardType(
                it.id.toInt(), it.name, CardTypeDto.parseStrongList(it.strongList), hashMapOf()
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