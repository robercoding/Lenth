package app.lenth.domain.mapper

import app.lenth.data.db.models.PlaceEntity
import app.lenth.domain.PlaceUi
import app.lenth.domain.models.PlaceDomain

fun PlaceUi.toDomain(): PlaceDomain {
    return PlaceDomain(
        name = name,
        lat = lat,
        lng = lng
    )
}

fun PlaceDomain.toUi(): PlaceUi {
    return PlaceUi(
        name = name,
        lat = lat,
        lng = lng
    )
}

fun PlaceDomain.toEntity(): PlaceEntity {
    return PlaceEntity(
        place = name,
        lat = lat,
        lng = lng
    )
}

fun PlaceEntity.toDomain(): PlaceDomain {
    return PlaceDomain(
        name = place,
        lat = lat,
        lng = lng
    )
}