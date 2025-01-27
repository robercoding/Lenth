package app.lenth.ui.components.text

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun TextButtonAction(
    text: String,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    TextButton(
        enabled = isEnabled,
        onClick = onClick,
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp, pressedElevation = 0.dp, hoveredElevation = 0.dp, focusedElevation = 0.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
