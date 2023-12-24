package com.jhlee.kmm_rongame.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import dev.icerock.moko.resources.ImageResource


@Composable
expect fun getCommonImageResourceBitMap(imageResource: ImageResource?): ImageBitmap?
@Composable
expect fun getPlatformImageResourceBitMap(name: String): ImageBitmap?

@Composable
expect fun rememberBitmapFromBytes(bytes: ByteArray?): ImageBitmap?