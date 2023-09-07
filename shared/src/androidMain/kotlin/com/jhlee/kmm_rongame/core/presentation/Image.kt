package com.jhlee.kmm_rongame.core.presentation

import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import dev.icerock.moko.resources.ImageResource

@Composable
actual fun getImageResourceBitMap(imageResource: ImageResource?): ImageBitmap? {
    val ctx = LocalContext.current
    imageResource?.drawableResId?.let { resId ->
        val bitmap = BitmapFactory.decodeResource(ctx.resources, resId)
        return bitmap.asImageBitmap()
    }
    return null
}