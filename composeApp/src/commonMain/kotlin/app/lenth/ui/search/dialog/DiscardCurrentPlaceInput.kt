package app.lenth.ui.search.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DiscardCurrentPlaceInput(
    isDiscardCurrentInputAlertDialogVisible: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmDiscard: () -> Unit,
) {
    AnimatedVisibility(
        isDiscardCurrentInputAlertDialogVisible,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
        exit = fadeOut(animationSpec = tween(125)) + shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(125)),
    ) {
        androidx.compose.material.AlertDialog(
            onDismissRequest = { onDismissRequest() },
            title = { Text(
                text = "Discard current place?",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
            ) },
            text = { Text(
                text = "You haven't selected any place from the list. Do you want to discard the current place?",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
            ) },
            confirmButton = {
                Button(
                    onClick = { onConfirmDiscard() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                ) {
                    Text(
                        "Discard",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { onDismissRequest() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text("Cancel", style = MaterialTheme.typography.bodyMedium)
                }
            },
            shape = RoundedCornerShape(16.dp),
            backgroundColor = Color.DarkGray,
        )
    }
}