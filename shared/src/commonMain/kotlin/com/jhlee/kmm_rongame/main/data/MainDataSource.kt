package com.jhlee.kmm_rongame.main.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.core.data.HttpConst
import com.jhlee.kmm_rongame.core.data.HttpConst.FLATICON_URL
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.main.domain.FlaticonAuth
import com.jhlee.kmm_rongame.main.domain.MainDataSource
import com.jhlee.kmm_rongame.main.domain.UserInfo
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.url
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock

class MainDataSourceImpl(
    db: AppDatabase, private val httpClient: HttpClient
) : MainDataSource {
    private val queries = db.dbQueries
    override fun getFlaticonToken(): Flow<Resource<String>> = flow {
        emit(Resource.Loading())
        try {
            var token = ""
            var needTokenRefresh = false
            try {
                val commonInfo = queries.getCommonInfo().executeAsOne()
                if (Clock.System.now().epochSeconds >= (commonInfo.expires ?: 0)) {
                    needTokenRefresh = true
                } else {
                    token = queries.getCommonInfo().executeAsOne().token
                }
            } catch (ignored: Exception) {
            }
            if (needTokenRefresh) {
                val auth: FlaticonAuth = httpClient.post {
                    url(FLATICON_URL)
                    parameter(HttpConst.ParamKey.FLATICON_APIKEY, HttpConst.FLATICON_API_KEY)
                }.body()
                queries.insertCommon(auth.data.token, auth.data.expires.toLong())
                token = auth.data.token
            }
            emit(Resource.Success(token))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

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