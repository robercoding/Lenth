package app.lenth.ui.settings

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import lenth.composeapp.generated.resources.Res
import lenth.composeapp.generated.resources.settings_current_language_subtitle
import lenth.composeapp.generated.resources.settings_current_language_title
import lenth.composeapp.generated.resources.settings_current_theme_subtitle
import lenth.composeapp.generated.resources.settings_current_theme_title_dark
import lenth.composeapp.generated.resources.settings_current_theme_title_light
import org.jetbrains.compose.resources.stringResource

private val TileMarginBetween = 8.dp
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onClickChangeLanguage: () -> Unit,
    settingsViewModel: SettingsViewModel,
) {
    // trackScreenView(name = "SettingsScreen")
    val state by settingsViewModel.state.collectAsStateWithLifecycle()

    Surface(modifier = modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.statusBars)
        .background(MaterialTheme.colorScheme.surface),
    ) {
        SettingsComposable(
            onClickChangeLanguage = onClickChangeLanguage,
            state = state,
            onClickChangeTheme = { settingsViewModel.onClickChangeTheme(it) },
        )
    }
}

// pass state to this composable
@Composable
private fun SettingsComposable(
    state: SettingsState?,
    onClickChangeLanguage: () -> Unit,
    onClickChangeTheme: (Boolean) -> Unit,
) {
    val scrollableState = rememberScrollState()
    val isDarkMode = state?.appConfiguration?.isDarkMode ?: isSystemInDarkTheme()

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 4.dp)
                .verticalScroll(scrollableState),
        ) {
            PreferencesSection(
                modifier = Modifier.align(Alignment.Start),
                isDarkMode = isDarkMode,
                onClickLanguage = { onClickChangeLanguage() },
                onClickChangeTheme = onClickChangeTheme,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = state?.appConfiguration?.version ?: "Unknown version",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray,
            )
        }
    }
}

@Composable
private fun Header(modifier: Modifier, text: String, icon: ImageVector) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = rememberVectorPainter(image = icon), contentDescription = Icons.Default.Palette.name)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Left,
        )
    }
}


@Composable
private fun PreferencesSection(
    modifier: Modifier,
    isDarkMode: Boolean,
    onClickLanguage: () -> Unit,
    onClickChangeTheme: (Boolean) -> Unit,
) {
    Column(modifier = modifier) {
        // Header(modifier = Modifier, text = stringResource(resource = SharedRes.strings.settings_header_preferences), icon = Icons.Default.Star)
        // Header(modifier = Modifier, text = "Preferences", icon = Icons.Default.Star)
        // Spacer(modifier = Modifier.height(4.dp))
    ListItem(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable(
                    onClick = {
                        onClickLanguage()
                    },
                ),
            tonalElevation = 2.dp,
            leadingContent = {
                Icon(painter = rememberVectorPainter(image = Icons.Default.Language), contentDescription = Icons.Default.Language.name)
            },
            headlineContent = {
                Text(
                    text = stringResource(Res.string.settings_current_language_title),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            supportingContent = {
                Text(
                    text = stringResource(Res.string.settings_current_language_subtitle),
                    // text = stringResource(resource = SharedRes.strings.settings_preferences_section_language_subtitle),
                    style = MaterialTheme.typography.labelSmall,
                )
            },
        )
        Spacer(modifier = Modifier.height(TileMarginBetween))
        ListItem(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable(
                    onClick = { onClickChangeTheme(isDarkMode) },
                ),
            tonalElevation = 2.dp,
            leadingContent = {
                Crossfade(targetState = isDarkMode) { isDarkMode ->
                    if (isDarkMode) {
                        Icon(painter = rememberVectorPainter(image = Icons.Default.DarkMode), contentDescription = Icons.Default.DarkMode.name)
                    } else {
                        Icon(painter = rememberVectorPainter(image = Icons.Default.LightMode), contentDescription = Icons.Default.LightMode.name)
                    }
                }
            },
            headlineContent = {
                val resource = if(isDarkMode) Res.string.settings_current_theme_title_dark else Res.string.settings_current_theme_title_light
                Text(
                    // text = "Mode",
                    text = stringResource(resource = resource),
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            supportingContent = {
                Text(
                    // text = "Tap to change theme",
                    text = stringResource(resource = Res.string.settings_current_theme_subtitle),
                    style = MaterialTheme.typography.labelSmall,
                )
            },
        )
    }
}