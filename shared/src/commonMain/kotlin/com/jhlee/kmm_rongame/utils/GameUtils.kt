package com.jhlee.kmm_rongame.utils

import com.jhlee.kmm_rongame.card.domain.Card


class GameUtils {

    companion object {
        fun getPower(card: Card): Int {
            return (card.attack + card.defense + card.speed + card.hp + card.mp)
        }
    }
}