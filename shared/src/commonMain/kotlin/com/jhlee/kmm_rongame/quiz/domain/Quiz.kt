package com.jhlee.kmm_rongame.quiz.domain

data class Quiz(
    var id: Int,
    val category: String,
    val level: Int,
    val imageUrl: String,
    val answer: Int,
    val question: String,
    val choiceList: List<String>,
    val time: Long,
    val chance: Int,
    val reward: Int,
    val description: String = "",
    val selected: Int = -1,
    val durationTime: Long = -1
)