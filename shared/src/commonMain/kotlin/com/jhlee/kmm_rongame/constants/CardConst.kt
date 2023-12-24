package com.jhlee.kmm_rongame.constants

import com.jhlee.kmm_rongame.card.data.CardTypeConst
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardType

class CardConst {
    companion object {
        val FIRE = 0

        val WATER = 1

        val GROUND = 2

        val GOOD = 3

        val BAD = 4

        val WEIRD = 5

        val COMBINE_CARD_LIST by lazy {
            arrayListOf<Card>().apply {
//                this.add(
//                    Card(-1,
//                        FIRE,
//                        "불",
//                        "Fire",
//                        1,
//                        "img_fire",
//                        description = "원시시대에 발견된 이래로 인류를 지구상의 그 어떤 다른 종족들보다도 더 번성하게 만들어주었다",
//                        type = hashSetOf<CardType>().apply {
//                            this.add(CardTypeConst.TYPE_LIST[FIRE])
//                        })
//                )
            }
        }
    }
}