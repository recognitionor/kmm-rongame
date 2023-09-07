package com.jhlee.kmm_rongame

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.jhlee.kmm_rongame.di.AppModule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(appModule: AppModule) {
    val selectedItem = remember { mutableStateOf(0) }
    val items = listOf("Songs", "Artists", "Playlists")
    Scaffold(
        bottomBar = {
            NavigationBar(
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(icon = { Icon(Icons.Filled.Favorite, contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem.value == index,
                        onClick = { selectedItem.value = index })
                }
            }
        }
    ) {
        // 여기에서 선택된 아이템에 따라 다른 컴포저블을 표시합니다.
        when (selectedItem.value) {
            0 -> Box { Text(text = "0") }
            1 -> Text(text = "1")
            2 -> Text(text = "2")
        }
    }
}