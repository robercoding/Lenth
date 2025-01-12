package app.lenth.di

import app.lenth.IOSPlatform
import app.lenth.Platform
import org.koin.dsl.module

actual val platformModule = module {
    single<Platform> { IOSPlatform() }
}