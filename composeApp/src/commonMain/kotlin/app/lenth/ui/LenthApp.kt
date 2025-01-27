package app.lenth.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.lenth.ui.components.CustomTopAppBar
import app.lenth.ui.screens.start.LenthStart
import app.lenth.ui.settings.SettingsScreen
import app.lenth.utils.openLanguageSettings
import co.touchlab.kermit.Logger
import org.koin.compose.viewmodel.koinViewModel

/**
 * enum values that represent the screens in the app
 */
enum class LenthScreen(
    // val title: StringResource
) {
    Start,
    Settings
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LenthScreen(
    navController: NavHostController = rememberNavController(),
) {
    var selectedTab by remember { mutableStateOf(0) }

    val searchViewModel = koinViewModel<SearchViewModel>()

    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = LenthScreen.valueOf(
        backStackEntry?.destination?.route ?: LenthScreen.Start.name,
    )

    val backgroundColor by rememberUpdatedState(MaterialTheme.colorScheme.background)
    val onSurfaceColor by rememberUpdatedState(MaterialTheme.colorScheme.onSurface)

    var showOverlay by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<ImageBitmap?>(null) } // To track the selected image for animation

    Surface(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        color = backgroundColor,
    ) {
        Scaffold(
            modifier = Modifier.statusBarsPadding(),
            topBar = {},
            backgroundColor = backgroundColor,
        ) { paddingValues ->

            Column {
                CustomTopAppBar(
                    currentScreen = currentScreen,
                    navController = navController,
                    backgroundColor = backgroundColor,
                    onSurfaceColor = onSurfaceColor,
                )

                NavHost(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    navController = navController,
                    startDestination = LenthScreen.Start.name,
                    builder = {
                        composable(LenthScreen.Start.name) {
                            LenthStart(
                                selectedTab = selectedTab,
                                onTabSelected = { selectedTab = it },
                                searchViewModel = searchViewModel,
                                onClickImage = { imageBitmap ->
                                    selectedItem = imageBitmap
                                    showOverlay = true
                                },
                            )
                        }

                        composable(LenthScreen.Settings.name) {
                            // Add the Settings screen content here
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text("Settings Content", color = MaterialTheme.colorScheme.onSurface)
                            }

                            SettingsScreen(
                                modifier = Modifier.fillMaxSize(),
                                onClickChangeLanguage = { openLanguageSettings() },
                                settingsViewModel = koinViewModel(),
                            )
                        }
                    },
                )
            }
        }

        OverlayImage(
            showOverlay = showOverlay,
            selectedItem = selectedItem,
            onDismiss = {
                showOverlay = false
                selectedItem = null
                        },
        )
    }
}

fun Modifier.tabIndicatorOffset(
    currentTabPosition: TabPositionText,
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = currentTabPosition
    },
) {
    val currentTabWidth by animateDpAsState(
        targetValue = currentTabPosition.width,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
    )
    val indicatorOffset by animateDpAsState(
        targetValue = currentTabPosition.left,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
    )
    Logger.i(
        tag = "SASD", messageString = "Current tab width: $currentTabWidth and offset: $indicatorOffset",
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset { IntOffset(x = 100, y = 0) }
        .width(currentTabWidth)
}

data class TabPositionText(
    val width: Dp,
    val left: Dp,
)
