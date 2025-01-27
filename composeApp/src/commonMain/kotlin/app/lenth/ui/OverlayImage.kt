package app.lenth.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.lenth.ui.components.ExpandedImageOverlay
import app.lenth.ui.utils.BackHandler
import kotlinx.coroutines.delay

@Composable
fun OverlayImage(
    showOverlay: Boolean,
    selectedItem: ImageBitmap?,
    onDismiss: () -> Unit,
) {

    // We need dialog because we might be inside a BottomSheet and we want to render the content on top of it.
    // https://blog.sanskar10100.dev/drawing-custom-alerts-on-top-of-bottom-sheets-in-jetpack-compose
    val showDialog by remember(showOverlay) {
        mutableStateOf(showOverlay)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (showDialog) {
            Dialog(
                onDismissRequest = onDismiss,
                properties = DialogProperties(
                    dismissOnClickOutside = false,
                    dismissOnBackPress = true,
                    usePlatformDefaultWidth = false,
                ),
            ) {
                // Workaround to generate the animation. The Dialog itself is not animated on iOS side.
                var doAnimation by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) {
                    doAnimation = showOverlay
                }
                AnimatedVisibility(
                    visible = showOverlay && doAnimation,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 5 }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 5 }),
                    modifier = Modifier.fillMaxSize().clickable(interactionSource = null, indication = null) { doAnimation = false },
                ) {
                    DisposableEffect(Unit) {
                        onDispose { onDismiss() }
                    }
                    selectedItem?.let {
                        ExpandedImageOverlay(
                            imageBitmap = it,
                            onClose = { doAnimation = false },
                        )
                    }
                }
            }
        }
    }
}