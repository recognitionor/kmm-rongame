package com.jhlee.kmm_rongame.card.presentation

import com.jhlee.kmm_rongame.card.domain.Card

data class HomeState(
    val isLoading: Boolean = false,
    val isLoadDone: Boolean = false,
    val isShowCardInfoDialog: Boolean = false,
    val updateUserInfo: Boolean = false,
    val gatchaCard: Card? = null,
    val detailCardInfo: Card? = null,
    val cardList: List<Card> = emptyList(),
    val error: String = "",
    val cardRandomProgress: Int = 0,
    val homeScreenMode: Int = 0
) {
    companion object {
        const val HOME_SCREEN_DEFAULT = 0
        const val HOME_SCREEN_COMBINATION = 1
        const val HOME_SCREEN_SETTING = 2

    }
}
