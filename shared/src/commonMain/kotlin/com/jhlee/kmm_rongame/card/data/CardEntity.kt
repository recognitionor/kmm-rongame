package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.card.domain.Card
import database.CardEntity

fun CardEntity.toCard(): Card {
    return Card(
        id.toInt(),
        name,
        cost?.toInt() ?: 0,
        grade?.toInt() ?: 0,
        image ?: "",
        type,
        attack?.toInt() ?: 0,
        defense?.toInt() ?: 0,
        speed?.toInt() ?: 0,
        hp?.toInt() ?: 0,
        mp?.toInt() ?: 0
    )
}