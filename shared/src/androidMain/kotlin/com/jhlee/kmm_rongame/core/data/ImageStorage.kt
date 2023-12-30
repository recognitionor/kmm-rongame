package com.jhlee.kmm_rongame.core.data

import android.content.Context
import com.jhlee.kmm_rongame.core.util.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

actual class ImageStorage {
    actual companion object {

        private var context: Context? = null

        fun setContext(context: Context) {
            this.context = context
        }

        actual suspend fun saveImage(bytes: ByteArray): String {
            return withContext(Dispatchers.IO) {
                val fileName = UUID.randomUUID().toString()
                context?.openFileOutput(fileName, Context.MODE_PRIVATE).use { outputStream ->
                    outputStream?.write(bytes)
                }
                fileName
            }
        }

        actual suspend fun getImage(fileName: String): ByteArray? {
            return withContext(Dispatchers.IO) {
                context?.openFileInput(fileName).use { inputStream ->
                    inputStream?.readBytes()
                }
            }
        }

        actual suspend fun deleteImage(fileName: String) {
            return withContext(Dispatchers.IO) {
                context?.deleteFile(fileName)
            }
        }

        actual suspend fun existImage(filePath: String): Boolean {
            return File(context?.filesDir, filePath).exists()
        }
    }

}