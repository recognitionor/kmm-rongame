package com.jhlee.kmm_rongame.pandora.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.card.data.CardInfoManager
import com.jhlee.kmm_rongame.card.data.CardUtils
import com.jhlee.kmm_rongame.card.data.toCard
import com.jhlee.kmm_rongame.card.data.toCombineResult
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardCombination
import com.jhlee.kmm_rongame.card.domain.CardType
import com.jhlee.kmm_rongame.constants.RuleConst
import com.jhlee.kmm_rongame.core.data.ImageStorage
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.pandora.domain.PandoraDataSource
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.Clock
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.random.Random

class DBPandoraDataSource(db: AppDatabase) : PandoraDataSource {

    private val queries = db.dbQueries
    override fun getGoalCard(): Flow<Resource<Card>> = flow {
        emit(Resource.Loading())
        val myStage = queries.getUserInfo().executeAsOne().cardStage ?: 0
        val targetCardId = (myStage / 10).toInt()
        val targetUpgrade = (myStage % 10).toInt()
        val tempCard = queries.getCardInfo(targetCardId.toLong()).executeAsOne().toCard()
            .copy(upgrade = targetUpgrade)
        emit(Resource.Success(tempCard))


    }

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

                    val upgradeValue: Int = if (card1.upgrade == 0 || card2.upgrade == 0) {
                        1
                    } else {
                        card1.upgrade
                    }

                    val resultCard = card2.copy(
                        upgrade = card2.upgrade.plus(upgradeValue),
                        power = card1.power + card2.power
                    )
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

                            val resultCard = upgradeCard.copy(
                                upgrade = upgradeCard.upgrade.plus(1),
                                power = card1.power.plus(card2.power)
                            )
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
                        val card = Card.getCardFromCardInfo(
                            cardTemp, power = powerTemp, potential = potential
                        )
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

    override fun checkGameOver(
        cardList: List<Card?>, index: Int, rowSize: Int, colSize: Int
    ): Flow<Resource<Boolean>> = flow {
        if (cardList.contains(null)) {
            emit(Resource.Success(true))
            return@flow
        }
        try {
            val card1 = cardList[index]!!
            val startIndexRow = index % rowSize
            val startIndexCol = index / rowSize
            var combineResult: List<CardCombination> = emptyList()
            val cardInfoList = queries.getCardInfoList().executeAsList()
            // 왼쪽 인덱스
            val leftIndex = if (startIndexRow == 0) -1 else index - 1

            // 오른쪽 인덱스
            val rightIndex = if (startIndexRow + 1 >= rowSize) -1 else index + 1

            // 위쪽 인덱스
            val topIndex = if (startIndexCol == 0) -1 else index - rowSize

            // 아래쪽 인덱스
            val bottomIndex = if (startIndexCol + 1 >= colSize) -1 else index + rowSize

            val tempList: List<Int> = listOf(leftIndex, rightIndex, topIndex, bottomIndex)
            var isResult = false
            var count = 0
            tempList.forEach {
                count++
                if (it > -1) {
                    val card2 = cardList[it]!!
                    if (card1.cardId == card2.cardId) {
                        if (card2.potential > card2.upgrade) {
                            isResult = true
                            return@forEach
                        }
                    } else {
                        queries.getCardCombineList().executeAsList().forEach { cardEntity ->
                            if (cardEntity.item1?.toInt() == card1.cardId && cardEntity.item2?.toInt() == card2.cardId) {
                                combineResult = cardEntity.toCombineResult(cardInfoList)
                            }
                            if (cardEntity.item2?.toInt() == card1.cardId && cardEntity.item1?.toInt() == card2.cardId) {
                                combineResult = cardEntity.toCombineResult(cardInfoList)
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
                                if (upgradeCard.potential > upgradeCard.upgrade) {
                                    isResult = true
                                    return@forEach
                                }
                            }
                        } else {
                            isResult = true
                            return@forEach
                        }
                    }
                }
            }
            emit(Resource.Success(isResult))
        } catch (e: Exception) {
            Logger.log("error : $e")
            emit(Resource.Success(false))
        }
    }

    override fun checkWin(cardList: List<Card?>): Flow<Resource<Boolean>> = flow {
        Logger.log("checkWin")
        val stageIndex = queries.getUserInfo().executeAsOne().cardStage ?: 0
        val cardIndex = (stageIndex / 10).toInt()
        val upgradeCount = (stageIndex % 10).toInt()
        var isResult = false
        cardList.forEach { card ->
            if (card?.cardId == cardIndex && card.upgrade >= upgradeCount) {
                isResult = true
                return@forEach
            }
        }
        Logger.log("test1 : ${queries.getUserInfo().executeAsOne().cardStage}")

        if (isResult) {
//            queries.nextCardStage()
        }
        Logger.log("test2 : ${queries.getUserInfo().executeAsOne().cardStage}")

        emit(Resource.Success(isResult))
    }

    override fun getStageList(): Flow<Resource<List<Card>>> = flow {
        emit(Resource.Loading())
        try {
            val tempList = queries.getCardInfoList().executeAsList()
            val resultList = tempList.map {
                val tempType = hashSetOf<CardType>()
                it.type.split("|").forEach { typeName ->
                    CardInfoManager.CARD_TYPE_MAP[typeName]?.let { cardType ->
                        tempType.add(cardType)
                    }
                }
                val tempCount = if (it.count.toInt() == 0) {
                    queries.getCardCount(it.id).executeAsOne()
                } else {
                    it.count
                }

                it.toCard().copy(type = tempType, count = tempCount.toInt())
            }
            emit(Resource.Success(resultList))
        } catch (e: Exception) {
            emit(Resource.Error("${e.message}"))
        }
    }

    override fun pickCard(card: Card): Flow<Resource<Boolean>> = flow {
        try {
            queries.addCountCardInfo(card.cardId.toLong())
            queries.insertCardEntity(
                cardId = card.cardId.toLong(),
                power = card.power.toLong(),
                potential = card.potential.toLong(),
                upgrade = card.upgrade.toLong()
            )
            emit(Resource.Success(true))
        } catch (e: Exception) {
            Logger.log("error : ${e.message}")
            emit(Resource.Success(false))
        }
    }
}