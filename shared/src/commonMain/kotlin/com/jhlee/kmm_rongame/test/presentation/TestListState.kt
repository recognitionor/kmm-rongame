package com.jhlee.kmm_rongame.test.presentation

import com.jhlee.kmm_rongame.test.domain.Test

data class TestListState(
    val testList: List<Test> = emptyList(), val count: Int = 0
)

