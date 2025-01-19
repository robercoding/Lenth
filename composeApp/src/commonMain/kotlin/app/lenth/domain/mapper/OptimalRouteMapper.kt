package app.lenth.domain.mapper

import app.lenth.data.db.models.OptimalRouteEntity
import app.lenth.domain.models.OptimalRouteDomain
import app.lenth.ui.history.models.OptimalRouteUi
import kotlin.random.Random

fun OptimalRouteDomain.toRouteEntity(): OptimalRouteEntity {
    return OptimalRouteEntity(
        distance = distance,
        list = list
    )
}

fun OptimalRouteEntity.toRouteDomain(): OptimalRouteDomain {
    return OptimalRouteDomain(
        id = id,
        distance = distance,
        list = list
    )
}

fun OptimalRouteDomain.toUiModel(): OptimalRouteUi {
    return OptimalRouteUi(
        id = id ?: Random.nextInt(),
        distance = distance,
        path = list
    )
}

fun OptimalRouteUi.toDomainModel(): OptimalRouteDomain {
    if(id == -1) {
        return OptimalRouteDomain(
            distance = distance,
            list = path
        )
    }
    return OptimalRouteDomain(
        id = id,
        distance = distance,
        list = path
    )
}