package app.lenth.data

import app.lenth.database.RouteDatabase
import app.lenth.domain.mapper.toRouteEntity
import app.lenth.domain.models.OptimalRouteDomain

class RouteHistoryRepository(private val routeDatabase: RouteDatabase) {
    suspend fun insert(routeDomain: OptimalRouteDomain) {
        routeDatabase.getRouteDao().insert(routeDomain.toRouteEntity())
    }

    fun getAll() = routeDatabase.getRouteDao().getAllRoutesFlow()

    suspend fun clearAll() = routeDatabase.getRouteDao().clearAllRoutes()

    suspend fun delete(routeId: Int) = routeDatabase.getRouteDao().deleteById(routeId)
}