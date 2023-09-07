package com.jhlee.kmm_rongame.di

import com.jhlee.kmm_rongame.coin.domain.CoinDataSource
import com.jhlee.kmm_rongame.test.domain.TestDataSource


expect class AppModule {
    val dbTestDataSource: TestDataSource

    val remoteCoinDataSource: CoinDataSource
}