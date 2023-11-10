package com.jhlee.kmm_rongame.card.domain

data class Card(
    var id: Int = -1,
    val name: String,
    val nameEng: String,
    val grade: Int,
    val image: String,
    val description: String,
    val type: String,
    val power: Int = 0,
    val potential: Int = 0,
    val upgrade: Int = 0
)

