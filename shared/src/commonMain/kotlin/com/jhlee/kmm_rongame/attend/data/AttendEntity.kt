package com.jhlee.kmm_rongame.attend.data

import com.jhlee.kmm_rongame.attend.domain.Attend
import migrations.AttendEntity

fun AttendEntity.toAttend(): Attend {
    return Attend(
        id,
        date,
    )
}