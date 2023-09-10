package com.jhlee.kmm_rongame.di

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.coin.data.RemoteCoinDataSource
import com.jhlee.kmm_rongame.coin.domain.CoinDataSource
import com.jhlee.kmm_rongame.core.data.DatabaseDriverFactory
import com.jhlee.kmm_rongame.core.data.KtorClientFactory
import com.jhlee.kmm_rongame.main.data.DBUserInfoDataSource
import com.jhlee.kmm_rongame.main.domain.MainDataSource
import com.jhlee.kmm_rongame.test.data.DBTestDataSource
import com.jhlee.kmm_rongame.test.domain.TestDataSource

actual class AppModule {

    actual val dbTestDataSource: TestDataSource by lazy {
        DBTestDataSource(
            db = AppDatabase(
                driver = DatabaseDriverFactory().create()
            )
        )
    }
    actual val remoteCoinDataSource: CoinDataSource by lazy {
        RemoteCoinDataSource(KtorClientFactory.build())
    }
    actual val dbMainDataSource: MainDataSource by lazy {
        DBUserInfoDataSource(
            db = AppDatabase(
                driver = DatabaseDriverFactory().create()
            )
        )
    }
}