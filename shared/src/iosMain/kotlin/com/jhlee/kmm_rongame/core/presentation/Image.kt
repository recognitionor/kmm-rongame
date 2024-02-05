@file:OptIn(ExperimentalForeignApi::class, ExperimentalForeignApi::class)

package com.jhlee.kmm_rongame.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import dev.icerock.moko.resources.ImageResource
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Image
import platform.Foundation.NSData
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation

@Composable
actual fun getCommonImageResourceBitMap(imageResource: ImageResource?): ImageBitmap? {
    val uiImage = imageResource?.toUIImage()
    uiImage?.let { img ->
        val data: NSData? = UIImagePNGRepresentation(img)
        val byteArray = data?.bytes?.readBytes(data.length.toInt())
        byteArray?.let {
            return Bitmap.makeFromImage(Image.makeFromEncoded(it)).asComposeImageBitmap()
        }

    }
    return null
}
@Composable
actual fun getPlatformImageResourceBitMap(name: String): ImageBitmap? {
    val uiImage = UIImage.imageNamed(name)
    uiImage?.let { img ->
        val data: NSData? = UIImagePNGRepresentation(img)
        val byteArray = data?.bytes?.readBytes(data.length.toInt())
        byteArray?.let {
            return Bitmap.makeFromImage(Image.makeFromEncoded(it)).asComposeImageBitmap()
        }
    }
    return null
}

@Composable
actual fun rememberBitmapFromBytes(bytes: ByteArray?): ImageBitmap? {
    return remember(bytes) {
        if (bytes != null) {
            Bitmap.makeFromImage(
                Image.makeFromEncoded(bytes)
            ).asComposeImageBitmap()
        } else {
            null
        }
    }
}