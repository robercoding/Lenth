package app.lenth.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.lenth.ui.LenthScreen
import lenth.composeapp.generated.resources.Res
import lenth.composeapp.generated.resources.app_title_design_your_travel
import lenth.composeapp.generated.resources.app_title_settings
import org.jetbrains.compose.resources.stringResource

@Composable
fun CustomTopAppBar(
    currentScreen: LenthScreen,
    navController: NavController,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    onSurfaceColor: Color = MaterialTheme.colorScheme.onSurface

) {
    val designYourTravelTitlte = stringResource(Res.string.app_title_design_your_travel)
    val settingsTitle = stringResource(Res.string.app_title_settings)
    val isSettingsScreen = currentScreen == LenthScreen.Settings
    val text = if (isSettingsScreen) settingsTitle else designYourTravelTitlte

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Left icon (chevron/back)
        AnimatedVisibility(
            visible = isSettingsScreen,
            enter = fadeIn() + expandIn(expandFrom = Alignment.Center),
            exit = fadeOut() + shrinkOut(shrinkTowards = Alignment.CenterStart),
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = Icons.Default.ChevronLeft.name,
                tint = onSurfaceColor,
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(percent = 50))
                    .clickable {
                        navController.popBackStack()
                    }
            )
        }

        AnimatedText(
            text = text,
            modifier = Modifier.weight(1f),
        ) { animatedText ->
            Text(
                text = animatedText,
                style = MaterialTheme.typography.headlineSmall,
                color = onSurfaceColor,
                textAlign = TextAlign.Left
            )
        }

        // Spacer(modifier = Modifier.weight(1f))

        // Right icon (settings)
        AnimatedVisibility(visible = !isSettingsScreen) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Settings",
                tint = onSurfaceColor,
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(percent = 50))
                    .clickable {
                        if (isSettingsScreen) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(LenthScreen.Settings.name)
                        }
                    }
            )
        }
    }
}
