import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.backKeyListener
import com.jhlee.kmm_rongame.common.view.createDialog
import com.jhlee.kmm_rongame.constants.GradeConst
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.presentation.rememberBitmapFromBytes
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainState
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
import com.jhlee.kmm_rongame.pandora.presentation.PandoraDetailScreen
import com.jhlee.kmm_rongame.pandora.presentation.PandoraState
import com.jhlee.kmm_rongame.pandora.presentation.PandoraTargetScreen
import com.jhlee.kmm_rongame.pandora.presentation.PandoraViewModel
import com.jhlee.kmm_rongame.ui.theme.Green300
import com.jhlee.kmm_rongame.ui.theme.Red300
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlin.math.abs

@Composable
fun PandoraScreen(
    mainViewModel: MainViewModel, appModule: AppModule, initSize: Int = 4, dismiss: () -> Unit
) {
    val viewModel = getViewModel(key = PandoraViewModel.VIEWMODEL_KEY, factory = viewModelFactory {
        PandoraViewModel(
            appModule.dbPandoraDataSource
        )
    })
    val state: PandoraState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.setWholeScreen(true)
        viewModel.initCardList(initSize)
        backKeyListener = {

        }
    }

    DisposableEffect(Unit) {
        onDispose {
            backKeyListener = null
            mainViewModel.dismissDialog()
            mainViewModel.setWholeScreen(false)
        }
    }



    if (state.cardListSize <= 0) {
        return
    }

    val rowSize = state.rowSize
    val colSize = state.colSize

    val cellSize = 50.dp
    val startIndex = state.startSelectedIndex
    val selectedIndex = state.afterSelectedIndex
    val isSelectMode = state.isSelectMode
    val questionImage = getCommonImageResourceBitMap(SharedRes.images.ic_question)

    when (state.pandoraState) {
        PandoraState.PICK_DONE -> {
            dismiss.invoke()
        }

        PandoraState.STATE_GAME_OVER -> {
            mainViewModel.showDialog(
                MainState.PANDORA_GAME_OVER,
                createDialog(
                    title = "끝!",
                    message = "더 이상 움직일수 있는 카드가 없어요",
                    positiveButtonCallback = {
                        dismiss.invoke()
                    })
            )
        }

        PandoraState.STATE_GAME_WIN -> {
            mainViewModel.showDialog(
                MainState.PANDORA_GAME_OVER,
                createDialog(title = "승", message = "내가 가져 갈 카드를 고르세요", positiveButtonCallback = {
                    viewModel.selectDetailCard(null)
                    mainViewModel.dismissDialog()
                    viewModel.selectStatus(PandoraState.STATE_GAME_WIN_PICK)
                })
            )
        }
    }


    Column(
        modifier = Modifier.background(Color.White).fillMaxSize().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(modifier = Modifier.weight(0.7f)) {
            if (state.pandoraState == PandoraState.STATE_GAME_WIN_PICK) {
                Column {
                    Button({
                        dismiss.invoke()
                    }) {
                        Text(
                            text = "그냥 나가기",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp)
                        )
                    }
                    if (state.detailCard != null) {
                        Button({
                            state.detailCard?.let {
                                viewModel.pickCard(it)
                            }
                        }) {
                            Text(
                                text = "선택 카드 갖고 나가기",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                                    .padding(start = 30.dp, end = 30.dp)
                            )
                        }
                    }
                }
            } else {
                state.goalCard?.let {
                    PandoraTargetScreen(it, state)
                }
            }
        }

        Box(modifier = Modifier.fillMaxWidth().weight(2f)) {
            getCommonImageResourceBitMap(SharedRes.images.ic_enemy)?.let { it1 ->
                Image(
                    modifier = Modifier.fillMaxSize().alpha(0.4f),
                    bitmap = it1,
                    contentDescription = null
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (y in 0 until colSize) {
                    Row {
                        for (x in 0 until rowSize) {
                            val index = y * rowSize + x
                            val validItem = state.cardListSize > index
                            Logger.log("index : $index")
                            val isSelected = index == startIndex
                            val isAdjacent =
                                (abs(index / rowSize - startIndex / rowSize) == 1 && abs(index % rowSize - startIndex % rowSize) == 0) || (abs(
                                    index / rowSize - startIndex / rowSize
                                ) == 0 && abs(index % rowSize - startIndex % rowSize) == 1)
                            var isSelectable = (isSelected || isAdjacent)
                            if (validItem) {
                                isSelectable = isSelectable && state.cardList[index] != null
                            }

                            // 애니메이션 조건 변경
                            val shouldAnimate = remember(startIndex, selectedIndex) {
                                startIndex != -1 && selectedIndex != -1
                            }

                            // 여기가 꼬여있음
                            val startIndexRow = startIndex / rowSize
                            val startIndexCol = startIndex % rowSize
                            val selectIndexRow = selectedIndex / rowSize
                            val selectIndexCol = selectedIndex % rowSize

                            val xSign = if (startIndexCol < selectIndexCol) 1 else -1
                            val ySign = if (startIndexRow < selectIndexRow) 1 else -1
                            val targetX =
                                if ((!shouldAnimate) || (startIndexCol == selectIndexCol)) 0f else (cellSize * xSign).value
                            val targetY =
                                if (!shouldAnimate || (startIndexRow == selectIndexRow)) 0f else (cellSize * ySign).value
                            val translationX by animateFloatAsState(
                                targetValue = if (isSelectMode && isSelectable) targetX else 0f,
                                animationSpec = tween(durationMillis = 300)
                            )
                            val translationY by animateFloatAsState(
                                targetValue = if (isSelectMode && isSelectable) targetY else 0f,
                                animationSpec = tween(durationMillis = 300)
                            )
                            val translationAlpha by animateFloatAsState(
                                targetValue = if (startIndex > -1 && selectedIndex > -1) 0f else 1f,
                                animationSpec = tween(durationMillis = 500)
                            )

                            Box(modifier = Modifier.size(cellSize)
                                .background(if (validItem) Color.White else Color.Transparent)
                                .then(if (validItem) Modifier.clickable {
                                    if (!validItem) {
                                        return@clickable
                                    }
                                    if (state.pandoraState == PandoraState.STATE_GAME_WIN_PICK) {
                                        if (state.detailCard != null) {
                                            viewModel.selectDetailCard(null)
                                        } else {
                                            viewModel.selectDetailCard(state.cardList[index])
                                        }
                                    } else {
                                        if (state.cardList[index] == null) {
                                            viewModel.gatchaCard(index)
                                        } else {
                                            if (!isSelectMode) {
                                                viewModel.changeSelectMode(
                                                    isSelectMode = true,
                                                    startSelectIndex = index,
                                                    afterSelectIndex = selectedIndex

                                                )
                                            } else {
                                                if (!isSelectable) {
                                                    viewModel.changeSelectMode()
                                                } else {
                                                    if (startIndex == index) {
                                                        viewModel.changeSelectMode()
                                                    } else {
                                                        viewModel.cardCombination(
                                                            startIndex, index
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else Modifier).then(
                                    if (isSelectMode && !isSelectable) Modifier.alpha(0.3f) else Modifier
                                ).border(
                                    border = if (isSelectMode && isSelectable && startIndex != index) BorderStroke(
                                        3.dp, Green300
                                    ) else if (state.cardList.size > index && state.detailCard != null && state.detailCard == state.cardList[index]) BorderStroke(
                                        3.dp, Red300
                                    ) else BorderStroke(0.dp, Color.Transparent)
                                ).graphicsLayer {
                                    if (startIndex == index) {
                                        this.translationX = translationX
                                        this.translationY = translationY
                                        this.alpha = translationAlpha
                                    }
                                    if (selectedIndex == index) {
                                        this.translationX = -translationX
                                        this.translationY = -translationY
                                        this.alpha = translationAlpha
                                    }

                                }, contentAlignment = Alignment.Center
                            ) {
                                if (state.cardList.size > index) {
                                    val tempCard = state.cardList[index]
                                    val image = rememberBitmapFromBytes(tempCard?.image)
                                    if (tempCard == null) {
                                        questionImage?.let {
                                            val colorFilter = if (index == state.cardGatchaIndex) {
                                                GradeConst.TYPE_MAP[(state.cardGatchaLoading + 1) % GradeConst.TYPE_MAP.size]?.color
                                            } else null

                                            Image(
                                                bitmap = it,
                                                colorFilter = if (colorFilter != null) ColorFilter.tint(
                                                    colorFilter
                                                ) else null,
                                                contentDescription = null,
                                                alignment = Alignment.TopCenter,
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }
                                    } else {
                                        image?.let {
                                            Box {
                                                Image(
                                                    bitmap = image,
                                                    contentDescription = null,
                                                    modifier = Modifier.padding(
                                                        top = 10.dp, start = 10.dp
                                                    )
                                                )
                                                Text(
                                                    modifier = Modifier.background(color = Color.White),
                                                    text = "${tempCard.upgrade}/${tempCard.potential}",
                                                    style = TextStyle(
                                                        fontSize = 15.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            if (x < colSize) {
                                Spacer(modifier = Modifier.width(3.dp))
                            }
                        }
                    }
                    if (y < rowSize) {
                        Spacer(modifier = Modifier.height(3.dp))
                    }
                }
            }
        }

        Box(modifier = Modifier.weight(1.4f)) {
            state.detailCard?.let {
                PandoraDetailScreen(it)
            }

        }
    }
}
