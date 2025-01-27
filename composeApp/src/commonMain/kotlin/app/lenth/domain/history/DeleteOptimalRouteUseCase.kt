package app.lenth.domain.history

import app.lenth.data.RouteHistoryRepository

class DeleteOptimalRouteUseCase(private val routeHistoryRepository: RouteHistoryRepository) {
    suspend operator fun invoke(id: Int) = routeHistoryRepository.delete(id)

}