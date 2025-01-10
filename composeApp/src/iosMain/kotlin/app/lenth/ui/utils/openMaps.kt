package app.lenth.ui.utils

import androidx.compose.runtime.Composable
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Composable
actual fun openMaps(lat: Double, lon: Double) {
}

actual fun openGoogleMaps(path: List<String>) {
    // Construct the Google Maps URL with the given path
    val locations = path.joinToString(separator = "/") { it.replace(" ", "+") } // Replace spaces for URL compatibility
    val googleMapsUrl = "https://www.google.com/maps/dir/$locations"

    // Attempt to open the URL
    val url = NSURL(string = googleMapsUrl)
    if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
        UIApplication.sharedApplication.openURL(url)
    } else {
        println("Cannot open Google Maps URL: $googleMapsUrl")
    }
}
