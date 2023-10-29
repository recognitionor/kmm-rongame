package com.jhlee.kmm_rongame.di

import android.content.Context
import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.attend.data.DBAttendDataSource
import com.jhlee.kmm_rongame.attend.domain.AttendDataSource
import com.jhlee.kmm_rongame.bank.data.DBBankDataSource
import com.jhlee.kmm_rongame.bank.domain.BankDataSource
import com.jhlee.kmm_rongame.card.data.DBCardDataSource
import com.jhlee.kmm_rongame.card.domain.CardDataSource
import com.jhlee.kmm_rongame.coin.data.RemoteCoinDataSource
import com.jhlee.kmm_rongame.coin.domain.CoinDataSource
import com.jhlee.kmm_rongame.core.data.DatabaseDriverFactory
import com.jhlee.kmm_rongame.core.data.KtorClientFactory
import com.jhlee.kmm_rongame.main.data.DBUserInfoDataSource
import com.jhlee.kmm_rongame.main.domain.MainDataSource
import com.jhlee.kmm_rongame.quiz.data.QuizDataSourceImpl
import com.jhlee.kmm_rongame.quiz.domain.QuizDataSource
import com.jhlee.kmm_rongame.test.data.DBTestDataSource
import com.jhlee.kmm_rongame.test.domain.TestDataSource

actual class AppModule(
    private val context: Context
) {

    actual val dbTestDataSource: TestDataSource by lazy {
        DBTestDataSource(
            db = AppDatabase(
                driver = DatabaseDriverFactory(context).create(),
            ),
        )
    }

    actual val remoteCoinDataSource: CoinDataSource by lazy {
        RemoteCoinDataSource(KtorClientFactory.build())
    }

    actual val dbMainDataSource: MainDataSource by lazy {
        DBUserInfoDataSource(
            db = AppDatabase(
                driver = DatabaseDriverFactory(context).create(),
            )
        )
    }

    actual val dbCardDataSource: CardDataSource by lazy {
        DBCardDataSource(
            db = AppDatabase(
                driver = DatabaseDriverFactory(context).create(),
            )
        )
    }
    actual val dbQuizDataSource: QuizDataSource by lazy {
        QuizDataSourceImpl(
            db = AppDatabase(
                driver = DatabaseDriverFactory(context).create(),
            ), KtorClientFactory.build()
        )
    }
    actual val dbAttendDataSource: AttendDataSource by lazy {
        DBAttendDataSource(
            db = AppDatabase(
                driver = DatabaseDriverFactory(context).create(),
            )
        )
    }

    actual val dbBankDataSource: BankDataSource by lazy {
        DBBankDataSource(
            db = AppDatabase(
                driver = DatabaseDriverFactory(context).create(),
            )
        )
    }
}