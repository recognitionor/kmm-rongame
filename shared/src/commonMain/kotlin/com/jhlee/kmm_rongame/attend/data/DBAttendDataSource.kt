package com.jhlee.kmm_rongame.attend.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.attend.domain.Attend
import com.jhlee.kmm_rongame.attend.domain.AttendDataSource
import com.jhlee.kmm_rongame.core.domain.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DBAttendDataSource(db: AppDatabase) : AttendDataSource {
    private val queries = db.dbQueries
    override fun getAttend(): Flow<Resource<List<Attend>>> = flow {
        emit(Resource.Loading())
        try {
            val list = queries.getAttendList().executeAsList().map {
                it.toAttend()
            }
            emit(Resource.Success(list))
        } catch (e: Exception) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    override fun insertAttend(date: Long): Flow<Resource<Unit>> = flow {
        emit(Resource.Success(queries.insertAttend(date)))
    }
}