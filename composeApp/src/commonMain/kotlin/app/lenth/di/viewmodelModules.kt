package app.lenth.di

import app.lenth.ui.SearchViewModel
import app.lenth.ui.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf

val viewModelModules = org.koin.dsl.module {
    viewModelOf(::SearchViewModel)
    viewModelOf(::SettingsViewModel)
}