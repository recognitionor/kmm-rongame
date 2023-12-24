package com.jhlee.kmm_rongame.cardgame.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.card.data.CardUtils
import com.jhlee.kmm_rongame.card.data.parseHashMap
import com.jhlee.kmm_rongame.card.data.toCard
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardType
import com.jhlee.kmm_rongame.cardgame.domain.CardGameDataSource
import com.jhlee.kmm_rongame.constants.CardConst
import com.jhlee.kmm_rongame.core.data.ImageStorage
import com.jhlee.kmm_rongame.core.domain.Resource
import database.CardTypeEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.Clock
import kotlin.random.Random

class DBCardGameDataSource(db: AppDatabase) : CardGameDataSource {

    private val queries = db.dbQueries
    override fun getEnemyList(selectIndex: Int): Flow<Resource<List<Card>>> = flow {
        try {
            var untilIndex = 2
            val resultList = arrayListOf<Card>()
            when (selectIndex) {
                in 0..5 -> {
                    untilIndex = 3
                }

                in 6..10 -> {
                    untilIndex = 6
                }
            }
            repeat(6) {
                val currentTimeMillis = Clock.System.now().toEpochMilliseconds()
                val cardInfoTemp = queries.getCardInfoRandom(1).executeAsOne()
                val list: MutableList<CardTypeEntity> = mutableListOf()
                cardInfoTemp.type.split("|").forEach { typeId ->
                    list.add(queries.getCardType(typeId.toLong()).executeAsOne())
                }
                val cardTypeSet: HashSet<CardType> = hashSetOf()
                list.forEach { type ->
                    cardTypeSet.add(
                        CardType(
                            type.id.toInt(), type.name, parseHashMap(type.strongList)
                        )
                    )
                }
                val card = Card(
                    cardId = cardInfoTemp.id.toInt(),
                    name = cardInfoTemp.name,
                    nameEng = cardInfoTemp.nameEng,
                    description = cardInfoTemp.description,
                    image = ImageStorage.getImage(cardInfoTemp.image ?: ""),
                    grade = cardInfoTemp.grade?.toInt() ?: 0,
                    type = cardTypeSet,
                )

                val ability = selectIndex + 1
                resultList.add(
                    card.copy(
                        id = it, power = ability, potential = ability, upgrade = ability
                    )
                )
                delay(1)
            }
            emit(Resource.Success(resultList))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "getEnemyList"))
        }
    }

    override fun getMyCardList(): Flow<Resource<List<Card>>> = flow {
        emit(Resource.Loading())
        supervisorScope {
            val cardList = async {
                queries.myCardList().executeAsList().map {
                    val cardInfo = queries.getCardInfo(it.cardId!!.toLong()).executeAsOne()
                    val list: MutableList<CardTypeEntity> = mutableListOf()
                    cardInfo.type.split("|").forEach { typeId ->
                        list.add(queries.getCardType(typeId.toLong()).executeAsOne())
                    }
                    it.toCard(cardInfo, list)
                }
            }.await()
            emit(Resource.Success(cardList))
        }
    }
}