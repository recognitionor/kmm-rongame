package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.utils.Utils.Companion.removeSpecialCharacters
import kotlinx.serialization.Serializable

@Serializable
data class CardInfoDto(
    var id: Int,
    val name: String,
    val nameEng: String,
    val grade: Int,
    val image: String,
    val description: String,
    val type: String,
) {
    companion object {
        suspend fun parseJson(json: String): List<CardInfoDto> {
            val modifiedString = json.replace("\"", "\\\"").lines()
            modifiedString.drop(1)
            val dataLines = modifiedString.drop(1)
            val tempList = mutableListOf<CardInfoDto>()
            dataLines.forEach {
                val items = it.split(",\\s*(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())
                try {
                    val cardInfoDto = CardInfoDto(
                        id = items[0].toInt(),
                        name = removeSpecialCharacters(items[1]),
                        nameEng = removeSpecialCharacters(items[2]),
                        grade = items[3].toInt(),
                        image = items[4],
                        description = items[5].replace("\\\"", "").trim('"'),
                        type = items[6],
                    )
                    tempList.add(cardInfoDto)
                } catch (ignored: Exception) {
                }
            }
            return tempList
        }
    }
}
