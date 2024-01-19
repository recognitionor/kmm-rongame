package com.jhlee.kmm_rongame.book.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.card.domain.Card
import com.jhlee.kmm_rongame.common.view.StarRatingBar
import com.jhlee.kmm_rongame.common.view.TitleContentTextView
import com.jhlee.kmm_rongame.constants.GradeConst.Companion.TYPE_MAP
import com.jhlee.kmm_rongame.core.presentation.rememberBitmapFromBytes
import com.jhlee.kmm_rongame.core.util.Logger

@Composable
fun BookListItem(book: Card, index: Int) {
    val cardImg = rememberBitmapFromBytes(book.image)

    Card(
        modifier = Modifier.height(200.dp).border(
            width = 4.dp, color = Color.Black, shape = RoundedCornerShape(8.dp)
        )
    ) {
        Row {
            if (index % 2 == 1) {
                Box(modifier = Modifier.weight(1f).padding(8.dp)) {
                    cardImg?.let {
                        Image(
                            bitmap = it,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                BookListItemInfoView(Modifier.weight(1f), book, index)
            } else {
                BookListItemInfoView(Modifier.weight(1f), book, index)
                Box(modifier = Modifier.weight(1f).padding(8.dp)) {
                    cardImg?.let {
                        Image(
                            bitmap = it,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BookListItemInfoView(modifier: Modifier, book: Card, index: Int) {
    Column(modifier = modifier.padding(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${index + 1}.",
                style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.width(5.dp))
            val fontSize = when (book.name.length) {
                in 0..3 -> 28.sp
                in 4..4 -> 18.sp
                in 5..7 -> 10.sp
                else -> 6.sp
            }
            Text(
                text = book.name, style = TextStyle(fontSize = fontSize, fontWeight = FontWeight.Bold)
            )
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Row {
                TitleContentTextView("타입", book.type.map { it.name })
            }
        }


        Spacer(modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TYPE_MAP[book.grade]?.color?.let {
                StarRatingBar(book.grade, it, size = 15.dp)
            }
        }
    }
}