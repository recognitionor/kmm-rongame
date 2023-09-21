package com.jhlee.kmm_rongame.quiz.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.jhlee.kmm_rongame.card.presentation.CardViewModel
import com.jhlee.kmm_rongame.di.AppModule
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun QuizScreen(appModule: AppModule) {
    val quizViewModel = getViewModel(key = CardViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { QuizViewModel(appModule.dbQuizDataSource) })
    val cardStateValue by quizViewModel.state.collectAsState()


    Text(text = "quiz")
}