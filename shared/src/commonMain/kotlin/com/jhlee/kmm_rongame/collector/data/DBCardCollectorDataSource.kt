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
        Logger.log("getCollectorWantedList")
        emit(Resource.Loading())
        supervisorScope {
            try {
                val tempList = queries.getCardCollectList().executeAsList()
                val isDoneList = tempList.filter { it.isDone == false }
                val emptyCount = RuleConst.COLLECTOR_WANTED_COUNT - isDoneList.size
                if (emptyCount > 0) {
                    repeat(emptyCount) {
                        val randomOffset = Clock.System.now().toEpochMilliseconds()
                        val isGrade: Boolean = (randomOffset.toInt() % 2) == 1
                        var randomCard: Card? = null
                        var gradeValue: Int? = null
                        if (isGrade) {
                            gradeValue = Random(randomOffset).nextInt(1, 7)
                        } else {
                            randomCard = queries.getCardInfoRandom(Int.MAX_VALUE.toLong())
                                .executeAsOneOrNull()?.toCard()
                            gradeValue = randomCard?.grade ?: 1
                        }
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
                    if (it.cardId == null) {
                        it.toCardCollectorWantedItem()
                    } else {
                        it.cardId.let { id ->
                            it.toCardCollectorWantedItem()
                                .copy(card = queries.getCardInfo(id).executeAsOne().toCard())
                        }
                    }
                }
                emit(Resource.Success(result))

            } catch (e: Exception) {
                Logger.log("error : $e")
                emit(Resource.Error(e.message ?: "error"))
            }
        }
    }

    override fun updateCollectorWanted(id: Long): Flow<Resource<Unit>> = flow { }
    override fun getSelectList(cardCollectorWantedItem: CardCollectorWantedItem): Flow<Resource<List<Card>>> =
        flow {
            emit(Resource.Loading())
            val result: MutableList<Card> = mutableListOf()
            queries.myCardList().executeAsList().forEach {
                val card = it.toCard(queries.getCardInfo(it.cardId!!.toLong()).executeAsOne())
                var isFilter = false
                if (cardCollectorWantedItem.card != null) {
                    if (card.cardId != cardCollectorWantedItem.card?.cardId) {
                        isFilter = true
                        return@forEach
                    }
                }
                if (cardCollectorWantedItem.grade > card.grade) {
                    isFilter = true
                    return@forEach
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
            if (result.size >= cardCollectorWantedItem.count) {
                emit(Resource.Error("팔수 있는 카드를 갖고 있지 않은것 같은데?"))
            } else {
                emit(Resource.Success(result))
            }

        }
}