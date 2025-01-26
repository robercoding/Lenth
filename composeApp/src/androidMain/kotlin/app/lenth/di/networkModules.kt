package app.lenth.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import org.koin.core.module.Module
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

actual val networkModule: Module = module {
    single<HttpClient> {
        HttpClient(OkHttp) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(HttpTimeout) {
                this.connectTimeoutMillis = 40000L
                socketTimeoutMillis = 40000L
                requestTimeoutMillis = 40000L
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        encodeDefaults = true
                        isLenient = true
                        allowSpecialFloatingPointValues = true
                        allowStructuredMapKeys = true
                        prettyPrint = true
                        ignoreUnknownKeys = true
                        useArrayPolymorphism = false
                    },
                )
            }
        }
    }
}