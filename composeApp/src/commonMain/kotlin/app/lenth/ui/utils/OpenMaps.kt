package app.lenth.ui.utils

import androidx.compose.runtime.Composable

@Composable
expect fun openMaps(lat: Double, lon: Double)

expect fun openGoogleMaps(path: List<String>)