package com.jhlee.kmm_rongame.bank.presentation

import com.jhlee.kmm_rongame.bank.domain.Bank

data class BankState(
    val isLoading: Boolean = false,
    val needUserRefresh: Boolean = false,
    val commentList: List<String> = emptyList(),
    val commentIndex: Int = 0,
    val bank: Bank? = null,
    val bankHistory: List<Bank.History> = emptyList(),
    val error: String = ""
) {
    companion object {
        const val BANK_COMMENT_HELLO = 0
        const val BANK_COMMENT_INCOME = 1
        const val BANK_COMMENT_1 = 2
        const val BANK_COMMENT_2 = 3
        const val BANK_COMMENT_3 = 4

    }
}
