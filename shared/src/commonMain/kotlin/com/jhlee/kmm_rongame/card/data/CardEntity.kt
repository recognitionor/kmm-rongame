package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardType
import com.jhlee.kmm_rongame.core.data.ImageStorage
import com.jhlee.kmm_rongame.core.util.Logger
import database.CardInfoEntity
import database.CardTypeEntity
import database.GetMyCard
import database.MyCardList
import kotlinx.serialization.json.Json.Default.decodeFromString

suspend fun MyCardList.toCard(): Card {
    val tempSet = decodeFromString(type ?: "") as HashSet<CardType>
    return Card(
        id.toInt(),
        cardId?.toInt() ?: 0,
        name ?: "",
        nameEng ?: "",
        grade?.toInt() ?: 1,
        ImageStorage.getImage(image ?: ""),
        description ?: "",
        tempSet,
        power?.toInt() ?: 0,
        potential?.toInt() ?: 0,
        upgrade?.toInt() ?: 0
    )
}

fun parseHashMap(input: String): HashMap<Int, Int> {
    val keyValueRegex = Regex("(\\d+):(\\d+)")
    val matchResults = keyValueRegex.findAll(input)

    val hashMap = HashMap<Int, Int>()

    for (matchResult in matchResults) {
        val key = matchResult.groupValues[1].toIntOrNull() ?: 0
        val value = matchResult.groupValues[2].toIntOrNull() ?: 0
        hashMap[key] = value
    }

    return hashMap
}

suspend fun MyCardList.toCard(
    cardInfoEntity: CardInfoEntity,
    typeList: MutableList<CardTypeEntity>,
): Card {
    val cardTypeSet: HashSet<CardType> = hashSetOf<CardType>()
    typeList.forEach {
        cardTypeSet.add(CardType(it.id.toInt(), it.name, parseHashMap(it.strongList)))
    }
    return Card(
        id.toInt(),
        cardId?.toInt() ?: 0,
        cardInfoEntity.name,
        cardInfoEntity.nameEng,
        grade?.toInt() ?: 1,
        ImageStorage.getImage(cardInfoEntity.image ?: ""),
        cardInfoEntity.description,
        cardTypeSet,
        power?.toInt() ?: 0,
        potential?.toInt() ?: 0,
        upgrade?.toInt() ?: 0
    )
}

suspend fun GetMyCard.toCard(): Card {
    val tempSet = decodeFromString(type ?: "") as HashSet<CardType>
    return Card(
        id.toInt(),
        cardId?.toInt() ?: 0,
        name ?: "",
        nameEng ?: "",
        grade?.toInt() ?: 1,
        ImageStorage.getImage(image ?: ""),
        description ?: "",
        tempSet,
        power?.toInt() ?: 0,
        potential?.toInt() ?: 0,
        upgrade?.toInt() ?: 0
    )
}
