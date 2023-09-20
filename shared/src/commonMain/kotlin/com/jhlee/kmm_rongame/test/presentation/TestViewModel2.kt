package com.jhlee.kmm_rongame.test.presentation

import com.jhlee.kmm_rongame.test.domain.TestDataSource
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class TestViewModel2(private val testDataSource: TestDataSource) : ViewModel() {
    private val _state = MutableStateFlow(TestListState())

    val state = _state

//    val state = combine(_state, testDataSource.getTestList()) { state, testList ->
//        state.copy(testList = testList)
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), TestListState())
//    val state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), _state.value)
//    val state = combine(_state, testDataSource.getTestList()) { state, testList ->
//        _state.value
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), TestListState())

    init {
        test()
    }

    fun test() {
        _state.update {
            it.copy(count = state.value.count + 1)
        }
    }

}