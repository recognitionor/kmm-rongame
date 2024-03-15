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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.constants.GradeConst
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.presentation.rememberBitmapFromBytes
import com.jhlee.kmm_rongame.core.util.Logger
import com.jhlee.kmm_rongame.di.AppModule
import com.jhlee.kmm_rongame.main.presentation.MainViewModel
import com.jhlee.kmm_rongame.pandora.presentation.PandoraDetailScreen
import com.jhlee.kmm_rongame.pandora.presentation.PandoraState
import com.jhlee.kmm_rongame.pandora.presentation.PandoraViewModel
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlin.math.abs
import kotlin.math.sqrt

@Composable
fun PandoraScreen(mainViewModel: MainViewModel, appModule: AppModule, gridSize: Int = 7) {



    val viewModel = getViewModel(key = PandoraViewModel.VIEWMODEL_KEY, factory = viewModelFactory {
        PandoraViewModel(
            appModule.dbPandoraDataSource
        )
    })
    val state: PandoraState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initCardList(gridSize)
    }

    val cellSize = 50.dp
    val startIndex = state.startSelectedIndex
    val selectedIndex = state.afterSelectedIndex
    val isSelectMode = state.isSelectMode

    val questionImage = getCommonImageResourceBitMap(SharedRes.images.ic_question)

    Column(
        modifier = Modifier.background(Color.White).fillMaxWidth().padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            for (y in 0 until gridSize) {

                Row {
                    for (x in 0 until gridSize) {
                        val index = y * gridSize + x
                        val isSelected = index == startIndex
                        val isAdjacent =
                            abs(index % gridSize - startIndex % gridSize) == 1 && abs(index / gridSize - startIndex / gridSize) == 0 || abs(
                                index / gridSize - startIndex / gridSize
                            ) == 1 && abs(index % gridSize - startIndex % gridSize) == 0
                        var isSelectable = (isSelected || isAdjacent)
                        if (state.cardList.size > index) {
                            isSelectable = isSelectable && state.cardList[index] != null
                        }

                        // 애니메이션 조건 변경
                        val shouldAnimate = remember(startIndex, selectedIndex) {
                            startIndex != -1 && selectedIndex != -1
                        }
                        val startIndexRow = startIndex / gridSize
                        val startIndexCol = startIndex % gridSize
                        val selectIndexRow = selectedIndex / gridSize
                        val selectIndexCol = selectedIndex % gridSize
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

                        Box(
                            modifier = Modifier.size(cellSize).background(Color.White).clickable {
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
                                                Logger.log("click : $index")
                                                viewModel.cardCombination(startIndex, index)
                                            }
                                        }
                                    }
                                }
                            }.then(
                                if (isSelectMode && !isSelectable) Modifier.alpha(0.3f) else Modifier
                            ).border(
                                border = if (isSelectMode && isSelectable && startIndex != index) BorderStroke(
                                    3.dp, Color.Black
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
                            if (state.cardList.isEmpty()) {
                                return
                            }
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
                                        Image(bitmap = image, contentDescription = null, modifier = Modifier.padding(top = 10.dp, start = 10.dp))
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
                        if (x < gridSize - 1) {
                            Spacer(modifier = Modifier.width(3.dp))
                        }
                    }

                }
                if (y < gridSize - 1) {
                    Spacer(modifier = Modifier.height(3.dp))
                }
            }
        }
        state.detailCard?.let {
            PandoraDetailScreen(it)
        }

    }
}
