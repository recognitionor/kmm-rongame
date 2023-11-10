package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.constants.CardConst
import kotlinx.datetime.Clock
import kotlin.random.Random

class CardUtils {

    companion object {
        fun getCardRandomPower(cardGrade: Int): Int {
            var offset = 1
            when (cardGrade) {
                1 -> offset = 5
                2 -> offset = 7
                3 -> offset = 10
                4 -> offset = 15
                5 -> offset = 20
                6 -> offset = 25
                7 -> offset = 30
            }
            return Random(Clock.System.now().epochSeconds).nextInt(cardGrade, cardGrade * offset)
        }

        fun getCardRandomPotential(): Int {
            return when (Random(Clock.System.now().epochSeconds).nextInt(1, 100)) {
                in 1..10 -> 1
                in 11..30 -> 2
                in 31..40 -> 3
                in 41..60 -> 4
                in 61..70 -> 5
                in 71..80 -> 6
                in 81..90 -> 7
                in 91..96 -> 8
                else -> 9
            }
        }
    }

}