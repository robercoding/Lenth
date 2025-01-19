package app.lenth.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun ExpandedImageOverlay(
    imageUrl: String,
    onClose: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .clickable { onClose() },
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Expanded Image",
            modifier = Modifier.aspectRatio(1f).fillMaxWidth().clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop,
        )
    }
}
