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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.lenth.ui.utils.FlagEmoji
import app.lenth.ui.utils.SemaphoreEmoji
import app.lenth.utils.formatToDistanceKmNoDecimals
import kotlinx.coroutines.launch
import lenth.composeapp.generated.resources.Res
import lenth.composeapp.generated.resources.tab_history_item_subtitle_distance
import lenth.composeapp.generated.resources.tab_history_item_subtitle_locations
import lenth.composeapp.generated.resources.tab_history_item_title_from_to
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CustomListItem(
    modifier: Modifier = Modifier,
    byteArray: ByteArray?,
    startPlace: String,
    endPlace: String,
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
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { imageBitmap?.let(onClickImage) }
                .background(Color.Red),
            contentAlignment = Alignment.Center,
        ) {
            imageBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = "Image",
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop,
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
            Text(
                text = "$SemaphoreEmoji $startPlace",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "$FlagEmoji $endPlace",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = stringResource(Res.string.tab_history_item_subtitle_locations, locations),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(Res.string.tab_history_item_subtitle_distance, distance.formatToDistanceKmNoDecimals()),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Navigate",
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}
