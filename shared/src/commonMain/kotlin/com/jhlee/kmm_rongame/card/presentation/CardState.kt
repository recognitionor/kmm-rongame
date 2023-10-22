package com.jhlee.kmm_rongame.card.presentation

import com.jhlee.kmm_rongame.card.domain.Card

data class CardState(
    val isLoading: Boolean = false,
    val isLoadDone: Boolean = false,
    val isShowCardInfoDialog: Boolean = false,
    val updateUserInfo: Boolean = false,
    val gatchaCard: Card? = null,
    val detailCardInfo: Card? = null,
    val cardList: List<Card> = emptyList(),
    val error: String = "",
    val cardRandomProgress: Int = 0
)
