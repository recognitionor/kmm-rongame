package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardType
import database.CardEntity
import kotlinx.serialization.json.Json.Default.decodeFromString

fun CardEntity.toCard(): Card {
    val tempSet = decodeFromString(type) as HashSet<CardType>
    return Card(
        id.toInt(),
        name,
        nameEng,
        grade?.toInt() ?: 1,
        image ?: "",
        description,
        tempSet,
        power?.toInt() ?: 0,
        potential?.toInt() ?: 0,
    )
}