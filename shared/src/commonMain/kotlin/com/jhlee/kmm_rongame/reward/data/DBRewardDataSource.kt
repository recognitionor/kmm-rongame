package com.jhlee.kmm_rongame.reward.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.main.data.toUser
import com.jhlee.kmm_rongame.main.domain.MainDataSource
import com.jhlee.kmm_rongame.main.domain.UserInfo
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class DBRewardDataSource(
    db: AppDatabase,
) : MainDataSource {
    private val queries = db.dbQueries
    override fun getFlaticonToken(): Flow<Resource<String>> = flow { }

    override fun getUserInfo(): Flow<Resource<UserInfo>> {
        return flow {
            try {
                emit(Resource.Loading())
                val userInfoList =
                    queries.getUserInfo().asFlow().mapToList(currentCoroutineContext())
                        .firstOrNull()
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
                queries.insertUser(userInfo.name, userInfo.money.toLong(), 0, 0)
                emit(Resource.Success(userInfo))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "error getUserInfo"))
            }
        }
    }

    override fun updateUserInfo(userInfo: UserInfo): Flow<Resource<UserInfo>> = flow {
        emit(Resource.Loading())
        queries.minusUserMoney(userInfo.money.toLong())
        val updatedUser = queries.getUserInfo().executeAsOne().toUser()
        emit(Resource.Success(updatedUser))
    }

    override fun updateCardStage(): Flow<Resource<UserInfo>> = flow {
        emit(Resource.Loading())
        queries.nextCardStage()
        val userInfoList =
            queries.getUserInfo().asFlow().mapToList(currentCoroutineContext()).firstOrNull()
        val userInfo = userInfoList?.firstOrNull()
        if (userInfo != null) {
            emit(Resource.Success(userInfo.toUser()))
        } else {
            emit(Resource.Error("No user found"))
        }
    }

    override fun initCardWholeData(isReset: Boolean): Flow<Resource<Triple<Int, Int, Int>>> =
        flow { }

    override fun updateMoney(): Flow<Resource<UserInfo>> = flow { }
}