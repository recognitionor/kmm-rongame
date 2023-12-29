package com.jhlee.kmm_rongame.card.data

import com.jhlee.kmm_rongame.core.util.Logger
import kotlinx.serialization.Serializable

@Serializable
data class CardTypeDto(
    var id: Int,
    var name: String,
    var strongList: String,
) {
    companion object {

        suspend fun parseStrongList(strongList: String): HashMap<String, Int> {
            val resultMap = hashMapOf<String, Int>()
            try {
                val pairs = strongList.split("|")
                for (pair in pairs) {
                    val (keyStr, valueStr) = pair.split(":")
                    val value = valueStr.toInt()
                    resultMap[keyStr] = value
                }
            } catch (e: Exception) { Logger.log("error : $e")}
            return resultMap
        }

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
