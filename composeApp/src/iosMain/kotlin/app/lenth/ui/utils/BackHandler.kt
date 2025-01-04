package app.lenth.ui.utils

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(isEnabled: Boolean, onBack: () -> Unit) {
 // Can't find a way to intercept back press on iOS
}