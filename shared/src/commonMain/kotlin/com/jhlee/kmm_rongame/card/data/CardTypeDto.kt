package com.jhlee.kmm_rongame.card.data

import kotlinx.serialization.Serializable

@Serializable
data class CardTypeDto(
    var id: Int,
    var name: String,
    var strongList: String,
) {
    companion object {

        suspend fun parseJson(json: String): List<CardTypeDto> {
            val modifiedString = json.replace("\"", "\\\"").lines()
            modifiedString.drop(1)
            val dataLines = modifiedString.drop(1)
            val tempList = mutableListOf<CardTypeDto>()
            dataLines.forEach {
                val items = it.split(",\\s*(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())
                try {
                    tempList.add(
                        CardTypeDto(
                            id = items[0].toInt(), name = items[1], strongList = items[2]
                        )
                    )
                } catch (ignored: Exception) {
                }
            }
            return tempList
        }
    }
}
