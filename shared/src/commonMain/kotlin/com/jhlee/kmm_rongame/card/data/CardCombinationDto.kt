package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.core.util.Logger
import kotlinx.serialization.Serializable

@Serializable
data class CardCombinationDto(
    val id: Int,
    val item1: Int,
    val item2: Int,
    val result: String,
) {
    companion object {
        suspend fun parseJson(json: String): List<CardCombinationDto> {
            val modifiedString = json.replace("\"", "\\\"").lines()
            modifiedString.drop(1)
            val dataLines = modifiedString.drop(1)
            val tempList = mutableListOf<CardCombinationDto>()
            dataLines.forEach {
                val items = it.split(",\\s*(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())
                try {
                    tempList.add(CardCombinationDto(
                        id = items[0].toInt(),
                        item1 = items[1].toInt(),
                        item2 = items[2].toInt(),
                        result = items[3]
                    ))
                } catch (ignored: Exception) {
                }
            }
            return tempList
        }
    }
}
