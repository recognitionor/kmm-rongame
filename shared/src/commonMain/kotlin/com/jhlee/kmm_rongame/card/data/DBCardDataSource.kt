package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardDataSource
import com.jhlee.kmm_rongame.constants.CardConst
import com.jhlee.kmm_rongame.constants.GatchaConst
import com.jhlee.kmm_rongame.constants.RuleConst
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.Clock
import kotlin.random.Random

class DBCardDataSource(db: AppDatabase) : CardDataSource {

    private val queries = db.dbQueries
    override fun getCardList(): Flow<Resource<List<Card>>> {
        return flow {
            emit(Resource.Loading())
            supervisorScope {
                val cardList = async {
                    queries.getCardList().executeAsList().map { it.toCard() }
                }.await()
                emit(Resource.Success(cardList))
            }
        }
    }

    override fun gatchaCard(): Flow<Resource<Card>> {
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
                val random = Random(Clock.System.now().epochSeconds) // 시드값을 현재 시간으로 설정
                val randomNumber = random.nextInt(1, CardConst.HERO_LIST.size)

                val hero = CardConst.HERO_LIST[(randomNumber)]
                val cost = when (random.nextInt(1, 101)) {
                    in 1..10 -> 1
                    in 11..50 -> 2
                    in 51..80 -> 3
                    in 81..90 -> 4
                    else -> 5
                }
                val grade = when ((random.nextInt(1, 100))) {
                    in 1..49 -> 0
                    in 50..74 -> 1
                    in 75..89 -> 2
                    in 90..94 -> 3
                    in 95..97 -> 4
                    in 98..99 -> 5
                    else -> 6
                }
                var card = Card(
                    0,
                    name = hero.name,
                    cost = cost,
                    grade = grade,
                    image = hero.image,
                    hero.type,
                    0,
                    0,
                    0,
                    0,
                    0
                )
                card = distributePoints(grade, card)
                Logger.log(card.toString())
                queries.updateUserMoney(RuleConst.GATCHA_COST.toLong())
                queries.insertCardEntity(
                    card.name,
                    card.cost.toLong(),
                    card.grade.toLong(),
                    card.image,
                    card.type,
                    card.attack.toLong(),
                    card.defense.toLong(),
                    card.speed.toLong(),
                    card.hp.toLong(),
                    card.mp.toLong()
                )
                emit(Resource.Success(card))
            } catch (e: IOException) {
                Logger.log(e.toString())
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
        return card.copy(
            attack = pointList[0] ?: 0,
            defense = pointList[1] ?: 0,
            speed = pointList[2] ?: 0,
            hp = pointList[3] ?: 0,
            mp = pointList[4] ?: 0,
        )
    }
}