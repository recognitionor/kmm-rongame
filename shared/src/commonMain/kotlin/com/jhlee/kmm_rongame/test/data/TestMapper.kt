package com.jhlee.kmm_rongame.test.data

import com.jhlee.kmm_rongame.test.domain.Test
import migrations.TestEntity

fun TestEntity.toTest(): Test {
    return Test(id = id, name = name)
}