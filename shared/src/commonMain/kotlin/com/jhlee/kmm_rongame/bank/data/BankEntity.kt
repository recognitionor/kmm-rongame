package com.jhlee.kmm_rongame.bank.data

import com.jhlee.kmm_rongame.bank.domain.Bank
import migrations.BankEntity

fun BankEntity.toBank(historyList: List<Bank.History> = emptyList()): Bank {
    historyList.forEach {
        it.interestRate
    }
    return Bank(
        id = id.toInt(),
        name = name,
        account = 0,
        historyList,
        interestRate = interestRate
    )
}