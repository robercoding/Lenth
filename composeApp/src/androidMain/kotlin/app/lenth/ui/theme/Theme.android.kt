package app.lenth.ui.theme

import android.app.Activity
import android.os.Build
import android.view.Window
import android.view.WindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

@Composable
actual fun UpdateStatusBarColor(useDarkIcons: Boolean) {
    val view = LocalView.current
    val context = LocalContext.current

    if (context is Activity) {
        val window = context.window
        val windowInsetsController = ViewCompat.getWindowInsetsController(view)

        // Set light or dark status bar icons
        windowInsetsController?.isAppearanceLightStatusBars = useDarkIcons
    }
}

fun setStatusBarColor(window: Window, color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) { // Android 15+
        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
            view.setBackgroundColor(color)

            // Adjust padding to avoid overlap
            view.setPadding(0, statusBarInsets.top, 0, 0)
            insets
        }
    } else {
        // For Android 14 and below
        window.statusBarColor = color
    }
}
