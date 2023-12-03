package com.jhlee.kmm_rongame.cardgame.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.card.data.toCard
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.cardgame.domain.CardGameDataSource
import com.jhlee.kmm_rongame.constants.CardConst
import com.jhlee.kmm_rongame.core.domain.Resource
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

                val random = Random(currentTimeMillis).nextInt(0, Int.MAX_VALUE) % untilIndex
                val card = CardConst.BASIC_CARD_LIST[random]
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
                queries.getCardList().executeAsList().map { it.toCard() }
            }.await()
            emit(Resource.Success(cardList))
        }
    }
}