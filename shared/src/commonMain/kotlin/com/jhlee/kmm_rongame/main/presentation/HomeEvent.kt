package com.jhlee.kmm_rongame.main.presentation

sealed interface HomeEvent {
    data class OnRegisterUserInfo(val name: String) : HomeEvent
}