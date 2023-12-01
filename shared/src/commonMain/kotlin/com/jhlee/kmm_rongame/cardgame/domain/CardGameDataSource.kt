package com.jhlee.kmm_rongame.cardgame.domain

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.core.domain.Resource
import kotlinx.coroutines.flow.Flow

interface CardGameDataSource {
    fun getEnemyList(selectIndex: Int): Flow<Resource<List<Card>>>

    fun getMyCardList(): Flow<Resource<List<Card>>>
}