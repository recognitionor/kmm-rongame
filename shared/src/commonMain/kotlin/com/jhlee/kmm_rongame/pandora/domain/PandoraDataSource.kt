package com.jhlee.kmm_rongame.pandora.domain

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.main.domain.UserInfo
import kotlinx.coroutines.flow.Flow

interface PandoraDataSource {

    fun getGoalCard(): Flow<Resource<Card>>
    fun combinationCard(list: List<Card?>): Flow<Resource<Card>>
    fun gatchaBasicCard(): Flow<Resource<Card>>

    fun checkGameOver(
        cardList: List<Card?>, rowSize: Int, colSize: Int
    ): Flow<Resource<Boolean>>

    fun checkWin(): Flow<Resource<Boolean>>

    fun getStageList(): Flow<Resource<List<Card>>>

    fun pickCard(card: Card): Flow<Resource<Boolean>>

    fun test(): Flow<Resource<Boolean>>

}