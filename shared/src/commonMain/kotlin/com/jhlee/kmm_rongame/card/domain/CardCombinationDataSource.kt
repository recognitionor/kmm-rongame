package com.jhlee.kmm_rongame.card.domain

import com.jhlee.kmm_rongame.core.domain.Resource
import kotlinx.coroutines.flow.Flow

interface CardCombinationDataSource {
    fun getCardList(): Flow<Resource<List<Card>>>
    fun combinationCard(list: List<Card?>): Flow<Resource<Card>>
}