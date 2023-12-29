package com.jhlee.kmm_rongame.test.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.test.domain.Test
import com.jhlee.kmm_rongame.test.domain.TestDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope

class DBTestDataSource(
    db: AppDatabase,
) : TestDataSource {
    private val queries = db.dbQueries

    override fun getTestList(): Flow<List<Test>> = flow {
        queries.getTestList().asFlow().mapToList(currentCoroutineContext()).map { testList ->
            supervisorScope {
                testList.map {
                    async { it.toTest() }
                }.map { it.await() }
            }
        }
    }

    override fun insertTest(test: Test) {
    }
}