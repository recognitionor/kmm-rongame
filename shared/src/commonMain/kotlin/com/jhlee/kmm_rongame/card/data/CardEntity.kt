package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.card.domain.CardType
import com.jhlee.kmm_rongame.core.data.ImageStorage
import com.jhlee.kmm_rongame.core.util.Logger
import database.CardInfoEntity
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

fun parseHashMap(input: String): HashMap<String, Int> {
    val keyValueRegex = Regex("(\\d+):(\\d+)")
    val matchResults = keyValueRegex.findAll(input)

    val hashMap = HashMap<String, Int>()

    for (matchResult in matchResults) {
        val key = matchResult.groupValues[1]
        val value = matchResult.groupValues[2].toIntOrNull() ?: 0
        hashMap[key] = value
    }

    return hashMap
}

suspend fun MyCardList.toCard(
    cardInfoEntity: CardInfoEntity,
): Card {
    val cardTypeSet: HashSet<CardType> = hashSetOf()
    type?.split("|")?.forEach { typeName ->
        CardInfoManager.CARD_TYPE_MAP[typeName]?.let {
            Logger.log("cardType : $it")
            cardTypeSet.add(it)
        }
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
    val pairs = type?.split("|") ?: emptyList()
    val tempSet: HashSet<CardType> = hashSetOf()
    for (pair in pairs) {
        CardInfoManager.getCardTypeFromId(pair.toInt())?.let {
            tempSet.add(it)
        }
    }
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
