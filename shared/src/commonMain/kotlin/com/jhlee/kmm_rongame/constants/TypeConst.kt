package com.jhlee.kmm_rongame.constants

class TypeConst {

    companion object {
        const val ELEMENTAL = "Elemental"
        const val ANIMAL = "Animal"
        const val WEAPON = "Weapon"

        val TYPE_SET: MutableSet<String> = mutableSetOf<String>().apply {
            this.add(ELEMENTAL)
            this.add(ANIMAL)
            this.add(WEAPON)
        }
    }
}