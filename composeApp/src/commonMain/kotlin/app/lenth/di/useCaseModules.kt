package app.lenth.di

import app.lenth.domain.FindHamiltonianCycleMinimumCostUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModules = module {
    single { FindHamiltonianCycleMinimumCostUseCase(get()) }
}