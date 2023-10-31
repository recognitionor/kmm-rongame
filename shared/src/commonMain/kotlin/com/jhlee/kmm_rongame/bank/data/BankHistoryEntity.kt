package com.jhlee.kmm_rongame.bank.data

import com.jhlee.kmm_rongame.bank.domain.Bank
import database.BankHistoryEntity

fun BankHistoryEntity.toBankHistory(): Bank.History {
    return Bank.History(
        id = id.toInt(),
        bankId = bank_id.toInt(),
        amount = amount.toInt(),
        interestRate = interestRate,
        date = date
    )
}