package com.jhlee.kmm_rongame.pandora.presentation

import com.jhlee.kmm_rongame.card.domain.Card

data class PandoraListState(
    val isLoading: Boolean = false,
    val pandoraScreen: Int = 0,
    val stageList: List<Card> = emptyList(),
    val currentStage: Int = 0
) {
    companion object {
        const val SCREEN_LIST = 0
        const val SCREEN_GAME = 1
    }
}