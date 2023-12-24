package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.card.domain.CardType
import com.jhlee.kmm_rongame.constants.CardConst

object CardTypeConst {

    
    val TYPE_LIST by lazy {
        ArrayList<CardType>().apply {
            this.add(
                CardConst.FIRE, CardType(CardConst.FIRE, "불", hashMapOf(CardConst.GROUND to 2))
            )
            this.add(
                CardConst.WATER, CardType(CardConst.WATER, "물", hashMapOf(CardConst.FIRE to 2))
            )
            this.add(
                CardConst.GROUND, CardType(CardConst.GROUND, "땅", hashMapOf(CardConst.WATER to 2))
            )
            this.add(CardConst.GOOD, CardType(CardConst.GOOD, "좋은힘", hashMapOf(CardConst.BAD to 2)))
            this.add(CardConst.BAD, CardType(CardConst.BAD, "나쁜힘", hashMapOf(CardConst.WEIRD to 2)))
            this.add(
                CardConst.WEIRD, CardType(CardConst.WEIRD, "이상한힘", hashMapOf(CardConst.GOOD to 2))
            )
        }
    }
}