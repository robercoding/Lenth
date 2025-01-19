package app.lenth.domain.models

data class OptimalRouteDomain(
    val id: Int? = null,
    val distance: Double,
    val list: List<String>
)