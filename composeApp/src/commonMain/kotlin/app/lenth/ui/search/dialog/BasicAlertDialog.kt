package app.lenth.ui.search.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BasicAlertDialog(
    isAlertDialogVisible: Boolean,
    title: String,
    message: String,
    dismissButtonText: String,
    confirmButtonText: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
) {
    AnimatedVisibility(
        isAlertDialogVisible,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
        exit = fadeOut(animationSpec = tween(125)) + shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(125)
        ),
    ) {
        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            title = {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                )
            },
            text = {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            confirmButton = {
                Button(
                    onClick = { onConfirm() },
                    shape = MaterialTheme.shapes.medium, // Use consistent shape
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = confirmButtonText,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismissRequest() },
                    shape = MaterialTheme.shapes.medium, // Same shape as confirmButton
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(dismissButtonText, style = MaterialTheme.typography.bodyMedium)
                }
            },
            shape = MaterialTheme.shapes.large, // Dialog shape
            containerColor = MaterialTheme.colorScheme.surface,
        )
    }
}