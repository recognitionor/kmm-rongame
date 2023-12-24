package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.card.domain.CardCombination

object CardCombinationUtils {
    fun gatchaCombine(random: Int, combineResult: List<CardCombination>): CardCombination? {
        for (combination in combineResult) {
            if (random <= combination.cardPercent) {
                return combination
            }
        }
        return null
    }
}