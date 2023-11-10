package com.jhlee.kmm_rongame.constants

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.Hero
import com.jhlee.kmm_rongame.constants.TypeConst.Companion.ANIMAL
import com.jhlee.kmm_rongame.constants.TypeConst.Companion.DRAGON
import com.jhlee.kmm_rongame.constants.TypeConst.Companion.ELEMENTAL

class CardConst {
    companion object {
        val BASIC_CARD_LIST = arrayListOf<Card>().apply {
            this.add(
                Card(
                    -1,
                    "불",
                    "Fire",
                    1,
                    "img_fire",
                    description = "원시시대에 발견된 이래로 인류를 지구상의 그 어떤 다른 종족들보다도 더 번성하게 만들어주었다",
                    type = ""
                )
            )
            this.add(
                Card(
                    -1,
                    "물",
                    "Water",
                    1,
                    "img_water",
                    description = "물(水, water, \uD83D\uDCA7)은 수소 원자 둘과 산소 원자 하나로 이루어진 화합물(H2O)이다. 지구에 풍부하게 존재하는 물질로 생물의 생명활동에 필수적이다.",
                    type = ""
                )
            )
            this.add(
                Card(
                    -1,
                    "땅",
                    "Ground",
                    1,
                    "img_soil",
                    description = "땅은 커다란 지구의 표면을 이루고 있는 땅바닥이에요. 마치 큰 퍼즐 조각처럼 생각해도 돼. 이 퍼즐 조각들이 모여서 지구의 모양을 만들어 주는 거야. 땅은 산, 강, 바다, 숲, 들 그리고 우리가 걸어다니고 집을 짓고 놀 수 있는 곳을 말해.",
                    type = ""
                )
            )
            this.add(
                Card(
                    -1,
                    "좋은힘",
                    "Good",
                    1,
                    "img_sun",
                    description = "좋은힘, 긍정적인힘, 착한힘 나쁜힘에게 강하지만 이상한 힘에게는 약하다.",
                    type = ""
                )
            )
            this.add(
                Card(
                    -1,
                    "나쁜힘",
                    "Bad",
                    1,
                    "img_moon",
                    description = "",
                    type = "나쁜힘, 부정적인힘, 음침한힘 이상한힘 에게 강하지만 착한 힘에게는 약하다."
                )
            )
            this.add(
                Card(
                    -1,
                    "이상한힘",
                    "Weird",
                    1,
                    "img_star",
                    description = "이상한 힘, 엉뚱한 힘 은 때로는 창의성을 발휘 하기도 한다. 이 힘은 착한 힘에게 강하지만, 나쁜힘에게는 약하다.",
                    type = ""
                )
            )
        }

        val HERO_LIST = arrayListOf<Hero>().apply {
            this.add(Hero(0, DRAGON, "클라우드", "img_cloud_dragon", ""))
            this.add(
                Hero(
                    1,
                    DRAGON,
                    "팻",
                    "img_fat_dragon",
                    "팻 드래곤은 뚱뚱한 외모와 귀여운 성격으로 인기가 많아, 종종 사람들의 반려동물로 키워지기도 합니다.\n" + "그들은 먹이를 찾는 데 시간을 보내기보다는 주로 먹이가 있는 장소를 찾아 다니며, 이로써 귀여움을 뽐내곤 합니다."
                )
            )
            this.add(Hero(2, DRAGON, "카인드", "img_kind_dragon", ""))
            this.add(Hero(3, DRAGON, "파이어", "img_fire_dragon", ""))
            this.add(Hero(4, DRAGON, "헬로", "img_hello_dragon", ""))
            this.add(Hero(5, DRAGON, "러브", "img_love_dragon", ""))
            this.add(Hero(6, DRAGON, "스마트", "img_smart_dragon", ""))
        }
    }

}