package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardCombination
import com.jhlee.kmm_rongame.card.domain.CardCombinationDataSource
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import database.CardTypeEntity
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
                    val list: MutableList<CardTypeEntity> = mutableListOf()
                    cardInfo.type.split("|").forEach { typeId ->
                        list.add(queries.getCardType(typeId.toLong()).executeAsOne())
                    }
                    it.toCard(cardInfo, list)
                }
            }.await()
            emit(Resource.Success(cardList))
        }
    }

    override fun combinationCard(list: List<Card?>): Flow<Resource<Card>> = flow {
        emit(Resource.Loading())
        supervisorScope {
            try {
                val card1 = list[0]!!
                val card2 = list[1]!!
                var combineResult: List<CardCombination> = emptyList()
                queries.getCardCombineList().executeAsList().forEach {
                    if (it.item1?.toInt() == card1.cardId && it.item2?.toInt() == card2.cardId) {
                        combineResult = it.toCombineResult()
                    }
                    if (it.item2?.toInt() == card1.cardId && it.item1?.toInt() == card2.cardId) {
                        combineResult = it.toCombineResult()
                    }
                }
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
                    val cardId = CardUtils.selectRandomCard(combineResult)
                    val cardTemp = queries.getCardInfo(cardId.toLong()).executeAsOne()
                    val power = CardUtils.getCardRandomPower(cardTemp.grade?.toInt() ?: 0)
                    val potential = CardUtils.getCardRandomPotential()
                    val card =
                        Card.getCardFromCardInfo(cardTemp, power = power, potential = potential)
                    queries.removeCard(card1.id.toLong())
                    queries.removeCard(card2.id.toLong())
                    queries.insertCardEntity(
                        cardId.toLong(), potential.toLong(), 0, power.toLong()
                    )
                    emit(Resource.Success(card))
                    return@supervisorScope
                }
            } catch (e: Exception) {
                emit(Resource.Error("조합에 문제가 있습니다."))
                return@supervisorScope
            }
        }
    }

}