package com.jhlee.kmm_rongame.book.domain

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.core.domain.Resource
import kotlinx.coroutines.flow.Flow

interface BookDataSource {
    fun getBookList(): Flow<Resource<List<Card>>>

}