package com.jhlee.kmm_rongame.bank.domain

import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.Clock

class BankUtils {
    companion object {
        fun hasDayPassed(offsetTime: Long): Boolean {
            return offsetTime + (60 * 60 * 24) <= Clock.System.now().epochSeconds
        }

        fun formatNumber(number: Int): String {
            return number.toString().reversed().chunked(3).joinToString(",").reversed()
        }

        fun formatNumber(number: Long): String {
            return number.toString().reversed().chunked(3).joinToString(",").reversed()
        }
    }
}