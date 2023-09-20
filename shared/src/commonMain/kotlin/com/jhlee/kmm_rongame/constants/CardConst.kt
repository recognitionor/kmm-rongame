package com.jhlee.kmm_rongame.constants

import com.jhlee.kmm_rongame.card.domain.Hero
import com.jhlee.kmm_rongame.constants.TypeConst.Companion.ANIMAL
import com.jhlee.kmm_rongame.constants.TypeConst.Companion.ELEMENTAL

class CardConst {
    companion object {
        val HERO_LIST = arrayListOf<Hero>().apply {
            this.add(Hero(0, ELEMENTAL, "불", "ic_fire"))
            this.add(Hero(0, ELEMENTAL, "물", "ic_water_drop"))
            this.add(Hero(0, ELEMENTAL, "풀", "ic_grass"))
            this.add(Hero(0, ELEMENTAL, "철", "ic_iron"))
            this.add(Hero(0, ANIMAL, "벌레", "ic_bug"))

        }
    }

}