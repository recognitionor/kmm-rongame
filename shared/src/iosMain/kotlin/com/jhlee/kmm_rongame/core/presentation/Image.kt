package com.jhlee.kmm_rongame.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import dev.icerock.moko.resources.ImageResource
import kotlinx.cinterop.readBytes
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.UIKit.UIImageJPEGRepresentation

@Composable
actual fun getImageResourceBitMap(imageResource: ImageResource?): ImageBitmap? {
    val uiImage = imageResource?.toUIImage()
    uiImage?.let { img ->
        val data: NSData? = UIImageJPEGRepresentation(img, 1.0)
        val byteArray = data?.bytes?.readBytes(data.length.toInt())
        byteArray?.let {
            return Bitmap.makeFromImage(Image.makeFromEncoded(it)).asComposeImageBitmap()
        }

    }
    return null
}