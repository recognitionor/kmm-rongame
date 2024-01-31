package com.jhlee.kmm_rongame.common.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.jhlee.kmm_rongame.book.presentation.BookState
import com.jhlee.kmm_rongame.constants.CardFilterConst

@Composable
fun CustomDropDownMenu(sortFilterList: List<String>, selectCallback: (sortIndex: Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(CardFilterConst.POWER) }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = sortFilterList[selectedIndex], fontSize = 12.sp)
            Spacer(modifier = Modifier.weight(1f))
            if (expanded) {
                Icon(
                    Icons.Default.ArrowDropUp, contentDescription = ""
                )
            } else {
                Icon(
                    Icons.Default.ArrowDropDown, contentDescription = ""
                )
            }

        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            sortFilterList.forEachIndexed { index, s ->
                DropdownMenuItem(text = {
                    Text(text = sortFilterList[index])
                }, onClick = {
                    expanded = false
                    selectedIndex = index
                    selectCallback.invoke(index)
                })
            }
        }
    }
}