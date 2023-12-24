package com.jhlee.kmm_rongame.di

import com.jhlee.kmm_rongame.attend.domain.AttendDataSource
import com.jhlee.kmm_rongame.bank.domain.BankDataSource
import com.jhlee.kmm_rongame.card.domain.CardCombinationDataSource
import com.jhlee.kmm_rongame.card.domain.CardDataSource
import com.jhlee.kmm_rongame.cardgame.domain.CardGameDataSource
import com.jhlee.kmm_rongame.coin.domain.CoinDataSource
import com.jhlee.kmm_rongame.main.domain.MainDataSource
import com.jhlee.kmm_rongame.quiz.domain.QuizDataSource
import com.jhlee.kmm_rongame.test.domain.TestDataSource


expect class AppModule {

    val dbTestDataSource: TestDataSource

    val remoteCoinDataSource: CoinDataSource

    val dbMainDataSource: MainDataSource

    val dbCardDataSource: CardDataSource

    val dbQuizDataSource: QuizDataSource

    val dbAttendDataSource: AttendDataSource

    val dbBankDataSource: BankDataSource

    val dbCardGameDataSource: CardGameDataSource

    val dbCardCombinationDataSource: CardCombinationDataSource
}