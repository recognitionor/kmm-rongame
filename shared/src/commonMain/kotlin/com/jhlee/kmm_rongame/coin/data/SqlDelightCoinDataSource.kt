package com.jhlee.kmm_rongame.coin.data

import com.jhlee.kmm_rongame.coin.domain.Coin
import com.jhlee.kmm_rongame.coin.domain.CoinDataSource
import com.jhlee.kmm_rongame.core.domain.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SqlDelightCoinDataSource : CoinDataSource {
    override fun getCoinList(): Flow<Resource<List<Coin>>> {
        return flow { }
    }
}