package com.jhlee.kmm_rongame.quiz.data

import com.jhlee.kmm_rongame.quiz.domain.Quiz
import database.QuizEntity
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json.Default.decodeFromString

@Serializable
data class QuizDto(
    var id: Int,
    val category: String,
    val level: Int,
    val imageUrl: String,
    val answer: Int,
    val question: String,
    val choiceList: List<String>,
    val time: Long,
    val chance: Int,
    val reward: Int,
    val description: String = "",
    val selected: Int = -1,
    val durationTime: Long = -1
) {
    companion object {
        suspend fun parseJson(json: String): List<QuizDto> {
            val lines = json.replace("\"", "").replace("'", "").trim().lines()
            val dataLines = lines.drop(1)
            val tempList = mutableListOf<QuizDto>()
            dataLines.map { line ->
                try {
                    val items = line.split(",")
                    tempList.add(
                        QuizDto(
                            id = items[0].toInt(),
                            category = items[1],
                            level = items[2].toInt(),
                            imageUrl = items[3],
                            answer = items[4].toInt(),
                            question = items[5],
                            choiceList = items[5].split("|"),
                            time = items[7].toLong(),
                            chance = items[8].toInt(),
                            reward = items[9].toInt(),
                            description = items[10]
                        )
                    )
                } catch (ignored: Exception) {
                }
            }
            return tempList
        }
    }
}

fun QuizEntity.toQuiz() = Quiz(
    id.toInt(),
    category,
    level?.toInt() ?: 1,
    imageUrl,
    answer?.toInt() ?: 0,
    question,
    decodeFromString(choiceList) as List<String>,
    time ?: 0,
    chance?.toInt() ?: 0,
    reward?.toInt() ?: 0,
    description,
    selected?.toInt() ?: 0,
    durationTime?.toLong() ?: 0
)

fun QuizDto.toQuiz() = Quiz(
    id,
    category,
    level,
    imageUrl,
    answer,
    question,
    choiceList,
    time,
    chance,
    reward,
    description,
    selected,
    durationTime
)
