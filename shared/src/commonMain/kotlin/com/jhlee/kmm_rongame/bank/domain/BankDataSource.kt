package com.jhlee.kmm_rongame.bank.domain

import com.jhlee.kmm_rongame.core.domain.Resource
import kotlinx.coroutines.flow.Flow

interface BankDataSource {
    fun initBank(): Flow<Resource<Bank>>

    fun getBank(bankId: Long): Flow<Resource<Bank>>

    fun getBankHistory(bankId: Int): Flow<Resource<List<Bank.History>>>

    fun deposit(bankId: Int, money: Int): Flow<Resource<Unit>>

    fun withDraw(history: Bank.History): Flow<Resource<Unit>>
}