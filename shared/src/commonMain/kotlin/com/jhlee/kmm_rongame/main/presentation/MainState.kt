package com.jhlee.kmm_rongame.main.presentation

import com.jhlee.kmm_rongame.main.domain.UserInfo

data class MainState(
    val isWholeScreenOpen: Boolean = false,
    val userInfo: UserInfo? = null,
    val isLoading: Boolean = false,
    val error: String = ""
)