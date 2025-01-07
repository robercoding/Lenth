package app.lenth.ui.search.filter

import androidx.compose.runtime.Stable

@Stable
enum class SearchTypeUi(val displayName: String) {
    ALL("All"),
    ADDRESSES("Addresses"),
    CITIES("Cities"),
    REGIONS("Regions"),
    ESTABLISHMENTS("Establishments"),
    GEOCODE("Geocode");
}
