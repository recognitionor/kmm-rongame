package com.jhlee.kmm_rongame.card.domain

import com.jhlee.kmm_rongame.core.domain.Resource
import kotlinx.coroutines.flow.Flow

interface CardDataSource {
    fun getCardList(): Flow<Resource<List<Card>>>
    fun gatchaCard(): Flow<Resource<Card>>
}