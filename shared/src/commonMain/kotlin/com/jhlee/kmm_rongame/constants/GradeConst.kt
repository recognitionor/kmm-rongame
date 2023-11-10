package com.jhlee.kmm_rongame.constants

import androidx.compose.ui.graphics.Color

class GradeConst {
    data class GradeInfo(
        val color: Color, val name: String
    )

    companion object {
        private const val COTTON_CANDY = "솜사탕"
        private const val PAPER = "종이"
        private const val GLASS = "유리"
        private const val WOOD = "나무"
        private const val ROCK = "바위"
        private const val IRON = "철"
        private const val DIAMOND = "다이아몬드"

        val TYPE_MAP: MutableMap<Int, GradeInfo> = mutableMapOf<Int, GradeInfo>().apply {
            this[0] = GradeInfo(Color.Gray, COTTON_CANDY)
            this[1] = GradeInfo(Color.DarkGray, PAPER)
            this[2] = GradeInfo(Color.Green, GLASS)
            this[3] = GradeInfo(Color.Cyan, WOOD)
            this[4] = GradeInfo(Color.Blue, ROCK)
            this[5] = GradeInfo(Color.Magenta, IRON)
            this[6] = GradeInfo(Color.Red, DIAMOND)
        }

    }
}