package app.lenth.ui.utils

import co.touchlab.kermit.Logger
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openGoogleMaps(path: List<String>) {
    if (path.size < 2) {
        Logger.e("Error: You need at least two locations for a route.")
        return
    }

    // Encode the path to make it URL-compatible
    val formattedPath = path.map { it.replace(" ", "+").replace(",", "%2C") }

    // Extract the starting point, destination, and intermediate waypoints
    val start = formattedPath.first() // First location as the origin
    val destination = formattedPath.last() // Last location as the destination
    val waypoints = formattedPath.drop(1).dropLast(1).joinToString(separator = "%7C") // Intermediate stops (pipe-separated)

    // Construct the universal Google Maps URL
    val googleMapsUrl = if (waypoints.isNotEmpty()) {
        "https://www.google.com/maps/dir/?api=1&origin=$start&destination=$destination&waypoints=$waypoints&travelmode=driving"
    } else {
        "https://www.google.com/maps/dir/?api=1&origin=$start&destination=$destination&travelmode=driving"
    }

    // Open the URL using UIApplication
    val googleMapsNSURL = NSURL(string = googleMapsUrl)
    if (googleMapsNSURL != null) {
        Logger.i("Opening Google Maps with URL: $googleMapsUrl")
        UIApplication.sharedApplication.openURL(
            googleMapsNSURL,
            options = emptyMap<Any?, Any?>(),
            completionHandler = { success ->
                if (!success) {
                    Logger.e("Failed to open URL: $googleMapsUrl")
                }
            }
        )
    } else {
        Logger.e("Failed to construct Google Maps URL.")
    }
}




// actual fun openGoogleMaps(path: List<String>) {
//     // Example hardcoded coordinates for testing
//     val startLat = "37.7749"
//     val startLng = "-122.4194"
//     val destLat = "34.0522"
//     val destLng = "-118.2437"
//
//     // Construct Google Maps app URL
//     val googleMapsAppUrl = "comgooglemaps://?saddr=$startLat,$startLng&daddr=$destLat,$destLng&directionsmode=driving"
//
//     // Construct fallback browser URL
//     val browserUrl = "https://www.google.com/maps/dir/?api=1&origin=$startLat,$startLng&destination=$destLat,$destLng&travelmode=driving"
//
//     // Check if the Google Maps app is available
//     val googleMapsNSURL = NSURL(string = googleMapsAppUrl)
//     if (googleMapsNSURL != null && UIApplication.sharedApplication.canOpenURL(googleMapsNSURL)) {
//         Logger.i("Opening Google Maps app...")
//         UIApplication.sharedApplication.openURL(
//             googleMapsNSURL,
//             options = emptyMap<Any?, Any?>(),
//             completionHandler = { success ->
//                 if (!success) {
//                     Logger.e("Failed to open Google Maps app: $googleMapsAppUrl")
//                 }
//             }
//         )
//     } else {
//         Logger.i("Google Maps app not available, opening in browser...")
//         val browserNSURL = NSURL(string = browserUrl)
//         if (browserNSURL != null) {
//             UIApplication.sharedApplication.openURL(
//                 browserNSURL,
//                 options = emptyMap<Any?, Any?>(),
//                 completionHandler = { success ->
//                     if (!success) {
//                         Logger.e("Failed to open browser: $browserUrl")
//                     }
//                 }
//             )
//         } else {
//             Logger.e("Failed to construct browser URL.")
//         }
//     }
// }

// actual fun openGoogleMaps(path: List<String>) {
//     // // Construct the Google Maps URL with the given path
//     // val locations = path.joinToString(separator = "/") { it.replace(" ", "+") } // Replace spaces for URL compatibility
//     // val googleMapsUrl = "https://www.google.com/maps/dir/$locations"
//     //
//     // // Attempt to open the URL
//     // val url = NSURL(string = googleMapsUrl)
//     // if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
//     //     Logger.i("Oepning...: $googleMapsUrl")
//     //     UIApplication.sharedApplication.openURL(url)
//     // } else {
//     //     Logger.i("Cannot open Google Maps URL: $googleMapsUrl")
//     // }
//
//     // Construct Google Maps app URL
//     val googleMapsAppUrl = "comgooglemaps://?saddr=37.7749,-122.4194&daddr=34.0522,-118.2437&directionsmode=driving"
//
//     // Construct fallback browser URL
//     val browserUrl = "https://www.google.com/maps/dir/?api=1&origin=37.7749,-122.4194&destination=34.0522,-118.2437&travelmode=driving"
//
//     // Check if the Google Maps app is available
//     val googleMapsUrl = NSURL(string = googleMapsAppUrl)
//     if (googleMapsUrl != null && UIApplication.sharedApplication.canOpenURL(googleMapsUrl)) {
//         // Open Google Maps app
//         Logger.i("Opening Google Maps app...")
//         UIApplication.sharedApplication.openURL(googleMapsUrl)
//     } else {
//         // Open in the default browser
//         Logger.i("Oepn default browser")
//         val browserNSURL = NSURL(string = browserUrl)
//         if (browserNSURL != null) {
//             Logger.i("Opening Google Maps browser...")
//             UIApplication.sharedApplication.openURL(browserNSURL)
//         }
//     }
// }
