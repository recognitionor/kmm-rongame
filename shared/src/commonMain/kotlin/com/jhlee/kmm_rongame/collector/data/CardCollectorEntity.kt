package com.jhlee.kmm_rongame.collector.data

import com.jhlee.kmm_rongame.collector.domain.CardCollectorWantedItem
import migrations.CardCollectorEntity

fun CardCollectorEntity.toCardCollectorWantedItem(): CardCollectorWantedItem =
    CardCollectorWantedItem(
        null,
        isDone == true,
        reward.toInt(),
        grade?.toInt() ?: 0,
        upgrade?.toInt() ?: 0,
        power?.toInt() ?: 0,
        count.toInt()
    )