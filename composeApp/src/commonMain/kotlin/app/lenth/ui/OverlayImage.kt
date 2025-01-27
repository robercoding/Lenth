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

@Composable
fun OverlayImage(
    showOverlay: Boolean,
    selectedItem: ImageBitmap?,
    onDismiss: () -> Unit,
    onFinishAnimation: () -> Unit,
) {

    val showDialog by remember(showOverlay, selectedItem) {
        val boolean = showOverlay || selectedItem != null
        mutableStateOf(boolean)
    }

    var currentState by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(showOverlay) {
        currentState = showOverlay
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (showDialog) {
            Dialog(
                onDismissRequest = onDismiss,
                properties = DialogProperties(
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true,
                    usePlatformDefaultWidth = false,
                ),
            ) {
                AnimatedVisibility(
                    visible = showOverlay && currentState,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 5 }),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 5 }),
                    modifier = Modifier.fillMaxSize().clickable(interactionSource = null, indication = null) { onDismiss() },
                ) {
                    DisposableEffect(Unit) {
                        onDispose {
                            onFinishAnimation()
                        }
                    }
                    selectedItem?.let {
                        ExpandedImageOverlay(
                            imageBitmap = it,
                            onClose = {
                                onDismiss()
                            },
                        )
                    }
                }
            }
        }
    }
}