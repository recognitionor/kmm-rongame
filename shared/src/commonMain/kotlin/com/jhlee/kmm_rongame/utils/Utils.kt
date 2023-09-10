package com.jhlee.kmm_rongame.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class Utils {

    companion object {
        fun getCurrentDateInFormat(
            format: String = "yyyyMMdd", instant: Instant = Clock.System.now()

        ): String {
            val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

            // 원하는 포맷에 따라 문자열 생성
            return when (format) {
                "yyyyMMdd" -> "${localDateTime.year}${
                    localDateTime.monthNumber.toString().padStart(2, '0')
                }${localDateTime.dayOfMonth.toString().padStart(2, '0')}"
                // 추가적인 포맷을 처리하려면 여기에 다른 case를 추가하세요.
                else -> localDateTime.toString()
            }
        }
    }
}