package app.lenth.di

import app.lenth.AndroidPlatform
import app.lenth.Platform
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {
    // single<Platform> { AndroidPlatform(get()) }
    singleOf(::AndroidPlatform) bind Platform::class
}