package com.jhlee.kmm_rongame.pandora.domain

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.core.domain.Resource
import kotlinx.coroutines.flow.Flow

interface PandoraDataSource {
    fun combinationCard(list: List<Card?>): Flow<Resource<Card>>
    fun gatchaBasicCard(): Flow<Resource<Card>>

}