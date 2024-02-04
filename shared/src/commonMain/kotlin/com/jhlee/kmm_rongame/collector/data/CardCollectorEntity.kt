package com.jhlee.kmm_rongame.collector.data

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.collector.domain.CardCollectorWantedItem
import migrations.CardCollectorEntity

fun CardCollectorEntity.toCardCollectorWantedItem(card: Card): CardCollectorWantedItem =
    CardCollectorWantedItem(
        id = id.toInt(),
        card = card,
        isDone == true,
        reward.toInt(),
        grade?.toInt() ?: 0,
        upgrade?.toInt() ?: 0,
        power?.toInt() ?: 0,
        count.toInt()
    )

