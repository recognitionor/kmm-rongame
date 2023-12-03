package com.jhlee.kmm_rongame.reward.data

import com.jhlee.kmm_rongame.main.domain.UserInfo
import database.UserInfoEntity

suspend fun UserInfoEntity.toUser(): UserInfo {
    return UserInfo(
        id = id,
        name = name,
        money = money?.toInt() ?: 0,
        cardStage = cardStage?.toInt() ?: 0
    )
}