package com.jhlee.kmm_rongame.reward.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.attend.data.toAttend
import com.jhlee.kmm_rongame.attend.domain.Attend
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.main.data.toUser
import com.jhlee.kmm_rongame.main.domain.MainDataSource
import com.jhlee.kmm_rongame.main.domain.UserInfo
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class DBRewardDataSource(
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
        queries.updateUserMoney(userInfo.money.toLong())
        val updatedUser = queries.getUserInfo().executeAsOne().toUser()
        emit(Resource.Success(updatedUser))
    }


}