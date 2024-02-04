package com.jhlee.kmm_rongame.bank.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.backKeyListener
import com.jhlee.kmm_rongame.bank.domain.BankUtils
import com.jhlee.kmm_rongame.common.view.ClickableDefaults
import com.jhlee.kmm_rongame.common.view.NumberInputField
import com.jhlee.kmm_rongame.common.view.StoryDialog
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.presentation.getString
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainState
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
import com.jhlee.kmm_rongame.ui.theme.LightColorScheme
import com.jhlee.kmm_rongame.utils.Utils
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory

@Composable
fun BankScreen(mainViewModel: MainViewModel, appModule: AppModule, dismiss: () -> Unit) {
    val viewModel = getViewModel(key = BankViewModel.VIEWMODEL_KEY,
        factory = viewModelFactory { BankViewModel(appModule.dbBankDataSource) })

    val mainState by mainViewModel.state.collectAsState()
    val bankState by viewModel.state.collectAsState()
    val commentList = remember { mutableStateOf<List<String>>(emptyList()) }
    if (commentList.value.isEmpty()) {
        commentList.value = arrayListOf(
            getString(SharedRes.strings.bank_comment_hello),
            getString(SharedRes.strings.bank_comment_1),
            getString(SharedRes.strings.bank_comment_2),
            getString(SharedRes.strings.bank_comment_3),
            getString(SharedRes.strings.bank_comment_4),
            getString(SharedRes.strings.bank_comment_5)
        )
    }
    LaunchedEffect(Unit) {
        backKeyListener = {
            dismiss.invoke()
        }
        mainViewModel.setWholeScreen(true)
        viewModel.setComment(commentList.value)
    }

    DisposableEffect(Unit) {
        onDispose {
            backKeyListener = null
            mainViewModel.dismissDialog()
            mainViewModel.setWholeScreen(false)
        }
    }

    if (bankState.needUserRefresh) {
        mainViewModel.getUserInfo()
        viewModel.setNeedUserRefresh(false)
    }

    Scaffold(topBar = {
        Row {
            getCommonImageResourceBitMap(SharedRes.images.ic_back)?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier.clickable { dismiss.invoke() }.width(30.dp).height(30.dp)
                        .padding(5.dp)
                )
            }
        }

    }) {

        Box {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(60.dp))

                Row {
                    getCommonImageResourceBitMap(SharedRes.images.img_bank_cat)?.let {
                        Image(
                            bitmap = it,
                            contentDescription = null,
                            modifier = Modifier.size(150.dp)
                                .then(ClickableDefaults.getDefaultClickable(pressedAlpha = 0f) {
                                    viewModel.addCommentIndex()
                                })
                        )
                    }
                    Column(
                        Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${bankState.bank?.name}", style = TextStyle(
                                fontSize = 28.sp, fontWeight = FontWeight.Bold
                            )
                        )

                        Text(text = "${getString(SharedRes.strings.user_info_money_title)}  ${mainState.userInfo?.money ?: 0}")

                        Text(text = "${getString(SharedRes.strings.bank_account_balance)} : ${bankState.bank?.account ?: 0}")

                        Text(
                            text = "${getString(SharedRes.strings.bank_interest_rate)} ${bankState.bank?.interestRate ?: 0}%"
                        )
                    }
                }

                if (bankState.commentList.isNotEmpty()) {
                    val comment =
                        if (bankState.commentIndex != 0 && bankState.commentIndex % 100 == 0) {
                            getString(SharedRes.strings.bank_gift)
                        } else {
                            (bankState.commentList[bankState.commentIndex % bankState.commentList.size])
                        }

                    Box(modifier = Modifier.then(ClickableDefaults.getDefaultClickable(pressedAlpha = 0f) {
                        viewModel.addCommentIndex()
                    })) { StoryDialog(content = comment) }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row {
                    Button(modifier = Modifier.weight(1f)
                        .then(ClickableDefaults.getDefaultClickable {

                        }), enabled = (mainState.userInfo?.money ?: 0) >= 100, onClick = {
                        mainViewModel.showDialog(MainState.BANK_VIEW_MODE_DEPOSIT_DIALOG,
                            createDepositDialog(
                                "내가 가진 돈 :", "맡기실 돈을 입력 해주세요", mainState.userInfo?.money ?: 0
                            ) {
                                viewModel.processDeposit(it)
                                mainViewModel.dismissDialog()
                            })
                    }) {
                        Text(text = "입금")
                    }
                }
                bankState.bank?.history?.let {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(it) { item ->
                            Box(modifier = Modifier.fillMaxSize().padding(bottom = 10.dp)) {
                                Row {
                                    Column(modifier = Modifier.weight(1f)) {

                                        if (item.amount > 0) {
                                            Text(
                                                text = "저축한 금액 : ${item.amount}",
                                                color = Color.Blue,
                                                style = TextStyle(
                                                    fontSize = 16.sp, fontWeight = FontWeight.Bold
                                                )
                                            )
                                            Text(
                                                text = "기준 이자율 ${item.interestRate}%",
                                                style = TextStyle(
                                                    fontSize = 12.sp, fontWeight = FontWeight.Bold
                                                ),
                                                color = Color.Black
                                            )
                                            Text(
                                                text = "기대 이자 ${(item.amount.toLong() * (item.interestRate / 100.toFloat())).toInt()}",
                                                style = TextStyle(
                                                    fontSize = 12.sp, fontWeight = FontWeight.Bold
                                                ),
                                                color = Color.Black
                                            )
                                        } else {
                                            Text(
                                                text = "찾은간 금액 : ${item.amount}",
                                                color = LightColorScheme.error,
                                                style = TextStyle(
                                                    fontSize = 16.sp, fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }

                                        Text(
                                            text = Utils.formatUnixEpochTime(item.date),
                                            style = TextStyle(
                                                fontSize = 12.sp, fontWeight = FontWeight.Light
                                            ),
                                            color = Color.LightGray
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                    }
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.End // 수평 정렬을 가운데로 설정
                                    ) {
                                        var title = ""
                                        var color: Color = Color.Gray
                                        if (BankUtils.hasDayPassed(item.date)) {
                                            color = LightColorScheme.primary
                                            title = "이자와 출금"
                                        } else {
                                            color = Color.Gray
                                            title = "원금만 출금"
                                        }

                                        Button(
                                            onClick = {
                                                viewModel.processWithDraw(item)
                                            }, colors = ButtonDefaults.buttonColors(
                                                contentColor = Color.Black, containerColor = color
                                            )
                                        ) {
                                            Text(text = title)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun createDepositDialog(
    title: String, content: String, totalMyMoney: Int, confirm: (Int) -> Unit,
): @Composable () -> Unit = {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.8F))
            .pointerInput(Unit) {
                detectTapGestures { }
            }, contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(Color.White),
            modifier = Modifier.fillMaxWidth().padding(24.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var inputData by remember { mutableStateOf(100) }
                Text(
                    text = "$title $totalMyMoney 원", style = TextStyle(
                        fontSize = 24.sp, fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        Text(text = content)
                        Text(
                            text = "최소 100원만 받아 준다", style = TextStyle(
                                fontSize = 12.sp, fontWeight = FontWeight.Light
                            ), color = Color.LightGray
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    NumberInputField(totalMyMoney, {
                        inputData = it
                    }, modifier = Modifier.padding(5.dp))
                    Text(text = "원")
                }

                Row {
                    Button(enabled = (inputData >= 100) && true, onClick = {
                        confirm.invoke(inputData)
                    }) {
                        Text(text = getString(SharedRes.strings.confirm))
                    }
                    Spacer(Modifier.width(20.dp))
                    Button(enabled = true, onClick = {
                        confirm.invoke(0)
                    }) {
                        Text(text = getString(SharedRes.strings.cancel))
                    }
                }
            }
        }
    }
}