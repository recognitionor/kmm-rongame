package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.card.domain.CardCombination
import migrations.CardCombineEntity
import migrations.CardInfoEntity

fun CardCombineEntity.toCombineResult(list: List<CardInfoEntity>): List<CardCombination> {
    val tempList = mutableListOf<CardCombination>()
//    result.replace("\"", "").replace("\\", "").split(",").map { combination ->
//        val (cardIdStr, cardPercentStr) = combination.split(":")
//        val cardPercent = cardPercentStr.toFloat()
//        val cardInfo = list.find { it.name == cardIdStr }
//        if (cardInfo != null) {
//            tempList.add(CardCombination(cardInfo.id.toInt(), cardPercent))
//        }
//    }
    tempList.sortedByDescending { it.cardPercent }

    return tempList
}