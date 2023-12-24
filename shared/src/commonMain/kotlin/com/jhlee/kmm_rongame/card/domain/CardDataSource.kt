package com.jhlee.kmm_rongame.card.domain

import com.jhlee.kmm_rongame.core.domain.Resource
import kotlinx.coroutines.flow.Flow

interface CardDataSource {

    fun getCardInfoList(): Flow<Resource<List<Card>>>

    fun getCardTypeInfoList(): Flow<Resource<List<CardType>>>
    fun getCardCombineInfoList(): Flow<Resource<List<CardType>>>
    fun getMyCardList(): Flow<Resource<List<Card>>>
    fun gatchaBasicCard(): Flow<Resource<Card>>
}