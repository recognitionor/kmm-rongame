package com.jhlee.kmm_rongame.cardgame.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.card.data.CardInfoManager
import com.jhlee.kmm_rongame.card.data.toCard
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardType
import com.jhlee.kmm_rongame.cardgame.domain.CardGameDataSource
import com.jhlee.kmm_rongame.core.data.ImageStorage
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.supervisorScope
import migrations.CardInfoEntity

class DBCardGameDataSource(db: AppDatabase) : CardGameDataSource {

    private val queries = db.dbQueries
    override fun getEnemyList(selectIndex: Int): Flow<Resource<List<Card>>> = flow {
        try {
            val resultList = arrayListOf<Card>()

            repeat(6) {
                val cardInfoTemp = if (it < 3) {
                    var cardTemp: CardInfoEntity? = null
                    try {
                        cardTemp = queries.getCardInfo(selectIndex.toLong() + it).executeAsOne()
                    } catch (_: Exception) {
                    }
                    if (cardTemp == null) {
                        val gradeTemp = (selectIndex / 16) + 1
                        cardTemp = queries.getCardInfoRandom(gradeTemp.toLong()).executeAsOne()
                    }
                    cardTemp
                } else {
                    val gradeTemp = (selectIndex / 16) + 1
                    queries.getCardInfoRandom(gradeTemp.toLong()).executeAsOne()
                }


                val cardTypeSet: HashSet<CardType> = hashSetOf()
                cardInfoTemp.type.split("|").forEach { typeName ->
                    CardInfoManager.CARD_TYPE_MAP[typeName]?.let { cardType ->
                        cardTypeSet.add(cardType)
                    }
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
                        id = it, power = ability, potential = 10, upgrade = 10
                    )
                )
                delay(1)
            }
            emit(Resource.Success(resultList))
        } catch (e: Exception) {
            Logger.log("enemy $e")
            emit(Resource.Error(e.message ?: "getEnemyList"))
        }
    }

    override fun getMyCardList(): Flow<Resource<List<Card>>> = flow {
        emit(Resource.Loading())
        supervisorScope {
            val cardList = async {
                queries.myCardList().executeAsList().map {
                    val cardInfo = queries.getCardInfo(it.cardId!!.toLong()).executeAsOne()
                    it.toCard(cardInfo)
                }
            }.await()
            Logger.log("Success")
            emit(Resource.Success(cardList))
        }
    }
}