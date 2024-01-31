package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardCombination
import com.jhlee.kmm_rongame.constants.CardConst
import com.jhlee.kmm_rongame.core.util.Logger
import kotlinx.datetime.Clock
import kotlin.random.Random

class CardUtils {

    companion object {


        fun isUpgradeCard(card: Card): Boolean {
            var result = false
            if (card.cardId != CardConst.BAD && card.cardId != CardConst.GOOD && card.cardId != CardConst.WEIRD) {
                result = true
            }
            return result
        }

        fun selectRandomCard(cardCombinations: List<CardCombination>): Int {
            // 퍼센트 기준으로 내림차순 정렬
            val sortedCardCombinations = cardCombinations.sortedByDescending { it.cardPercent }

            // 전체 퍼센트 합 계산
            val totalPercent = sortedCardCombinations.sumOf { it.cardPercent.toDouble() }

            // 0부터 전체 퍼센트 합 사이의 랜덤 값을 생성
            val randomValue = Random.nextDouble(0.0, totalPercent)

            // 랜덤 값에 해당하는 카드 선택
            var currentTotal = 0.0
            for (cardCombination in sortedCardCombinations) {
                currentTotal += cardCombination.cardPercent.toDouble()
                if (randomValue < currentTotal) {
                    return cardCombination.cardId
                }
            }

            // 만약 선택된 카드가 없는 경우, 마지막 카드를 반환
            return sortedCardCombinations.last().cardId
        }


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
            return Random(Clock.System.now().nanosecondsOfSecond).nextInt(cardGrade, offset)
        }

        fun getCardRandomPotential(): Int {
            return when (Random(Clock.System.now().nanosecondsOfSecond).nextInt(1, 100)) {
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