package app.lenth.ui.search.filter

import app.lenth.data.SearchTypeDomain

fun SearchTypeUi.toDomain(): SearchTypeDomain {
    return when (this) {
        SearchTypeUi.ALL -> SearchTypeDomain.ALL
        SearchTypeUi.ADDRESSES -> SearchTypeDomain.ADDRESSES
        SearchTypeUi.CITIES -> SearchTypeDomain.CITIES
        SearchTypeUi.REGIONS -> SearchTypeDomain.REGIONS
        SearchTypeUi.ESTABLISHMENTS -> SearchTypeDomain.ESTABLISHMENTS
        SearchTypeUi.GEOCODE -> SearchTypeDomain.GEOCODE
    }
}
