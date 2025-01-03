package app.lenth.di

import app.lenth.data.GeoCodingRepository

val repositoryModule = org.koin.dsl.module {
    single { GeoCodingRepository(get()) }
}