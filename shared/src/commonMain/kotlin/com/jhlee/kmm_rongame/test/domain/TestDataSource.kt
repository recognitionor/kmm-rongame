package com.jhlee.kmm_rongame.test.domain

import kotlinx.coroutines.flow.Flow

interface TestDataSource {
    fun getTestList(): Flow<List<Test>>
    fun insertTest(test: Test)
}