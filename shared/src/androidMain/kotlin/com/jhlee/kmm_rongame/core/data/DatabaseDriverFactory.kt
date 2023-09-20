package com.jhlee.kmm_rongame.core.data

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.jhlee.kmm_rongame.AppDatabase

actual class DatabaseDriverFactory(
    private val context: Context
) {
    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(
            AppDatabase.Schema, context, "db.db"
        )
    }
}