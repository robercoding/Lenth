package app.lenth.ui.search.optimalpathsheet

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RouteItem(
    index: Int,
    place: String,
    isStart: Boolean = false,
    isEnd: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp),
    ) {
        CanvasWithCircleAndLine(index = index, isStart = isStart, isEnd = isEnd)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = place,
            color = MaterialTheme.colorScheme.onSurface, // Updated to use theme color
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
