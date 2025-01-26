package app.lenth.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import app.lenth.utils.formatToDistanceKmNoDecimals
import co.touchlab.kermit.Logger
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CustomListItem(
    modifier: Modifier = Modifier,
    byteArray: ByteArray?,
    title: String,
    locations: Int,
    distance: Double,
    onClickImage: (ImageBitmap) -> Unit,
    onClick: () -> Unit,
) {

    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(byteArray) {
        if (imageBitmap != null || byteArray == null) return@LaunchedEffect
        launch {
            imageBitmap = byteArray.decodeToImageBitmap()
        }
        // imageBitmap = null
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Leading Content

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { imageBitmap?.let(onClickImage) }
                .background(Color.Red),
            contentAlignment = Alignment.Center,
        ) {
            imageBitmap?.let {
                Logger.i("Paint!: imageBitmap: $it")
                // Image(
                //     bitmap = it,
                //     contentDescription = null,
                //     // modifier = Modifier.clip(RoundedCornerShape(16.dp)).size(70.dp),
                // )
                Image(
                    bitmap= it,
                    contentDescription = "Image",
                    modifier = Modifier.size(70.dp),
                    contentScale = ContentScale.Crop,
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Main Content
        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = "Locations: $locations",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Distance: ${distance.formatToDistanceKmNoDecimals()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Trailing Content
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}
