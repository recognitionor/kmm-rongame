package com.jhlee.kmm_rongame.main.domain

import kotlinx.serialization.Serializable

@Serializable
data class FlaticonAuth(
    val `data`: Data
) {
    @Serializable
    data class Data(
        val expires: Int, val token: String
    )
}