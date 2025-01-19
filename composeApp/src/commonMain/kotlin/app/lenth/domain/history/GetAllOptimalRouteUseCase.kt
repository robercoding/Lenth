package app.lenth.domain.history

import app.lenth.data.RouteHistoryRepository
import app.lenth.domain.mapper.toRouteDomain
import app.lenth.domain.models.OptimalRouteDomain
import kotlinx.coroutines.flow.map

class GetAllOptimalRouteUseCase(private val routeHistoryRepository: RouteHistoryRepository) {
    operator fun invoke() = routeHistoryRepository.getAllRoutes().map { list ->
        list.map { entity -> entity.toRouteDomain() }
    }

}