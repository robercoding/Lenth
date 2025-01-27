package app.lenth.di

import app.lenth.domain.FindHamiltonianCycleMinimumCostUseCase
import app.lenth.domain.SearchPlacesByInputQueryUseCase
import app.lenth.domain.history.ClearAllOptimalRoutesUseCase
import app.lenth.domain.history.DeleteOptimalRouteUseCase
import app.lenth.domain.history.GetAllOptimalRouteUseCase
import app.lenth.domain.history.GetOptimalRouteUseCase
import app.lenth.domain.history.GetStaticImageUseCase
import app.lenth.domain.history.InsertOptimalRouteUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModules = module {
    single { GetOptimalRouteUseCase(get(), get()) }
    single { FindHamiltonianCycleMinimumCostUseCase(get()) }
    single { GetStaticImageUseCase(get()) }
    single { SearchPlacesByInputQueryUseCase(get()) }
    single { InsertOptimalRouteUseCase(get()) }

    single { GetAllOptimalRouteUseCase(get()) }
    single { ClearAllOptimalRoutesUseCase(get()) }
    single { DeleteOptimalRouteUseCase(get()) }
}