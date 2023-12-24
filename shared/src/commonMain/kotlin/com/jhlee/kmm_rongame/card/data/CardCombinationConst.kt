package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.card.domain.CardCombination
import com.jhlee.kmm_rongame.constants.CardConst

object CardCombinationConst {
    val COMBINE_LIST by lazy {
        HashMap<Pair<Int, Int>, List<CardCombination>>().apply {
            this[Pair(CardConst.FIRE, CardConst.WATER)] =
                arrayListOf(
                    CardCombination(CardConst.WATER, 100f),
                    CardCombination(CardConst.FIRE, 900f)
                )
            this[Pair(CardConst.FIRE, CardConst.GROUND)] = arrayListOf(CardCombination(1, 1000f))
        }
    }
}