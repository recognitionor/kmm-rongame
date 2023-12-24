package com.jhlee.kmm_rongame.card.domain

import kotlinx.serialization.Serializable

@Serializable
data class CardType(
    var id: Int,
    var name: String,
    var strongList: HashMap<Int, Int>
)