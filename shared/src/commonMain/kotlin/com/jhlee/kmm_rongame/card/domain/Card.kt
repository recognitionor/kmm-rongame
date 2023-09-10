package com.jhlee.kmm_rongame.card.domain

data class Card(
    var id: Int,
    val name: String,
    val cost: Int,
    val grade: Int,
    val image: String,
    val type: String,
    val attack: Int,
    val defense: Int,
    val speed: Int,
    val hp: Int,
    val mp: Int,
)

