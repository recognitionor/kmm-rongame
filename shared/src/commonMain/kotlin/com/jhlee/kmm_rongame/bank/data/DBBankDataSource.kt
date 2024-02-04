package com.jhlee.kmm_rongame.bank.data

import com.jhlee.kmm_rongame.AppDatabase
import com.jhlee.kmm_rongame.bank.domain.Bank
import com.jhlee.kmm_rongame.bank.domain.BankDataSource
import com.jhlee.kmm_rongame.bank.domain.BankUtils
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.utils.Utils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlin.random.Random

class DBBankDataSource(db: AppDatabase) : BankDataSource {
    private val queries = db.dbQueries
    override fun initBank(): Flow<Resource<Bank>> {
        val flow: Flow<Resource<Bank>> = flow {
            val bank = Bank(
                1,
                "고양이은행",
                0,
                emptyList(),
                Random(Clock.System.now().toEpochMilliseconds()).nextInt(1, 100).toLong()
            )
            queries.insertBank(bank.id.toLong(), bank.name, 1, bank.interestRate)
            emit(Resource.Success(bank))
        }
        return flow
    }

    override fun getBank(bankId: Long): Flow<Resource<Bank>> = flow {
        try {
            val result = queries.getBank(bankId).executeAsOne()
            val date = Utils.getCurrentDateInFormat().toLong()
            if ((result.date ?: 0) < date) {
                val newInterest =
                    Random(Clock.System.now().toEpochMilliseconds()).nextInt(1, 100).toLong()
                queries.insertBank(1, result.name, date, newInterest)
            }
            val historyList = queries.getBankHistory(bankId).executeAsList()
            val newResult = queries.getBank(bankId).executeAsOne()
            emit(Resource.Success(newResult.toBank(historyList.map { it.toBankHistory() })))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun getBankHistory(bankId: Int): Flow<Resource<List<Bank.History>>> = flow {

    }

    override fun deposit(bankId: Int, money: Int): Flow<Resource<Unit>> = flow {
        try {
            queries.minusUserMoney(money.toLong())
            val bank = queries.getBank(bankId.toLong()).executeAsOne()
            queries.insertBankHistory(
                bankId.toLong(), money.toLong(), bank.interestRate, Clock.System.now().epochSeconds
            )
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun withDraw(history: Bank.History): Flow<Resource<Unit>> = flow {
        try {
            val interest: Float
            val tempMoney: Int
            if (BankUtils.hasDayPassed(history.date)) {
                interest = history.amount * (history.interestRate.toFloat() / 100)
                tempMoney = history.amount + interest.toInt()
            } else {
                tempMoney = history.amount
            }
            queries.plusUserMoney(tempMoney.toLong())
            queries.removeBankHistory(history.id.toLong())
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
        }
    }

    override fun gift(): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        queries.plusUserMoney(100)
        emit(Resource.Success(Unit))
    }
}