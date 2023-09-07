package com.jhlee.kmm_rongame.test.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.test.domain.Test
import com.jhlee.kmm_rongame.test.domain.TestDataSource
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope

class DBTestDataSource(
    db: AppDatabase
) : TestDataSource {
    private val queries = db.dbQueries

    override fun getTestList(): Flow<List<Test>> {
        return queries.getTestList().asFlow().mapToList().map { testList ->
            supervisorScope {
                testList.map {
                    async { it.toTest() }
                }.map { it.await() }
            }
        }
    }

    override fun insertTest(test: Test) {
        queries.insertTestEntity(test.name)
    }
}