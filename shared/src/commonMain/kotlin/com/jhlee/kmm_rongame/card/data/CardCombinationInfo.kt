package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.core.data.ImageStorage
import kotlinx.serialization.Serializable
import migrations.CardInfoEntity

@Serializable
data class CardCombinationInfo(
    val resultCard: Card,
    val stuffList: List<Card>,
    val percent: Float,
    val isOpened: Boolean = false,
)

suspend fun CardInfoEntity.toCard(): Card {
    return Card(
        cardId = this.id.toInt(),
        name = this.name,
        nameEng = this.nameEng,
        description = this.description,
        type = HashSet(),
        grade = this.grade?.toInt() ?: 0,
        image = ImageStorage.getImage(this.image ?: "")
    )
}