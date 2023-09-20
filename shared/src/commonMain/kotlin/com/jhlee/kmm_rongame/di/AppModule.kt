package com.jhlee.kmm_rongame.di

import com.jhlee.kmm_rongame.card.domain.CardDataSource
import com.jhlee.kmm_rongame.coin.domain.CoinDataSource
import com.jhlee.kmm_rongame.main.domain.MainDataSource
import com.jhlee.kmm_rongame.test.domain.TestDataSource


expect class AppModule {

    val dbTestDataSource: TestDataSource

    val remoteCoinDataSource: CoinDataSource

    val dbMainDataSource: MainDataSource

    val dbCardDataSource: CardDataSource
}