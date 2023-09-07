package com.jhlee.kmm_rongame.core.util

import android.util.Log

actual object Logger {
    actual fun log(message: String) {
        Log.d("jhlee", message)
    }
}