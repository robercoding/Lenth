package app.lenth.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.lenth.Platform
import app.lenth.data.preferences.DevelopmentPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    platform: Platform,
    private val developmentPreference: DevelopmentPreference,
) : ViewModel() {


    internal val state: StateFlow<SettingsState?> =  developmentPreference
        .getBooleanFlow(DevelopmentPreference.PREFERENCE_THEME_IS_DARK_MODE)
        .mapLatest {
            SettingsState(
                appConfiguration = AppConfiguration(
                    language = mapLanguage(platform.language),
                    version = platform.version,
                    platformType = platform.type,
                    isDarkMode = it ?: false,
                )
            )
        }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SettingsState(
            appConfiguration = AppConfiguration(
                language = mapLanguage(platform.language),
                version = platform.version,
                platformType = Platform.Type.ANDROID,
                isDarkMode = developmentPreference.getData(DevelopmentPreference.PREFERENCE_THEME_IS_DARK_MODE) ?: false
            )
        )
    )

    fun onClickChangeTheme(isDarkMode: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            developmentPreference.setData(DevelopmentPreference.PREFERENCE_THEME_IS_DARK_MODE, !isDarkMode)
        }
    }
}

private fun mapLanguage(language: String): String {
    return when (language) {
        "en" -> "English"
        "es" -> "Español"
        "ca" -> "Català"
        "gl" -> "Galego"
        "eu" -> "Euskara"
        else -> language
    }
}

data class AppConfiguration(
    val language: String,
    val version: String,
    val isDarkMode: Boolean,
    val platformType: Platform.Type
)

data class SettingsState(
    val appConfiguration: AppConfiguration?,
)