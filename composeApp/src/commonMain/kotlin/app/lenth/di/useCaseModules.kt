package app.lenth.di

import app.lenth.domain.FindHamiltonianCycleMinimumCostUseCase
import app.lenth.domain.SearchPlacesByInputQueryUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModules = module {
    single { FindHamiltonianCycleMinimumCostUseCase(get()) }
    single { SearchPlacesByInputQueryUseCase(get()) }
}