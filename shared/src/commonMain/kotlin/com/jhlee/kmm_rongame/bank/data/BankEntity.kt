package com.jhlee.kmm_rongame.bank.data

import com.jhlee.kmm_rongame.bank.domain.Bank
import database.BankEntity

fun BankEntity.toBank(historyList: List<Bank.History> = emptyList()): Bank {
    return Bank(
        id = id.toInt(),
        name = name,
        account = account.toInt(),
        historyList,
        interestRate = interestRate
    )
}