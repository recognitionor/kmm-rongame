package com.jhlee.kmm_rongame.coin.domain

import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.test.domain.Test
import kotlinx.coroutines.flow.Flow

interface CoinDataSource {
    fun getCoinList(): Flow<Resource<List<Coin>>>
}