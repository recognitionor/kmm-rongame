package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardDataSource
import com.jhlee.kmm_rongame.card.domain.CardType
import com.jhlee.kmm_rongame.constants.GatchaConst
import com.jhlee.kmm_rongame.constants.RuleConst
import com.jhlee.kmm_rongame.core.data.ImageStorage
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import io.ktor.client.HttpClient
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.supervisorScope
import kotlin.random.Random

class CardDataSourceImpl(db: AppDatabase, private val httpClient: HttpClient) : CardDataSource {

    private val queries = db.dbQueries

    override fun getMyCardList(): Flow<Resource<List<Card>>> = flow {
        Logger.log("getMyCardList :")
        emit(Resource.Loading())
        supervisorScope {
            try {
                val cardList = async {
                    queries.myCardList().executeAsList().map {
                        val cardInfo = queries.getCardInfo(it.cardId!!.toLong()).executeAsOne()

                        CardInfoManager.CARD_TYPE_MAP
//                        cardInfo.type.split("|").forEach { typeId ->
//                            val type = queries.getCardType(typeId.toLong()).executeAsOne()
//                            list.add(type)
//                        }
                        it.toCard(cardInfo)
                    }
                }.await()
                emit(Resource.Success(cardList))
            } catch (e: Exception) {
                Logger.log("error : $e")
                emit(Resource.Error(e.message.toString()))
            }


        }
    }


    override fun gatchaBasicCard(): Flow<Resource<Card>> {
        val cardGrade = 1
        return flow {
            try {
                emit(Resource.Loading())
                var count = GatchaConst.DELAY_TIME
                val offsetTime: Long = 50
                while (count > 0) {
                    emit(Resource.Loading())
                    kotlinx.coroutines.delay(offsetTime)
                    count = count.minus(offsetTime.toInt())
                }
                val cardInfoTemp = queries.getCardInfoRandom(1).executeAsOne()
                val cardTypeSet: HashSet<CardType> = hashSetOf()
                cardInfoTemp.type.split("|").forEach { typeName ->
                    CardInfoManager.CARD_TYPE_MAP[typeName]?.let {
                        cardTypeSet.add(it)
                    }
                }

                val card = Card(
                    cardId = cardInfoTemp.id.toInt(),
                    name = cardInfoTemp.name,
                    nameEng = cardInfoTemp.nameEng,
                    description = cardInfoTemp.description,
                    image = ImageStorage.getImage(cardInfoTemp?.image ?: ""),
                    grade = cardInfoTemp.grade?.toInt() ?: 0,
                    type = cardTypeSet,
                    power = CardUtils.getCardRandomPower(cardGrade),
                    potential = CardUtils.getCardRandomPotential()
                )
                queries.minusUserMoney(RuleConst.GATCHA_COST.toLong())
                queries.insertCardEntity(
                    cardId = card.cardId.toLong(),
                    power = card.power.toLong(),
                    potential = card.potential.toLong(),
                    upgrade = card.upgrade.toLong()
                )
                emit(Resource.Success(card))
            } catch (e: IOException) {
                emit(Resource.Error(e.message as String))
            }
        }
    }


    private fun distributePoints(grade: Int, card: Card): Card {
        var totalPoint = 0
        when (grade) {
            0 -> totalPoint = 10
            1 -> totalPoint = 15
            2 -> totalPoint = 20
            3 -> totalPoint = 30
            4 -> totalPoint = 45
            5 -> totalPoint = 60
            6 -> totalPoint = 80
        }
        var remainPoint = totalPoint
        val pointList = arrayListOf<Int>()
        for (i in 0..4) {
            var random = 0
            random = if (i == 0) {
                Random.Default.nextInt((remainPoint.toFloat() / 1.5).toInt())
            } else {
                Random.Default.nextInt(remainPoint)
            }
            remainPoint = remainPoint.minus(random)
            pointList.add(random)
        }
        pointList.shuffle()
        return card.copy()
    }
}