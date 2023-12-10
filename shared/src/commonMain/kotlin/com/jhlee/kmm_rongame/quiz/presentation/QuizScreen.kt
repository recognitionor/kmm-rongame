package com.jhlee.kmm_rongame.quiz.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.card.presentation.HomeViewModel
import com.jhlee.kmm_rongame.common.view.ProgressBar
import com.jhlee.kmm_rongame.common.view.StarRatingBar
import com.jhlee.kmm_rongame.common.view.createDialog
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.presentation.getString
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.quiz.domain.Quiz
import com.jhlee.kmm_rongame.ui.theme.QuizBorder
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun QuizScreen(appModule: AppModule, callback: (totalPoint: Int) -> Unit) {
    val quizViewModel = getViewModel(key = HomeViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { QuizViewModel(appModule.dbQuizDataSource) })
    val quizState by quizViewModel.state.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            quizViewModel.clearQuizInfo()
        }
    }

    var quiz: Quiz? = null
    if (quizState.quizList.isNotEmpty() && quizState.quizList.size > quizState.quizIndex) {
        quiz = quizState.quizList[quizState.quizIndex]
    }

    when (quizState.quizStatus) {
        QuizState.QUIZ_STATUS_ALL_FINISH -> {
            createDialog(getString(SharedRes.strings.quiz_result_title),
                "총 ${quizState.quizTotalCount} 문제 중 ${quizState.quizTotalAnswer} 를 맞춰 ${quizState.quizTotalPoint}점을 획득합니다.",
                "img_smart_dragon",
                positiveButtonCallback = {
                    callback.invoke(quizState.quizTotalPoint)
                }).invoke()
        }

        QuizState.QUIZ_STATUS_DONE_FAIL -> {
            createDialog(getString(SharedRes.strings.quiz_fail_message),
                quiz?.description ?: "다음엔 꼭 맞추어야 한다!!",
                "img_smart_dragon",
                positiveButtonCallback = {
                    quizViewModel.nextStage()
                }).invoke()
        }

        QuizState.QUIZ_STATUS_DONE_SUCCESS -> {
            createDialog(getString(SharedRes.strings.quiz_success_message_title),
                "${quiz?.reward}${getString(SharedRes.strings.quiz_success_message_content)}",
                "img_smart_dragon",
                {
                    quizViewModel.nextStage()
                }).invoke()

        }

        QuizState.QUIZ_STATUS_LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.8F))
            ) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        QuizState.QUIZ_STATUS_ING -> {
            if (quizState.quizList.isNotEmpty()) {

                Column(modifier = Modifier.fillMaxSize().padding(15.dp)) {

                    ProgressBar((quizState.quizTimeProgress))

                    Spacer(modifier = Modifier.height(6.dp).fillMaxWidth())
                    Row {
                        Column(modifier = Modifier.weight(0.7f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = getString(SharedRes.strings.quiz_remain_time),
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(8.dp)
                                )
                                Text(
                                    text = "${((quiz?.time ?: 60) - (quizState.quizTime))}초 남음",
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = getString(SharedRes.strings.quiz_level),
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(8.dp)
                                )
                                StarRatingBar(quiz?.level ?: 0, Color.Magenta)
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = getString(SharedRes.strings.quiz_remain_chance),
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(8.dp)

                                )
                                Text(text = quizState.quizChanceCount.toString(), fontSize = 16.sp)
                            }
                        }
                        getCommonImageResourceBitMap(SharedRes.images.img_smart_dragon)?.let {
                            Image(
                                modifier = Modifier.weight(0.3f),
                                bitmap = it,
                                contentDescription = null
                            )
                        }
                    }

                    Text(
                        fontSize = 18.sp, text = quiz?.question ?: "", modifier = Modifier.border(
                            width = 2.dp, color = QuizBorder, shape = RoundedCornerShape(10.dp)
                        ).padding(8.dp).fillMaxWidth()
                            .then(Modifier.heightIn(max = 200.dp, min = 100.dp)) // 최대 높이 설정
                            .verticalScroll(rememberScrollState()) // 스크롤 활성화
                    )

                    Spacer(modifier = Modifier.height(16.dp).fillMaxWidth())

                    quiz?.choiceList?.forEachIndexed { index, it ->
                        Box(
                            modifier = Modifier.fillMaxWidth().height(60.dp).clickable {
                                quizViewModel.summitAnswer(index, quiz)
                            }, contentAlignment = Alignment.CenterStart
                        ) {
                            Row {
                                Text(
                                    fontSize = 20.sp,
                                    color = QuizBorder,
                                    text = "${index + 1}. ",
                                    modifier = Modifier.verticalScroll(rememberScrollState())
                                )
                                Text(
                                    fontSize = 20.sp,
                                    color = Color.Black,
                                    text = it,
                                    modifier = Modifier.verticalScroll(rememberScrollState())
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp).fillMaxWidth())
                        }
                    }
                }
            }
        }
    }
}