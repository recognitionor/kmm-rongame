package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.card.domain.CardType

object CardTypeConst {
    val FIRE = 0

    val WATER = 1

    val GROUND = 2

    val GOOD = 3

    val BAD = 4

    val WEIRD = 5

    val TYPE_LIST by lazy {
        ArrayList<CardType>().apply {
            this.add(FIRE, CardType(FIRE, "불", hashMapOf(GROUND to 2)))
            this.add(WATER, CardType(WATER, "물", hashMapOf(FIRE to 2)))
            this.add(GROUND, CardType(GROUND, "땅", hashMapOf(WATER to 2)))
            this.add(GOOD, CardType(GOOD, "좋은힘", hashMapOf(BAD to 2)))
            this.add(BAD, CardType(BAD, "나쁜힘", hashMapOf(WEIRD to 2)))
            this.add(WEIRD, CardType(WEIRD, "이상한힘", hashMapOf(GOOD to 2)))
        }
    }
}