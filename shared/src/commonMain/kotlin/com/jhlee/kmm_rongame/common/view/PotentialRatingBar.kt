package com.jhlee.kmm_rongame.common.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jhlee.kmm_rongame.SharedRes
import com.jhlee.kmm_rongame.core.presentation.getCommonImageResourceBitMap
import com.jhlee.kmm_rongame.core.util.Logger

@Composable
fun PotentialRatingBar(
    maxPotential: Int = 10, currentAbility: Int = 1, size: Dp = 20.dp
) {
    Row {
        repeat(maxPotential) {
            val image = if (currentAbility > it) {
                SharedRes.images.ic_potential_on
            } else {
                SharedRes.images.ic_potential_off
            }
            getCommonImageResourceBitMap(image)?.let { it1 ->
                Image(
                    bitmap = it1, contentDescription = null, modifier = Modifier.size(size)
                )
            }
        }
    }
}
