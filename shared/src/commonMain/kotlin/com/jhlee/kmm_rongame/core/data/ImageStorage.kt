package com.jhlee.kmm_rongame.core.data

expect class ImageStorage {
    companion object {
        suspend fun saveImage(bytes: ByteArray): String
        suspend fun getImage(fileName: String): ByteArray?
        suspend fun deleteImage(fileName: String)

        suspend fun existImage(filePath: String): Boolean
    }
}