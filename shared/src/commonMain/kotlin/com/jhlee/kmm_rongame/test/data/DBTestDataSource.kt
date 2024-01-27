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
        val modifiedString = json.replace("\"", "\\\"").lines()
        val first = modifiedString[0]
        val second = modifiedString[1]
        Logger.log("first : $first")
        Logger.log("second : $second")

        val dataLines = modifiedString.drop(2)
        Logger.log("dataLineSize : ${dataLines.size}")
        val tempList = mutableListOf<CardInfoDto>()
        dataLines.forEach {
            val items = it.split(",\\s*(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())
            try {
                Logger.log("items : $items")
            } catch (ignored: Exception) {
            }
        }
    }

    override fun getTestList(): Flow<List<Test>> = flow {
        Logger.log("getTestList : ")
        Firebase.storage.reference.child("card").listAll().items.forEach {
            val csvString = httpClient.get(it.getDownloadUrl()).body<String>()
            parseCSV(csvString)
            Logger.log("new : ${it.name}")
        }
        Firebase.storage.reference.listAll().items.forEach {
            Logger.log("it : ${it.name}")

        }
    }

    override fun insertTest(test: Test) {
    }
}