package app.lenth.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpTimeout
import org.koin.core.module.Module
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual val networkModule: Module = org.koin.dsl.module {
    single<HttpClient> {
        HttpClient(Darwin) {
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