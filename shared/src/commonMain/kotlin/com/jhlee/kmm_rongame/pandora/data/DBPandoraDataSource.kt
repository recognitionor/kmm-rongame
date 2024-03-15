package com.jhlee.kmm_rongame.pandora.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.card.data.CardInfoManager
import com.jhlee.kmm_rongame.card.data.CardUtils
import com.jhlee.kmm_rongame.card.data.toCombineResult
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardCombination
import com.jhlee.kmm_rongame.card.domain.CardType
import com.jhlee.kmm_rongame.core.data.ImageStorage
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.pandora.domain.PandoraDataSource
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.Clock
import kotlin.math.absoluteValue
import kotlin.random.Random

class DBPandoraDataSource(db: AppDatabase) : PandoraDataSource {

    private val queries = db.dbQueries
    override fun combinationCard(list: List<Card?>): Flow<Resource<Card>> = flow {
        emit(Resource.Loading())
        supervisorScope {
            try {
                val card1 = list[0]!!
                val card2 = list[1]!!
                var combineResult: List<CardCombination> = emptyList()
                val cardInfoList = queries.getCardInfoList().executeAsList()
                if (card1.cardId == card2.cardId) {
                    if (card2.potential <= card2.upgrade) {
                        emit(Resource.Error("더 이상 업그레이드 할 수 없어요"))
                        return@supervisorScope
                    }
                    val resultCard = card2.copy(upgrade = card2.upgrade.plus(1))
                    emit(Resource.Success(resultCard))
                    return@supervisorScope
                } else {
                    queries.getCardCombineList().executeAsList().forEach {
                        if (it.item1?.toInt() == card1.cardId && it.item2?.toInt() == card2.cardId) {
                            combineResult = it.toCombineResult(cardInfoList)
                        }
                        if (it.item2?.toInt() == card1.cardId && it.item1?.toInt() == card2.cardId) {
                            combineResult = it.toCombineResult(cardInfoList)
                        }
                    }
                    // 일반 강화 로직
                    if (combineResult.isEmpty()) {
                        val isCard1CanUpgrade = CardUtils.isUpgradeCard(card1)
                        val isCard2CanUpgrade = CardUtils.isUpgradeCard(card2)
                        if (isCard1CanUpgrade != isCard2CanUpgrade) {
                            val upgradeCard: Card = if (isCard1CanUpgrade) {
                                card1
                            } else {
                                card2
                            }
                            if (upgradeCard.potential <= upgradeCard.upgrade) {
                                emit(Resource.Error("더 이상 업그레이드 할 수 없어요"))
                                return@supervisorScope
                            }

                            val resultCard = upgradeCard.copy(upgrade = upgradeCard.upgrade.plus(1))
                            emit(Resource.Success(resultCard))
                            return@supervisorScope
                        } else {
                            emit(Resource.Error("조합에 문제가 있습니다."))
                            return@supervisorScope
                        }
                    } else {
                        // 결합 로직
                        val cardId = CardUtils.selectRandomCard(combineResult)
                        val cardTemp = queries.getCardInfo(cardId.toLong()).executeAsOne()
                        val potential = CardUtils.getCardRandomPotential()

                        val powerTemp = CardUtils.getCardRandomPower(cardTemp.grade?.toInt() ?: 0)
                        val card =
                            Card.getCardFromCardInfo(cardTemp, power = powerTemp, potential = potential)
                        val power = if (card1.grade < card.grade || card2.grade < card.grade) {
                            card1.power + card2.power
                        } else {
                            powerTemp
                        }
                        emit(Resource.Success(card.copy(power = power)))
                        return@supervisorScope
                    }
                }
            } catch (e: Exception) {
                Logger.log("card combine error : ${e.message}")
                emit(Resource.Error("조합에 문제가 있습니다."))
                return@supervisorScope
            }
        }
    }

    override fun gatchaBasicCard(): Flow<Resource<Card>> {
        val cardGrade = 1
        return flow {
            try {
                emit(Resource.Loading())
                var count = 1000
                val offsetTime: Long = 50
                while (count > 0) {
                    emit(Resource.Loading())
                    kotlinx.coroutines.delay(offsetTime)
                    count = count.minus(offsetTime.toInt())
                }
                val random =
                    Random(Clock.System.now().toEpochMilliseconds()).nextInt().absoluteValue % 9
                // 60% 확률로 0~2 그외는 3~5 중에 뽑는다.
                val cardInfoTemp = if (random < 6) {
                    val randomCardId = Random(
                        Clock.System.now().toEpochMilliseconds() * 2
                    ).nextInt().absoluteValue % 3
                    queries.getCardInfo(randomCardId.toLong()).executeAsOne()
                } else {
                    val randomCardId = Random(
                        Clock.System.now().toEpochMilliseconds() * 2
                    ).nextInt().absoluteValue % 3 + 3
                    queries.getCardInfo(randomCardId.toLong()).executeAsOne()
                }

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
                    image = ImageStorage.getImage(cardInfoTemp.image ?: ""),
                    grade = cardInfoTemp.grade?.toInt() ?: 0,
                    type = cardTypeSet,
                    power = CardUtils.getCardRandomPower(cardGrade),
                    potential = CardUtils.getCardRandomPotential()
                )
                emit(Resource.Success(card))
            } catch (e: IOException) {
                Logger.log("e : ${e.message}")
                emit(Resource.Error(e.message as String))
            }
        }
    }
}