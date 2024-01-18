package com.jhlee.kmm_rongame.book.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.book.domain.BookDataSource
import com.jhlee.kmm_rongame.card.data.toCard
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.core.domain.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DBBookDataSource(db: AppDatabase) : BookDataSource {
    private val queries = db.dbQueries

    override fun getBookList(): Flow<Resource<List<Card>>> = flow {
        emit(Resource.Loading())
        try {
            val list = queries.getCardInfoList().executeAsList()
            emit(Resource.Success(list.map { it.toCard() }))
        } catch (e: Exception) {
            emit(Resource.Error("${e.message}"))
        }


    }
}