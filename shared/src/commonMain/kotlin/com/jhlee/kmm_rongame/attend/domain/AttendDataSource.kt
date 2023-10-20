package com.jhlee.kmm_rongame.attend.domain

import com.jhlee.kmm_rongame.core.domain.Resource
import kotlinx.coroutines.flow.Flow

interface AttendDataSource {
    fun getAttend(): Flow<Resource<List<Attend>>>

    fun insertAttend(date: Long): Flow<Resource<Unit>>
}