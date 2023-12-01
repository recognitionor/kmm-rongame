package com.jhlee.kmm_rongame.main.domain

import com.jhlee.kmm_rongame.core.domain.Resource
import kotlinx.coroutines.flow.Flow

interface MainDataSource {

    fun getFlaticonToken(): Flow<Resource<String>>
    fun getUserInfo(): Flow<Resource<UserInfo>>
    fun insertUserInfo(userInfo: UserInfo): Flow<Resource<UserInfo>>
    fun updateUserInfo(userInfo: UserInfo): Flow<Resource<UserInfo>>

}