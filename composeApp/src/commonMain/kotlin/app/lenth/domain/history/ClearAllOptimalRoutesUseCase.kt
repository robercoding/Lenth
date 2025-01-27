package app.lenth.domain.history

import app.lenth.data.RouteHistoryRepository

class ClearAllOptimalRoutesUseCase(private val routeHistoryRepository: RouteHistoryRepository) {
    suspend operator fun invoke() = routeHistoryRepository.clearAll()

}