package app.lenth.domain.history

import app.lenth.domain.FindHamiltonianCycleMinimumCostUseCase
import app.lenth.domain.mapper.toDomain
import app.lenth.ui.history.models.OptimalRouteUi

class GetOptimalRouteUseCase(
    private val getStaticImageUseCase: GetStaticImageUseCase,
    private val findHamiltonianCycleMinimumCostUseCase: FindHamiltonianCycleMinimumCostUseCase
) {
    suspend operator fun invoke(places: List<String>): OptimalRouteUi {
        val hamiltonianCycle = findHamiltonianCycleMinimumCostUseCase(places)
        val image = getStaticImageUseCase(hamiltonianCycle.path.map { it.toDomain() })
        return hamiltonianCycle.copy(mapImage = image)
    }
}