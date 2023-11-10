package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.card.domain.Card
import database.CardEntity

fun CardEntity.toCard(): Card {
    return Card(
        id.toInt(),
        name,
        nameEng,
        grade?.toInt() ?: 1,
        image ?: "",
        description,
        type ?: "",
        power?.toInt() ?: 0,
        potential?.toInt() ?: 0,
    )
}