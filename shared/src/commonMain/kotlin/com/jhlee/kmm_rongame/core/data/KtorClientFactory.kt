package com.jhlee.kmm_rongame.core.data

import com.jhlee.kmm_rongame.httpClient
import com.jhlee.kmm_rongame.initLogger
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json

object KtorClientFactory {

    fun build(): HttpClient {
        return httpClient {
            install(Logging) {
                level = LogLevel.HEADERS
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.v(tag = "HTTP Client", message = message)
                    }
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 5 * 1000
                connectTimeoutMillis = 3 * 1000
                socketTimeoutMillis = 5 * 1000
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }.also { initLogger() }
    }
}