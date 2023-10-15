package com.jhlee.kmm_rongame.main.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.main.domain.MainDataSource
import com.jhlee.kmm_rongame.main.domain.UserInfo
import com.jhlee.kmm_rongame.test.data.toTest
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.supervisorScope

class DBUserInfoDataSource(
    db: AppDatabase
) : MainDataSource {
    private val queries = db.dbQueries
    override fun getUserInfo(): Flow<Resource<UserInfo>> {
        return flow {
            try {
                emit(Resource.Loading())
                val userInfoList = queries.getUserInfo().asFlow().mapToList().firstOrNull()
                val userInfo = userInfoList?.firstOrNull()
                if (userInfo != null) {
                    emit(Resource.Success(userInfo.toUser()))
                } else {
                    emit(Resource.Error("No user found"))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "error getUserInfo"))
            }
        }
    }

    override fun insertUserInfo(userInfo: UserInfo): Flow<Resource<UserInfo>> {
        return flow {
            try {
                emit(Resource.Loading())
                queries.insertUser(userInfo.name, userInfo.money.toLong())
                emit(Resource.Success(userInfo))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "error getUserInfo"))
            }
        }
    }

    override fun updateUserInfo(userInfo: UserInfo): Flow<Resource<UserInfo>> = flow {
        emit(Resource.Loading())
        queries.updateUser(userInfo.money.toLong())
        val userInfoList = queries.getUserInfo().asFlow().mapToList().firstOrNull()
        val userInfo = userInfoList?.firstOrNull()
        if (userInfo != null) {
            emit(Resource.Success(userInfo.toUser()))
        } else {
            emit(Resource.Error("No user found"))
        }
    }
}