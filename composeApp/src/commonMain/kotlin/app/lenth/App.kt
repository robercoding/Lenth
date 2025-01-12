package app.lenth

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import app.lenth.data.preferences.DevelopmentPreference
import app.lenth.di.dataModule
import app.lenth.di.defaultModules
import app.lenth.di.networkModule
import app.lenth.di.platformModule
import app.lenth.di.repositoryModule
import app.lenth.di.shared.sharedDataModule
import app.lenth.di.useCaseModules
import app.lenth.di.viewModelModules
import app.lenth.ui.LenthScreen
import app.lenth.ui.theme.AppTheme
import app.lenth.ui.theme.UpdateStatusBarColor
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.dsl.KoinAppDeclaration

@Composable
@Preview
fun App(
    koinAppDeclaration: KoinAppDeclaration? = null,
    // isDarkMode: Boolean? = null
) {
    KoinApplication(
        application = {
            modules(defaultModules, networkModule, sharedDataModule, dataModule, repositoryModule, viewModelModules, useCaseModules, platformModule)
            koinAppDeclaration?.invoke(this)
        },
    ) {

        val developmentPreference = koinInject<DevelopmentPreference>()
        val currentSystemTheme = isSystemInDarkTheme()
        LaunchedEffect(Unit) {
            if(developmentPreference.getBooleanData(DevelopmentPreference.PREFERENCE_THEME_IS_DARK_MODE) == null) {
                developmentPreference.setData(DevelopmentPreference.PREFERENCE_THEME_IS_DARK_MODE, currentSystemTheme)
            }
        }
        val isDarkMode = developmentPreference.getBooleanFlow(DevelopmentPreference.PREFERENCE_THEME_IS_DARK_MODE).collectAsState(null).value

        if (isDarkMode != null) {
            AppTheme(darkTheme = isDarkMode) {
                LenthScreen()
            }
        }
    }
}