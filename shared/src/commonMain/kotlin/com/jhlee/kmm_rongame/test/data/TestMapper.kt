package com.jhlee.kmm_rongame.test.data

import com.jhlee.kmm_rongame.test.domain.Test
import database.TestEntity

fun TestEntity.toTest(): Test {
    return Test(id = id, name = name)
}