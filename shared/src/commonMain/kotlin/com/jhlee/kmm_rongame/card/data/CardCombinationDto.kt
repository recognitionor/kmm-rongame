package com.jhlee.kmm_rongame.card.data

import kotlinx.serialization.Serializable

@Serializable
data class CardCombinationDto(
    val id: Int,
    val item1: String,
    val item2: String,
    val result: String,
) {
    companion object {
        suspend fun parseJson(json: String): List<CardCombinationDto> {
            val modifiedString = json.replace("\"", "\\\"").lines()
            val dataLines = modifiedString.drop(1)
            val tempList = mutableListOf<CardCombinationDto>()
            dataLines.forEachIndexed { index, s ->
                val items = s.split(",\\s*(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())
                try {
                    tempList.add(
                        CardCombinationDto(
                            id = index,
                            item1 = items[1],
                            item2 = items[2],
                            result = items[3].replace("\\\"", "").trim('"')
                        )
                    )
                } catch (ignored: Exception) {
                }
            }
            return tempList
        }
    }
}
