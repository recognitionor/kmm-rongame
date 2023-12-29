package com.jhlee.kmm_rongame.core.data

import app.cash.sqldelight.db.SqlDriver


expect class DatabaseDriverFactory {
    fun create(): SqlDriver
}