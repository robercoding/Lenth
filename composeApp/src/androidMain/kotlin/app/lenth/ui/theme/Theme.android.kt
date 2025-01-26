package app.lenth.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.view.Window
import android.view.WindowInsets
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.lenth.data.preferences.DevelopmentPreference
import co.touchlab.kermit.Logger
import org.koin.compose.koinInject

@Composable
actual fun UpdateStatusBarColor(useDarkIcons: Boolean) {
    val developmentPreference = koinInject<DevelopmentPreference>()
    var isSystemInDarkTheme by remember { mutableStateOf<Boolean?>(null) }
    val currentSystemThemeIsDarkMode = isSystemInDarkTheme()
    val isDarkMode by developmentPreference.getBooleanFlow(DevelopmentPreference.PREFERENCE_THEME_IS_DARK_MODE).collectAsStateWithLifecycle(null)

    val view = LocalView.current
    val context = LocalContext.current.findActivity()

    if (context != null) {
        Logger.i("Context is an Activity")
        val window = context.window
        val windowInsetsController = ViewCompat.getWindowInsetsController(view)

        // Set light or dark status bar icons
        // If true then light icons, else dark icons
        Logger.i("UpdateStatusBarColor: isDarkMode: $isDarkMode")
        windowInsetsController?.isAppearanceLightStatusBars = isDarkMode?.not() ?: !currentSystemThemeIsDarkMode
    } else {
        Logger.i("UpdateStatusBarColor: Context is not an Activity")
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
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
