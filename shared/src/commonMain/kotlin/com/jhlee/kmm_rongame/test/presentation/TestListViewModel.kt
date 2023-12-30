package com.jhlee.kmm_rongame.test.presentation

import com.jhlee.kmm_rongame.test.domain.Test
import com.jhlee.kmm_rongame.test.domain.TestDataSource
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TestListViewModel(private val testDataSource: TestDataSource) : ViewModel() {

    private val _state = MutableStateFlow(TestListState())

    val state = combine(_state, testDataSource.getTestList()) { state, testList ->
        state.copy(testList = testList)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), TestListState())

    fun onEvent(event: TestEvent) {
        when (event) {
            TestEvent.OnTestClick -> {
                viewModelScope.launch {
                    testDataSource.insertTest(Test(0, "test"))
                }
                println("test")
            }
        }
    }

}