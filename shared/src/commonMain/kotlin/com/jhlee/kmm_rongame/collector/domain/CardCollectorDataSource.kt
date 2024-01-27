package com.jhlee.kmm_rongame.collector.domain

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.core.domain.Resource
import kotlinx.coroutines.flow.Flow

interface CardCollectorDataSource {
    fun initCollectorWantedItem(): Flow<Resource<Unit>>
    fun getCollectorWantedList(): Flow<Resource<List<CardCollectorWantedItem>>>
    fun updateCollectorWanted(id: Long): Flow<Resource<Unit>>

    fun getSelectList(cardCollectorWantedItem: CardCollectorWantedItem): Flow<Resource<List<Card>>>
}