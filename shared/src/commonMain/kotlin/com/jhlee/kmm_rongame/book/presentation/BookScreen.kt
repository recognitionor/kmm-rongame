package com.jhlee.kmm_rongame.book.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.backKeyListener
import com.jhlee.kmm_rongame.card.presentation.CardDetailInfoScreen
import com.jhlee.kmm_rongame.collector.presentation.CardCollectorScreen
import com.jhlee.kmm_rongame.common.view.CustomDropDownMenu
import com.jhlee.kmm_rongame.common.view.CustomStyledTextField
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainState
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
import com.jhlee.kmm_rongame.ui.theme.LightColorScheme
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun BookScreen(mainViewModel: MainViewModel, appModule: AppModule) {
    val viewModel = getViewModel(key = BookViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { BookViewModel(appModule.dbBookDataSource) })
    val state: BookState by viewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        backKeyListener = {
            mainViewModel.selectedTab(MainState.NAVIGATION_TAB_HOME)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            backKeyListener = null
        }
    }

    when (state.screenState) {
        BookState.BOOK_SCREEN_DEFAULT -> {
            Column(
                modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(22.dp))
                Button(modifier = Modifier.fillMaxWidth().padding(start = 50.dp, end = 50.dp),
                    onClick = {
                        viewModel.selectScreenMode(BookState.BOOK_SCREEN_CARD_COLLECTOR)
                    }) {
                    Text(text = "카드 수집가 의 집")
                }

                Spacer(modifier = Modifier.height(12.dp))
                if (state.isLoading) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                            .background(color = LightColorScheme.background),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                Row(modifier = Modifier.height(56.dp)) {
                    Box(
                        modifier = Modifier.weight(4f).fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomStyledTextField(state.search) {
                            viewModel.searchSortBookList(keyword = it)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        CustomDropDownMenu {
                            viewModel.searchSortBookList(sortIndex = it)
                        }
                    }

                }

                Spacer(modifier = Modifier.height(18.dp))
                LazyColumn {
                    items(state.sortedBookList.size) {
                        val book = state.sortedBookList[it]
                        Column(modifier = Modifier.clickable {
                            if (book.count > 0) {
                                viewModel.setDetailCardInfo(book)
                                viewModel.selectScreenMode(BookState.BOOK_SCREEN_DETAIL)
                            }
                        }) {
                            BookListItem(book, it)
                            Spacer(modifier = Modifier.height(18.dp))
                        }
                    }
                }
            }
        }

        BookState.BOOK_SCREEN_DETAIL -> {
            state.detailCardInfo?.let {
                CardDetailInfoScreen(it) {
                    viewModel.selectScreenMode(BookState.BOOK_SCREEN_DEFAULT)
                }
            }
        }

        BookState.BOOK_SCREEN_CARD_COLLECTOR -> {
            CardCollectorScreen(mainViewModel, appModule) {
                viewModel.selectScreenMode(BookState.BOOK_SCREEN_DEFAULT)
            }
        }
    }
}