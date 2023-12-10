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

        val BASIC_CARD_LIST by lazy {
            arrayListOf<Card>().apply {
                this.add(
                    Card(-1,
                        FIRE,
                        "불",
                        "Fire",
                        1,
                        "img_fire",
                        description = "원시시대에 발견된 이래로 인류를 지구상의 그 어떤 다른 종족들보다도 더 번성하게 만들어주었다",
                        type = hashSetOf<CardType>().apply {
                            this.add(CardTypeConst.TYPE_LIST[FIRE])
                        })
                )
                this.add(
                    Card(-1,
                        WATER,
                        "물",
                        "Water",
                        1,
                        "img_water",
                        description = "물(水, water, \uD83D\uDCA7)은 수소 원자 둘과 산소 원자 하나로 이루어진 화합물(H2O)이다. 지구에 풍부하게 존재하는 물질로 생물의 생명활동에 필수적이다.",
                        type = hashSetOf<CardType>().apply {
                            this.add(CardTypeConst.TYPE_LIST[WATER])
                        })
                )
                this.add(
                    Card(-1,
                        GROUND,
                        "땅",
                        "Ground",
                        1,
                        "img_soil",
                        description = "땅은 커다란 지구의 표면을 이루고 있는 땅바닥이에요. 마치 큰 퍼즐 조각처럼 생각해도 돼. 이 퍼즐 조각들이 모여서 지구의 모양을 만들어 주는 거야. 땅은 산, 강, 바다, 숲, 들 그리고 우리가 걸어다니고 집을 짓고 놀 수 있는 곳을 말해.",
                        type = hashSetOf<CardType>().apply {
                            this.add(CardTypeConst.TYPE_LIST[GROUND])
                        })
                )
                this.add(
                    Card(-1,
                        GOOD,
                        "좋은힘",
                        "Good",
                        1,
                        "img_sun",
                        description = "좋은힘, 긍정적인힘, 착한힘 나쁜힘에게 강하지만 이상한 힘에게는 약하다.",
                        type = hashSetOf<CardType>().apply {
                            this.add(CardTypeConst.TYPE_LIST[GOOD])
                        })
                )
                this.add(
                    Card(-1,
                        BAD,
                        "나쁜힘",
                        "Bad",
                        1,
                        "img_moon",
                        description = "나쁜힘, 부정적인힘, 음침한힘 이상한힘 에게 강하지만 착한 힘에게는 약하다.",
                        type = hashSetOf<CardType>().apply {
                            this.add(CardTypeConst.TYPE_LIST[BAD])
                        })
                )
                this.add(
                    Card(-1,
                        WEIRD,
                        "이상한힘",
                        "Weird",
                        1,
                        "img_star",
                        description = "이상한 힘, 엉뚱한 힘 은 때로는 창의성을 발휘 하기도 한다. 이 힘은 착한 힘에게 강하지만, 나쁜힘에게는 약하다.",
                        type = hashSetOf<CardType>().apply {
                            this.add(CardTypeConst.TYPE_LIST[WEIRD])
                        })
                )
            }
        }
    }
}