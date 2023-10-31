package com.jhlee.kmm_rongame.bank.domain

import kotlinx.datetime.Clock

class BankUtils {
    companion object {
        fun hasDayPassed(offsetTime: Long): Boolean {
            return offsetTime + (60 * 60 * 24) <= Clock.System.now().epochSeconds
        }
    }
}