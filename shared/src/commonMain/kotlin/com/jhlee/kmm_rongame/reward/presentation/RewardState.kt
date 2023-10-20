package com.jhlee.kmm_rongame.reward.presentation

data class RewardState(
    val dateList: List<String> = emptyList(),
    val attendedList: List<Long> = emptyList(),
    val rewardScreenSelected: Int = 0,
    val openQuizDialog: Boolean = false
)
