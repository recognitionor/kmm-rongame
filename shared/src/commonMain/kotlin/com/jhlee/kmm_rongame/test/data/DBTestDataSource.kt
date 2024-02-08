package com.jhlee.kmm_rongame.test.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.Firebase
import com.jhlee.kmm_rongame.card.data.CardInfoDto
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.storage
import com.jhlee.kmm_rongame.test.domain.Test
import com.jhlee.kmm_rongame.test.domain.TestDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DBTestDataSource(
    db: AppDatabase,
    private val httpClient: HttpClient,
) : TestDataSource {
    private val queries = db.dbQueries

    fun parseCSV(json: String) {
    }

    override fun getTestList(): Flow<List<Test>> = flow {
        Firebase.storage.reference.child("card").listAll().items.forEach {
            val csvString = httpClient.get(it.getDownloadUrl()).body<String>()
            parseCSV(csvString)
        }
        Firebase.storage.reference.listAll().items.forEach {

        }
    }

    override fun insertTest(test: Test) {
    }
}