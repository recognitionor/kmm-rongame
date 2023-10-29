package com.jhlee.kmm_rongame.bank.domain

data class Bank(
    val id: Int,
    val name: String,
    val account: Int,
    val history: List<History>,
    val interestRate: Long,

    ) {
    data class History(
        val bankId: Int, val amount: Int, val date: Long
    )
}
