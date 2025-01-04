package app.lenth.ui.utils

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat.startActivity

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