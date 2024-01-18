package com.jhlee.kmm_rongame.calendar.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.reward.presentation.RewardViewModel
import com.jhlee.kmm_rongame.ui.theme.LightColorScheme
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.ZoneOffset
import kotlinx.datetime.minus
import kotlinx.datetime.periodUntil
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

@Composable
fun CalendarScreen(rewardViewModel: RewardViewModel) {
    val currentDateTime = Clock.System.now()
    val timeZone = TimeZone.currentSystemDefault()
    val currentLocalDateTime = currentDateTime.toLocalDateTime(timeZone)

    val state by rewardViewModel.state.collectAsState()
    val year by remember { mutableStateOf(currentLocalDateTime.year) }
    val month by remember { mutableStateOf(currentLocalDateTime.month.ordinal + 1) }
    Column(
        modifier = Modifier.padding(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = year.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = month.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Calendar(year, month, rewardViewModel, state.attendedList.map { it.toString() })
    }
}

@Composable
fun Calendar(
    year: Int, month: Int, rewardViewModel: RewardViewModel, attendedList: List<String>
) {
    val firstDayOfMonth = LocalDate(year, month, 1)
    val lastDayOfMonth = firstDayOfMonth.plus(1, kotlinx.datetime.DateTimeUnit.MONTH).minus(
        1, kotlinx.datetime.DateTimeUnit.DAY
    )
    val daysInMonth = firstDayOfMonth.periodUntil(lastDayOfMonth).days
    val startDayOfWeek = (firstDayOfMonth.dayOfWeek.ordinal + 1) % 7


    val daysOfWeek = arrayOf("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")


    Row(modifier = Modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Surface(
                color = LightColorScheme.tertiary, modifier = Modifier.weight(1f)
            ) {
                Text(
                    fontSize = 10.sp,
                    text = dayOfWeek,
                    color = contentColorFor(Color.Gray),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
    val weeks = (daysInMonth + startDayOfWeek) / 7 + 1
    Column(modifier = Modifier.fillMaxWidth()) {
        for (week in 0 until weeks) {
            Row(modifier = Modifier.fillMaxWidth()) {
                for (dayOfWeek in 1..7) {
                    val dayOfMonth = (week * 7) + (dayOfWeek - (startDayOfWeek))
                    if (dayOfMonth in 1..daysInMonth + 1) {
                        val date = "$year${month.toString().padStart(2, '0')}${
                            dayOfMonth.toString().padStart(2, '0')
                        }"
                        Surface(
                            color = if (attendedList.contains(date)) Color.Black else Color.White,
                            shape = CircleShape,
                            modifier = Modifier.align(Alignment.CenterVertically).weight(1f)
                                .clip(CircleShape).clickable(onClick = {
                                    // Do something when a date is clicked
                                }).padding(8.dp),
                        ) {
                            Text(
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.CenterVertically)
                                    .fillMaxWidth(),
                                text = dayOfMonth.toString(),
                                color = if (attendedList.contains(date)) Color.Yellow else Color.Black,
                            )
                        }
                    } else {
                        Spacer(
                            modifier = Modifier.weight(1f).padding(8.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
