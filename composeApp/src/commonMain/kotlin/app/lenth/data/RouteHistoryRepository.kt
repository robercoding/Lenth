package app.lenth.data

import app.lenth.data.db.models.OptimalRouteEntity
import app.lenth.database.RouteDatabase
import app.lenth.domain.mapper.toRouteEntity
import app.lenth.domain.models.OptimalRouteDomain
import co.touchlab.kermit.Logger

class RouteHistoryRepository(private val routeDatabase: RouteDatabase) {
    suspend fun insertRoute(routeDomain: OptimalRouteDomain) {
        routeDatabase.getRouteDao().insert(routeDomain.toRouteEntity())
    }

    fun getAllRoutes() = routeDatabase.getRouteDao().getAllRoutesFlow()
}