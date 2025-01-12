package app.lenth.di.shared

import app.lenth.data.preferences.DevelopmentPreference
import org.koin.core.module.Module
import org.koin.dsl.module

val sharedDataModule: Module = module {
    single { DevelopmentPreference(get()) }
}