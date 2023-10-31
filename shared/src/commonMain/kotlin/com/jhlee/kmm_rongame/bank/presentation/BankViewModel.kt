package com.jhlee.kmm_rongame.bank.presentation

import com.jhlee.kmm_rongame.bank.domain.Bank
import com.jhlee.kmm_rongame.bank.domain.BankDataSource
import com.jhlee.kmm_rongame.coin.presentation.CoinListState
import com.jhlee.kmm_rongame.core.domain.Resource
import com.jhlee.kmm_rongame.core.util.Logger
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlin.math.absoluteValue

class BankViewModel(private val bankDataSource: BankDataSource) : ViewModel() {
    companion object {
        const val VIEWMODEL_KEY = "bank_view_model"
    }

    private val _state = MutableStateFlow(BankState())
    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)

    init {
        getBank()
    }


    fun addCommentIndex() {
        _state.update {
            it.copy(commentIndex = (state.value.commentIndex.plus(1)))
        }
    }

    fun processDeposit(depositMoney: Int) {
        if (depositMoney.absoluteValue != 0) {
            bankDataSource.deposit(state.value.bank?.id ?: 0, depositMoney).onEach {
                when (it) {
                    is Resource.Success -> {
                        getBank()
                    }

                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                }
            }.launchIn(viewModelScope)
        }
    }

    fun processWithDraw(item: Bank.History) {
        bankDataSource.withDraw(item).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    getBank()
                }
                is Resource.Loading -> {}
                is Resource.Error -> {}
            }

        }.launchIn(viewModelScope)
    }

    fun setComment(commentList: List<String>) {
        _state.update {
            it.copy(commentList = commentList)
        }
    }

    private fun initBank() {
        bankDataSource.initBank().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { bank ->
                        _state.update { it.copy(bank = bank) }
                    }
                }

                is Resource.Error -> {}
                is Resource.Loading -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun setNeedUserRefresh(needUserRefresh: Boolean) {
        _state.update { it.copy(needUserRefresh = needUserRefresh) }
    }

    private fun getBank() {
        bankDataSource.getBank(1).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.update { it.copy(bank = result.data, needUserRefresh = true) }
                }

                is Resource.Error -> {
                    initBank()
                }

                is Resource.Loading -> {
                }
            }
        }.launchIn(viewModelScope)
    }
}