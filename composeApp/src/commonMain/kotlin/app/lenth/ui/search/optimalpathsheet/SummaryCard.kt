package app.lenth.ui.search.optimalpathsheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import app.lenth.utils.formatToDistanceKm
import app.lenth.utils.formatToDistanceKmNoDecimals
import kotlin.math.pow
import kotlin.math.round
import lenth.composeapp.generated.resources.Res
import lenth.composeapp.generated.resources.tab_search_sheet_optimal_route_total_distance
import org.jetbrains.compose.resources.stringResource

@Composable
fun SummaryCard(
    modifier: Modifier = Modifier,
    totalDistance: Double,
    imageBitmap: ImageBitmap? = null,
    onClickImage: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Total Distance
                Text(
                    text = stringResource(Res.string.tab_search_sheet_optimal_route_total_distance),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = totalDistance.formatToDistanceKmNoDecimals(), // Display with 2 decimals
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            imageBitmap?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    modifier = Modifier.clip(RoundedCornerShape(16.dp)).size(70.dp).clickable { onClickImage() },
                )
            }


            // Estimated Time (Commented out for now)
            /*
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Estimated Time",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = estimatedTime,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            */
        }
    }
}
