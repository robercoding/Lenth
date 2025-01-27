package app.lenth.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.lenth.ui.utils.ButtonHeight
import app.lenth.ui.utils.ButtonRoundedCornerShape

@Composable
internal fun LenthPrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit,
    isLoading: Boolean = false,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(ButtonHeight),
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        shape = RoundedCornerShape(ButtonRoundedCornerShape),
    ) {
        Crossfade(targetState = isLoading) { isLoading ->
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(ButtonHeight),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp).align(Alignment.Center),
                        color = textColor,
                        strokeCap = StrokeCap.Round,
                        strokeWidth = 2.dp,
                    )
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().height(ButtonHeight),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = text,
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold,),
                        modifier = Modifier,
                    )
                }
            }
        }
    }
}

