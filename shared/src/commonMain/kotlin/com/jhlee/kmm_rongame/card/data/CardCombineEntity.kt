package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.card.domain.CardCombination
import database.CardCombineEntity

fun CardCombineEntity.toCombineResult(): List<CardCombination> {
    val tempList = mutableListOf<CardCombination>()
    result.replace("\"", "").replace("\\", "").split(",").map { combination ->
        val (cardIdStr, cardPercentStr) = combination.split(":")
        val cardId = cardIdStr.toInt()
        val cardPercent = cardPercentStr.toFloat()
        tempList.add(CardCombination(cardId, cardPercent))
    }
    tempList.sortedByDescending { it.cardPercent }

    return tempList
}