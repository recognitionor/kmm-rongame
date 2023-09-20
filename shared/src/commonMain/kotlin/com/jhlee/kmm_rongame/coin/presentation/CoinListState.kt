package com.jhlee.kmm_rongame.coin.presentation

import com.jhlee.kmm_rongame.coin.domain.Coin

data class CoinListState(
    val isLoading: Boolean = false,
    val coins: List<Coin> = emptyList(),
    val error: String = ""
)
