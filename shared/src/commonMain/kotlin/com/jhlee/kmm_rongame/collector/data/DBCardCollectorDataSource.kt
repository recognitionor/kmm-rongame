package com.jhlee.kmm_rongame.collector.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.card.data.toCard
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.collector.domain.CardCollectorDataSource
import com.jhlee.kmm_rongame.collector.domain.CardCollectorWantedItem
import com.jhlee.kmm_rongame.constants.RuleConst
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.Clock
import kotlin.random.Random

class DBCardCollectorDataSource(db: AppDatabase) : CardCollectorDataSource {
    val queries = db.dbQueries
    override fun initCollectorWantedItem(): Flow<Resource<Unit>> = flow {

    }

    override fun getCollectorWantedList(): Flow<Resource<List<CardCollectorWantedItem>>> = flow {
        emit(Resource.Loading())
        supervisorScope {
            try {
                val tempList = queries.getCardCollectList().executeAsList()
                val isDoneList = tempList.filter { it.isDone == false }
                val emptyCount = RuleConst.COLLECTOR_WANTED_COUNT - isDoneList.size
                if (emptyCount > 0) {
                    repeat(emptyCount) {
                        val randomOffset = Clock.System.now().toEpochMilliseconds()
                        var randomCard: Card? = null
                        var gradeValue: Int? = null
                        randomCard =
                            queries.getCardInfoRandom(Int.MAX_VALUE.toLong()).executeAsOneOrNull()
                                ?.toCard()
                        gradeValue = randomCard?.grade ?: 1
                        val countRandomValue = Random(randomOffset + 1).nextInt(1, 5)
                        val upgradeRandomValue = Random(randomOffset + 2).nextInt(1, 5)
                        val rewardRandomValue = Random(randomOffset + 3).nextInt(2, 10)
                        val powerRandomValue =
                            Random(randomOffset + 4).nextInt(1, (randomCard?.grade ?: 1) * 5)
                        val reward = countRandomValue * gradeValue * 50 * rewardRandomValue
                        queries.insertCardCollectorEntity(
                            randomCard?.cardId?.toLong(),
                            countRandomValue.toLong(),
                            gradeValue.toLong(),
                            reward.toLong(),
                            powerRandomValue.toLong(),
                            upgradeRandomValue.toLong()
                        )
                    }
                }
                val result = queries.getCardCollectList().executeAsList().map {
                    val card = queries.getCardInfo(it.cardId ?: 0).executeAsOne().toCard()
                    if (it.cardId == null) {
                        it.toCardCollectorWantedItem(card)
                    } else {
                        it.cardId.let { id ->
                            it.toCardCollectorWantedItem(card)
                                .copy(card = queries.getCardInfo(id).executeAsOne().toCard())
                        }
                    }
                }
                emit(Resource.Success(result))

            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "error"))
            }
        }
    }

    override fun updateCollectorWanted(id: Long): Flow<Resource<Unit>> = flow { }
    override fun getSelectList(cardCollectorWantedItem: CardCollectorWantedItem?): Flow<Resource<List<Card>>> =
        flow {
            emit(Resource.Loading())
            val result: MutableList<Card> = mutableListOf()
            if (cardCollectorWantedItem != null) {
                queries.myCardList().executeAsList().forEach {
                    val card = it.toCard(queries.getCardInfo(it.cardId!!.toLong()).executeAsOne())

                    var isFilter = false
                    if (cardCollectorWantedItem.card != null) {
                        if (card.cardId != cardCollectorWantedItem.card?.cardId) {
                            isFilter = true
                            return@forEach
                        }
                    }

                    if (cardCollectorWantedItem.upgrade > card.upgrade) {
                        isFilter = true
                        return@forEach
                    }
                    if (cardCollectorWantedItem.power > card.power) {
                        isFilter = true
                        return@forEach
                    }
                    if (!isFilter) {
                        result.add(card)
                    }
                }
            } else {
                result.addAll(queries.myCardList().executeAsList().map {
                    it.toCard(queries.getCardInfo(it.cardId!!.toLong()).executeAsOne())
                })
            }

            if (result.size >= (cardCollectorWantedItem?.count ?: 0)) {
                emit(Resource.Success(result))
            } else {
                emit(Resource.Error("팔수 있는 카드를 갖고 있지 않은것 같은데?"))
            }
        }

    override fun sellWantedCard(
        cardList: List<Card>, item: CardCollectorWantedItem
    ): Flow<Resource<List<CardCollectorWantedItem>>> = flow {
        emit(Resource.Loading())
        cardList.forEach {
            queries.removeCard(it.id.toLong())
            queries.plusUserMoney(item.reward.toLong())
        }
        queries.deleteCardCollectorEntity(item.id.toLong())
        emit(Resource.Success(emptyList()))
    }

    override fun wasteCard(cardList: List<Card>): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        cardList.forEach {
            queries.removeCard(it.id.toLong())
            queries.plusUserMoney(RuleConst.COLLECTOR_WASTE_PRICE.toLong())
        }
        emit(Resource.Success(Unit))
    }
}