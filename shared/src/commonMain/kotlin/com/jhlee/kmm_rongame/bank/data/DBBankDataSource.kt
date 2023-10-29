package com.jhlee.kmm_rongame.bank.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.bank.domain.Bank
import com.jhlee.kmm_rongame.bank.domain.BankDataSource
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock

class DBBankDataSource(db: AppDatabase) : BankDataSource {
    private val queries = db.dbQueries
    override fun initBank(): Flow<Resource<Bank>> = flow {
        Logger.log("initBank :")
        val bank = Bank(1, "고양이은행", 0, emptyList(), 1)
        queries.insertBank(bank.id.toLong(), bank.name, bank.account.toLong(), bank.interestRate)
        emit(Resource.Success(bank))
    }

    override fun getBank(bankId: Long): Flow<Resource<Bank>> = flow {
        try {
            val result = queries.getBank(bankId).executeAsOne()
            val historyList = queries.getBankHistory(bankId).executeAsList()
            emit(Resource.Success(result.toBank(historyList.map { it.toBankHistory() })))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun getBankHistory(bankId: Int): Flow<Resource<List<Bank.History>>> = flow {

    }

    override fun deposit(bankId: Int, money: Int): Flow<Resource<Unit>> = flow {
        try {
            queries.minusUserMoney(money.toLong())
            queries.depositBankAccount(money.toLong(), bankId.toLong())
            queries.insertBankHistory(
                bankId.toLong(), money.toLong(), Clock.System.now().epochSeconds
            )
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun withDraw(bankId: Int, money: Int): Flow<Resource<Unit>> = flow {
        try {
            queries.plusUserMoney(money.toLong())
            queries.withdrawBankAccount(money.toLong(), bankId.toLong())
            queries.insertBankHistory(
                bankId.toLong(), -money.toLong(), Clock.System.now().epochSeconds
            )
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }
}