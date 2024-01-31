package com.jhlee.kmm_rongame.collector.domain

import com.jhlee.kmm_rongame.card.domain.Card

data class CardCollectorWantedItem(
    var card: Card? = null,
    val isDone: Boolean,
    val reward: Int,
    val grade: Int,
    val upgrade: Int,
    val power: Int,
    val count: Int,
)