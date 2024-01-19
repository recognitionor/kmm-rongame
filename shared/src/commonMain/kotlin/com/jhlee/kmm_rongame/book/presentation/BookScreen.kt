package com.jhlee.kmm_rongame.book.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.common.view.CustomDropDownMenu
import com.jhlee.kmm_rongame.common.view.CustomStyledTextField
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
import com.jhlee.kmm_rongame.ui.theme.LightColorScheme
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun BookScreen(viewModel: MainViewModel, appModule: AppModule) {
    val viewModel = getViewModel(key = BookViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { BookViewModel(appModule.dbBookDataSource) })
    val state: BookState by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth().padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height(30.dp))
        if (state.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize().background(color = LightColorScheme.background),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        Row(modifier = Modifier.height(56.dp)) {
            Box(
                modifier = Modifier.weight(4f).fillMaxHeight(), contentAlignment = Alignment.Center
            ) {
                CustomStyledTextField(state.search) {
                    viewModel.setSearchKeyword(it)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center
            ) {
                CustomDropDownMenu {

                }
            }

        }

        Spacer(modifier = Modifier.height(18.dp))
        LazyColumn {
            items(state.bookList.size) {
                val book = state.bookList[it]
                Column {
                    BookListItem(book, it)
                    Spacer(modifier = Modifier.height(18.dp))
                }

            }
        }
        Spacer(modifier = Modifier.height(150.dp))
    }


}