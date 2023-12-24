package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardCombinationDataSource
import com.jhlee.kmm_rongame.constants.CardConst
import com.jhlee.kmm_rongame.core.domain.Resource
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
                val isCard1CanUpgrade = CardUtils.isUpgradeCard(card1)

                if (isCard1CanUpgrade != CardUtils.isUpgradeCard(card2)) {
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
                    if (upgradeCard.upgrade > 0) {
                        var isSameType = false
                        upgradeCard.type.forEach {
                            if (removeCard.type.contains(it)) {
                                isSameType = true
                                return@forEach
                            }
                        }
                        if (!isSameType) {
                            emit(Resource.Error("다른 속성의 카드와 결합하세요"))
                            return@supervisorScope
                        }
                    }

                    queries.removeCard(removeCard.id.toLong())
                    upgradeCard.type.addAll(removeCard.type)
                    var newName = ""
                    var newNameEng = ""
                    if (upgradeCard.upgrade > 0) {
                        newName = upgradeCard.name
                        newNameEng = upgradeCard.nameEng
                    } else {
                        newName = "${removeCard.name} ${upgradeCard.name}"
                        newNameEng = "${removeCard.nameEng} ${upgradeCard.nameEng}"
                    }
                    queries.upgradeCard(
                        (upgradeCard.power + removeCard.power).toLong(),
                        upgradeCard.id.toLong(),
                    )

                    val resultCard =
                        queries.getMyCard(upgradeCard.id.toLong()).executeAsOne().toCard()
                    emit(Resource.Success(resultCard))
                    return@supervisorScope
                } else {
                    val tempCardList = CardUtils.getEnhanceCard(card1, card2)
                    if (tempCardList.isNotEmpty()) {
                        val tempCardId = CardUtils.selectRandomCard(tempCardList)
                        val card = CardConst.COMBINE_CARD_LIST.find {
                            it.cardId == tempCardId
                        }!!

                    } else {
                        emit(Resource.Error("조합이 되는 카드가 아닙니다."))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error("조합에 문제가 있습니다."))
            }
        }
    }

}