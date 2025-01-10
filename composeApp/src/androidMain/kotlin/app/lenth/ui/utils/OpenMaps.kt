package app.lenth.ui.utils

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity
import app.lenth.utils.activityContext
import app.lenth.utils.applicationContext

@Composable
actual fun openMaps(lat: Double, lon: Double) {
    val context = LocalContext.current
    val directionsBuilder = Uri.Builder()
        .scheme("https")
        .authority("www.google.com")
        .appendPath("maps")
        .appendPath("dir")
        .appendPath("")
        .appendQueryParameter("api", "1")
        .appendQueryParameter("origin", "28.7041" + "," + "77.1025")
        .appendQueryParameter("destination", "18.5204" + "," + "73.8567")

    startActivity(context, Intent(Intent.ACTION_VIEW, directionsBuilder.build()), null)
}



actual fun openGoogleMaps(path: List<String>) {
    val context = activityContext
    // Build the Google Maps URL with the route
    // val context = applicationContext
    val locations = path.joinToString(separator = "/") { Uri.encode(it) }
    val googleMapsUrl = "https://www.google.com/maps/dir/$locations"

    // Create an intent to open Google Maps
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleMapsUrl))
    intent.setPackage("com.google.android.apps.maps") // Ensure it opens in the Google Maps app

    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        // Fallback: Open in a web browser if Google Maps is not installed
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(googleMapsUrl))
        context.startActivity(browserIntent)
    }
}
