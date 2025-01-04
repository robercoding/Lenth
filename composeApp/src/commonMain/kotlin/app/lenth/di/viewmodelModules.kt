package app.lenth.di

import app.lenth.ui.SearchViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf

val viewModelModules = org.koin.dsl.module {
    viewModelOf(::SearchViewModel)
}