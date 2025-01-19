package app.lenth.ui.history.models

data class OptimalRouteUi(val id: Int = -1, val distance: Double, val path: List<String>) {
    private val start = path.first()
    private val end = path.last()
    // private val image = "https://maps.googleapis.com/maps/api/staticmap?size=600x300&maptype=roadmap&path=color:0x0000ff|weight:5|$start|$end"
    // private val imageStart = "https://maps.googleapis.com/maps/api/staticmap?size=600x300&maptype=roadmap&markers=color:blue|$start"
    val imageStart = "https://maps.googleapis.com/maps/api/staticmap?center=$start&zoom=12&size=600x400&key=AIzaSyAzZxcgwEhBH4S6WPTE9FNfPHqeAS9EqqY"
}