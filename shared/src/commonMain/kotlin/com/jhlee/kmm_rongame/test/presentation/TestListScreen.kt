package com.jhlee.kmm_rongame.test.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.di.AppModule
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestScreen(appModule: AppModule) {
    var count by remember {
        mutableStateOf(0)
    }

    val viewModel = getViewModel(key = "test-list-screen",
        factory = viewModelFactory { TestListViewModel(appModule.dbTestDataSource) })

    val state by viewModel.state.collectAsState()

    Column {
        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            count++
            viewModel.onEvent(TestEvent.OnTestClick)
        }) {
            Text(text = "add Btn : $count")

        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.testList) { test ->
                Row {
                    Text(text = test.name)
                    Text(text = "!!")
                }


            }
        }
    }
}