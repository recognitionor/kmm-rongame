package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardCombination
import com.jhlee.kmm_rongame.card.domain.CardCombinationDataSource
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.supervisorScope

class DBCardCombinationDataSource(db: AppDatabase) : CardCombinationDataSource {

    private val queries = db.dbQueries
    override fun getCardList(): Flow<Resource<List<Card>>> = flow {
        emit(Resource.Loading())
        supervisorScope {
            val cardList = async {
                queries.myCardList().executeAsList().map {
                    val cardInfo = queries.getCardInfo(it.cardId!!.toLong()).executeAsOne()
                    it.toCard(cardInfo)
                }
            }.await()
            emit(Resource.Success(cardList))
        }
    }

    override fun getCardCombinationList(): Flow<Resource<List<CardCombinationInfo>>> = flow {
        emit(Resource.Loading())
        val resultList = mutableListOf<CardCombinationInfo>()
        supervisorScope {
            queries.getCardPadigreeList().executeAsList().forEach { item ->
                val resultItem = queries.getCardInfo(item.cardId).executeAsOne()
                val stuffItem1 = queries.getCardInfo(item.itemId1).executeAsOne()
                val stuffItem2 = queries.getCardInfo(item.itemId2).executeAsOne()
                val cardCombinationInfo = CardCombinationInfo(
                    resultItem.toCard(),
                    listOf(stuffItem1, stuffItem2).map { it.toCard() },
                    item.percent?.toFloat() ?: 0f,
                    item.count > 0
                )
                resultList.add(cardCombinationInfo)
            }
            emit(Resource.Success(resultList))
        }
    }

    override fun combinationCard(list: List<Card?>): Flow<Resource<Card>> = flow {
        emit(Resource.Loading())
        supervisorScope {
            try {
                val card1 = list[0]!!
                val card2 = list[1]!!
                var combineResult: List<CardCombination> = emptyList()
                val cardInfoList = queries.getCardInfoList().executeAsList()
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
                        val upgradeCard: Card
                        val removeCard: Card
                        if (isCard1CanUpgrade) {
                            removeCard = card2
                            upgradeCard = card1
                        } else {
                            removeCard = card1
                            upgradeCard = card2
                        }
                        if (upgradeCard.potential <= upgradeCard.upgrade) {
                            emit(Resource.Error("더 이상 업그레이드 할 수 없어요"))
                            return@supervisorScope
                        }
                        queries.removeCard(removeCard.id.toLong())
                        queries.upgradeCard(
                            (upgradeCard.power + removeCard.power).toLong(),
                            upgradeCard.id.toLong(),
                        )

                        val resultCard =
                            queries.getMyCard(upgradeCard.id.toLong()).executeAsOne().toCard()
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
                    queries.removeCard(card1.id.toLong())
                    queries.removeCard(card2.id.toLong())
                    queries.insertCardEntity(
                        cardId.toLong(), potential.toLong(), 0, power.toLong()
                    )
                    queries.addCountCardPadigreeEntity(cardId.toLong())
                    emit(Resource.Success(card.copy(power = power)))
                    return@supervisorScope
                }
            } catch (e: Exception) {
                emit(Resource.Error("조합에 문제가 있습니다."))
                return@supervisorScope
            }
        }
    }

    override fun openCombine(cardId: Int): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        supervisorScope {
            try {
                queries.addCountCardPadigreeEntity(cardId.toLong())
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "error"))
            }
        }
        emit(Resource.Success(Unit))
    }

}