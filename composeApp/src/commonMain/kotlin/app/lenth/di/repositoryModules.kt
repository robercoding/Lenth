package app.lenth.di

import app.lenth.data.GeoCodingRepository
import app.lenth.data.RouteHistoryRepository

val repositoryModule = org.koin.dsl.module {
    single { GeoCodingRepository(get()) }
    single { RouteHistoryRepository(get()) }
}