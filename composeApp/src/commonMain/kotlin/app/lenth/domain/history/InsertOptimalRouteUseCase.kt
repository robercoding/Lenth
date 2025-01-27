package app.lenth.domain.history

import app.lenth.data.RouteHistoryRepository
import app.lenth.domain.models.OptimalRouteDomain

class InsertOptimalRouteUseCase(private val routeHistoryRepository: RouteHistoryRepository) {
    suspend operator fun invoke(routeDomain: OptimalRouteDomain) = routeHistoryRepository.insert(routeDomain)
}