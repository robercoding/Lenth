package app.lenth.domain.mapper

import app.lenth.data.db.models.OptimalRouteEntity
import app.lenth.data.db.models.PlaceEntity
import app.lenth.domain.PlaceUi
import app.lenth.domain.models.OptimalRouteDomain
import app.lenth.domain.models.PlaceDomain
import app.lenth.ui.history.models.OptimalRouteUi
import kotlin.random.Random

fun OptimalRouteDomain.toRouteEntity(): OptimalRouteEntity {
    return OptimalRouteEntity(
        distance = distance,
        list = list.map {
            PlaceEntity(
                place = it.name,
                lat = it.lat,
                lng = it.lng
            )
        },
        mapImage = mapImage
    )
}

fun OptimalRouteEntity.toRouteDomain(): OptimalRouteDomain {
    return OptimalRouteDomain(
        id = id,
        distance = distance,
        list = list.map {
            PlaceDomain(
                name = it.place,
                lat = it.lat,
                lng = it.lng
            )
        },
        mapImage = mapImage
    )
}

fun OptimalRouteDomain.toUiModel(): OptimalRouteUi {
    return OptimalRouteUi(
        id = id ?: Random.nextInt(),
        distance = distance,
        path = list.map {
            PlaceUi(
                name = it.name,
                lat = it.lat,
                lng = it.lng
            )
        },
        mapImage = mapImage
    )
}

fun OptimalRouteUi.toDomain(): OptimalRouteDomain {
    val listPath = path.map {
        PlaceDomain(
            name = it.name,
            lat = it.lat,
            lng = it.lng
        )
    }
    if(id == -1) {
        return OptimalRouteDomain(
            distance = distance,
            list = listPath,
            mapImage = mapImage
        )
    }
    return OptimalRouteDomain(
        id = id,
        distance = distance,
        list = listPath,
        mapImage = mapImage
    )
}