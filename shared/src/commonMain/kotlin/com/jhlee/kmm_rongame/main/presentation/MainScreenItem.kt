package com.jhlee.kmm_rongame.main.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Money
import androidx.compose.ui.graphics.vector.ImageVector


class MainScreenItem(val name: String, val icon: ImageVector) {
    companion object {
        val SCREEN_LIST: List<MainScreenItem> = arrayListOf<MainScreenItem>().apply {
            add(MainScreenItem("홈", Icons.Default.Home))
            add(MainScreenItem("놀이", Icons.Default.Games))
            add(MainScreenItem("도감", Icons.Default.Book))
            add(MainScreenItem("획득", Icons.Default.Money))
        }
    }
}