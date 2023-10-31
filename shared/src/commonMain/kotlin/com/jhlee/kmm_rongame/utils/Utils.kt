package com.jhlee.kmm_rongame.utils

import com.jhlee.kmm_rongame.core.util.Logger
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class Utils {

    companion object {

        fun formatUnixEpochTime(epochTime: Long): String {
            val instant = Instant.fromEpochSeconds(epochTime)
            val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

            val year = localDateTime.year
            val month = localDateTime.monthNumber.toString().padStart(2, '0')
            val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
            val hour = localDateTime.hour.toString().padStart(2, '0')
            val minute = localDateTime.minute.toString().padStart(2, '0')
            val second = localDateTime.second.toString().padStart(2, '0')

            return "${year}년${month}월 ${day}일 ${hour}시 ${minute}분 ${second}초"
        }

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