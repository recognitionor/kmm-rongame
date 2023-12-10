package com.jhlee.kmm_rongame.card.domain

import kotlinx.serialization.Serializable

@Serializable
data class CardCombination(
    val cardId: Int, val cardPercent: Float
)