package com.jhlee.kmm_rongame.main.presentation

import androidx.compose.runtime.Composable
import com.jhlee.kmm_rongame.main.domain.UserInfo

data class MainState(


    val openDialog: Int = 0,
    val dialog: (@Composable () -> Unit)? = null,
    val isWholeScreenOpen: Boolean = false,
    val userInfo: UserInfo? = null,
    val isLoading: Boolean = false,
    val error: String = ""
) {
    companion object {
        const val NO_DIALOG = 0
        const val QUIZ_INFO_DIALOG = 1
    }
}