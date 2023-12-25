package com.jhlee.kmm_rongame.card.domain

import com.jhlee.kmm_rongame.core.data.ImageStorage
import database.CardInfoEntity
import kotlinx.serialization.Serializable

@Serializable
data class Card(
    var id: Int = -1,
    var cardId: Int = -1,
    val name: String,
    val nameEng: String,
    val grade: Int,
    val image: ByteArray?,
    val description: String,
    val type: HashSet<CardType>,
    val power: Int = 0,
    val potential: Int = 0,
    val upgrade: Int = 0,
) {
    companion object {
        suspend fun getCardFromCardInfo(
            cardInfoEntity: CardInfoEntity,
            power: Int = 0,
            potential: Int = 0,
            upgrade: Int = 0,
        ): Card {
            return Card(
                cardId = cardInfoEntity.id.toInt(),
                name = cardInfoEntity.name,
                nameEng = cardInfoEntity.nameEng,
                description = cardInfoEntity.description,
                image = ImageStorage.getImage(cardInfoEntity.image ?: ""),
                type = hashSetOf(),
                grade = cardInfoEntity.grade?.toInt() ?: 0,
                upgrade = upgrade,
                power = power,
                potential = potential
            )
        }
    }
}
