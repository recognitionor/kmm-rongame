package com.jhlee.kmm_rongame

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform