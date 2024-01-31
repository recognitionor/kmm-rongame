package com.jhlee.kmm_rongame.cardselector.presentaion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.common.view.CustomDropDownMenu
import com.jhlee.kmm_rongame.common.view.CustomStyledTextField
import com.jhlee.kmm_rongame.constants.CardFilterConst
import com.jhlee.kmm_rongame.core.util.Logger

@Composable
fun CardSelectSearchBar(
    searchKeyword: String,
    sortIndex: Int,
    isReverse: Boolean,
    toggleReverseFilter: () -> Unit,
    searchSortList: (keyword: String, sortIndex: Int) -> Unit
) {
    Row(modifier = Modifier.padding(12.dp).height(56.dp)) {
        Box(
            modifier = Modifier.weight(3.5f).fillMaxHeight(), contentAlignment = Alignment.Center
        ) {
            CustomStyledTextField(searchKeyword) {
                searchSortList.invoke(it, sortIndex)
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier.weight(1.5f).fillMaxHeight(), contentAlignment = Alignment.Center
        ) {
            CustomDropDownMenu(CardFilterConst.CARD_FILTER_MAP.values.toList()) {
                searchSortList.invoke(searchKeyword, it)
            }
        }
        Column(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(checked = isReverse, onCheckedChange = {
                toggleReverseFilter.invoke()
            }, modifier = Modifier.weight(1f))
            Text(
                "역순", textAlign = TextAlign.Center, style = TextStyle(
                    fontSize = 10.sp, fontWeight = FontWeight.Light, color = Color.Black
                ), modifier = Modifier.weight(1f)
            )
        }
    }
}