package com.jhlee.kmm_rongame.constants

import com.jhlee.kmm_rongame.card.domain.Hero
import com.jhlee.kmm_rongame.constants.TypeConst.Companion.ANIMAL
import com.jhlee.kmm_rongame.constants.TypeConst.Companion.DRAGON
import com.jhlee.kmm_rongame.constants.TypeConst.Companion.ELEMENTAL

class CardConst {
    companion object {
        val HERO_LIST = arrayListOf<Hero>().apply {
            this.add(Hero(0, DRAGON, "클라우드", "cloud_dragon", ""))
            this.add(
                Hero(
                    1,
                    DRAGON,
                    "팻",
                    "fat_dragon",
                    "팻 드래곤은 뚱뚱한 외모와 귀여운 성격으로 인기가 많아, 종종 사람들의 반려동물로 키워지기도 합니다.\n" + "그들은 먹이를 찾는 데 시간을 보내기보다는 주로 먹이가 있는 장소를 찾아 다니며, 이로써 귀여움을 뽐내곤 합니다."
                )
            )
            this.add(Hero(2, DRAGON, "카인드", "kind_dragon", ""))
            this.add(Hero(3, DRAGON, "파이어", "fire_dragon", ""))
            this.add(Hero(4, DRAGON, "헬로", "hello_dragon", ""))
            this.add(Hero(5, DRAGON, "러브", "love_dragon", ""))
            this.add(Hero(6, DRAGON, "스마트", "smart_dragon", ""))
        }
    }

}