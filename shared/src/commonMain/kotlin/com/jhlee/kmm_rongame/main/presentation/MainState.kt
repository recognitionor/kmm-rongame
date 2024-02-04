package com.jhlee.kmm_rongame.main.presentation

import androidx.compose.runtime.Composable
import com.jhlee.kmm_rongame.attend.domain.Attend
import com.jhlee.kmm_rongame.main.domain.UserInfo

data class MainState(
    val openDialog: Int = 0,
    val dialog: (@Composable () -> Unit)? = null,
    val isWholeScreenOpen: Boolean = false,
    val selectedTab: Int = 0,
    val userInfo: UserInfo? = null,
    val isLoading: Boolean = true,
    val progress: String = "",
    val isCardInfoLoading: Boolean = true,
    val error: String = "",
) {
    companion object {
        const val NO_DIALOG = 0
        const val QUIZ_INFO_DIALOG = 1
        const val NOT_ENOUGH_MONEY_DIALOG = 2
        const val BANK_VIEW_MODE_DEPOSIT_DIALOG = 3
        const val BANK_VIEW_MODE_WITHDRAW_DIALOG = 4
        const val CARD_START_DIALOG = 5
        const val CARD_NOT_START_DIALOG = 6
        const val INFO_DIALOG = 7

        const val NAVIGATION_TAB_HOME = 0
        const val NAVIGATION_TAB_GAME = 1
        const val NAVIGATION_TAB_BOOK = 2
        const val NAVIGATION_TAB_REWARD = 3

    }
}