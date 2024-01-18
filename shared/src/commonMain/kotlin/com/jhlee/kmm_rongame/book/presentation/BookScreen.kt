package com.jhlee.kmm_rongame.book.presentation

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.common.view.CustomDropDownMenu
import com.jhlee.kmm_rongame.common.view.CustomStyledTextField
import com.jhlee.kmm_rongame.core.presentation.rememberBitmapFromBytes
import com.jhlee.kmm_rongame.core.util.Logger
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
        Row (modifier = Modifier.height(80.dp))  {
            Box(modifier = Modifier.weight(4f).fillMaxHeight()) {
                CustomStyledTextField(state.search) {
                    viewModel.setSearchKeyword(it)
                }
            }
            Box(modifier = Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                CustomDropDownMenu {

                }
            }

        }


//        LazyColumn {
//            items(state.bookList.size) {
//                val book = state.bookList[it]
//                Column(modifier = Modifier.height(100.dp)) {
//                    val cardImg = rememberBitmapFromBytes(book.image)
//                    Row(modifier = Modifier.fillMaxWidth()) {
//                        cardImg?.let { image ->
//                            Image(
//                                bitmap = image,
//                                contentDescription = null,
//                                colorFilter = if (book.count == 0) ColorFilter.tint(Color.Black) else null
//                            )
//                        }
//                        Spacer(modifier = Modifier.width(12.dp))
//                        Text(text = book.name)
//                        Text(text = book.count.toString())
//                    }
//                }
//            }
//        }
        Spacer(modifier = Modifier.height(100.dp))
    }


}