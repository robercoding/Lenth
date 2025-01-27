package app.lenth.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun ExpandedImageOverlay(
    imageBitmap: ImageBitmap,
    hasCloseButton: Boolean = true,
    onClose: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.90f),
        contentAlignment = Alignment.Center,
    ) {
        Column {
            if (hasCloseButton) {
                Box(Modifier.fillMaxWidth().clickable { onClose() }, contentAlignment = Alignment.CenterEnd) {
                    IconButton(
                        onClick = onClose
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                        )
                    }
                }
            }
            Image(
                bitmap = imageBitmap,
                contentDescription = "Expanded Image",
                modifier = Modifier.aspectRatio(1f).fillMaxWidth().clip(RoundedCornerShape(16.dp)).clickable(interactionSource = null, indication = null, onClick = {}),
                contentScale = ContentScale.Crop,
            )
        }
    }
}
