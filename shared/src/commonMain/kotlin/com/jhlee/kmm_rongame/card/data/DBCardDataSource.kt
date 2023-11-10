package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardDataSource
import com.jhlee.kmm_rongame.constants.CardConst
import com.jhlee.kmm_rongame.constants.GatchaConst
import com.jhlee.kmm_rongame.constants.RuleConst
import com.jhlee.kmm_rongame.core.domain.Resource
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
                val random = Random(Clock.System.now().epochSeconds) // 시드값을 현재 시간으로 설정
                val randomNumber = random.nextInt(0, CardConst.BASIC_CARD_LIST.size)

                val cardTemp = CardConst.BASIC_CARD_LIST[(randomNumber)]

                val card = cardTemp.copy(
                    power = CardUtils.getCardRandomPower(cardGrade),
                    potential = CardUtils.getCardRandomPotential()
                )
                queries.minusUserMoney(RuleConst.GATCHA_COST.toLong())
                queries.insertCardEntity(
                    name = card.name,
                    nameEng = card.nameEng,
                    grade = card.grade.toLong(),
                    image = card.image,
                    description = card.description,
                    type = card.type,
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