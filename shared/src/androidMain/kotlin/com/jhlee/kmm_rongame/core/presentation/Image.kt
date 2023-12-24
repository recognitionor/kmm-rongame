package com.jhlee.kmm_rongame.core.presentation

import android.graphics.BitmapFactory
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import dev.icerock.moko.resources.ImageResource

@Composable
actual fun getCommonImageResourceBitMap(imageResource: ImageResource?): ImageBitmap? {
    val ctx = LocalContext.current
    imageResource?.drawableResId?.let { resId ->
        val bitmap = BitmapFactory.decodeResource(ctx.resources, resId)
        return bitmap.asImageBitmap()
    }
    return null
}

@Composable
actual fun getPlatformImageResourceBitMap(name: String): ImageBitmap? {
    val ctx = LocalContext.current
    val resId = ctx.resources.getIdentifier(name, "drawable", ctx.packageName)
    return AppCompatResources.getDrawable(ctx, resId)?.toBitmap()?.asImageBitmap()
}

@Composable
actual fun rememberBitmapFromBytes(bytes: ByteArray?): ImageBitmap? {
    return remember(bytes) {
        if(bytes != null) {
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size).asImageBitmap()
        } else {
            null
        }
    }
}