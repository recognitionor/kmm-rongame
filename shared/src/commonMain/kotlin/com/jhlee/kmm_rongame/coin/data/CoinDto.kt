package com.jhlee.kmm_rongame.coin.data

import com.jhlee.kmm_rongame.coin.domain.Coin
import kotlinx.serialization.Serializable

@Serializable
data class CoinDto(
    val id: String,
    val is_active: Boolean,
    val is_new: Boolean,
    val name: String,
    val rank: Int,
    val symbol: String,
    val type: String
)

fun CoinDto.toCoin() = Coin(id = id, name = name, rank = rank, symbol = symbol, type = type)