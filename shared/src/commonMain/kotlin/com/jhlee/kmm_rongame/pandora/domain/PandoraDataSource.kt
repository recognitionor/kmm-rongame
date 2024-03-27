package com.jhlee.kmm_rongame.pandora.domain

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.core.domain.Resource
import kotlinx.coroutines.flow.Flow

interface PandoraDataSource {

    fun getGoalCard(): Flow<Resource<Card>>
    fun combinationCard(list: List<Card?>): Flow<Resource<Card>>
    fun gatchaBasicCard(): Flow<Resource<Card>>

    fun checkGameOver(
        cardList: List<Card?>, index: Int, rowSize: Int, colSize: Int
    ): Flow<Resource<Boolean>>

    fun checkWin(cardList: List<Card?>): Flow<Resource<Boolean>>

    fun getStageList(): Flow<Resource<List<Card>>>

    fun pickCard(card: Card): Flow<Resource<Boolean>>

}