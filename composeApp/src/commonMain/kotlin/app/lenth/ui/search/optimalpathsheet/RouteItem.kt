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
        // Draw circle with index inside and line below
        CanvasWithCircleAndLine(index = index, isStart = isStart, isEnd = isEnd)

        Spacer(modifier = Modifier.width(8.dp))

        // Display the place text
        Text(
            text = place,
            color = MaterialTheme.colorScheme.onSurface, // Updated to use theme color
            style = MaterialTheme.typography.bodyMedium,
        )

        // Spacer(modifier = Modifier.width(8.dp))

        // // Start and End Indicators
        // if (isStart) {
        //     Text(
        //         text = "🏁"
        //
        //     )
        // } else if (isEnd) {
        //     Text(text = "🏁")
        // }
    }
}

// @Composable
// fun RouteItem(
//     index: Int,
//     location: String,
//     isStart: Boolean = false,
//     isEnd: Boolean = false
// ) {
//     Row(
//         modifier = Modifier
//             .fillMaxWidth()
//             .padding(vertical = 8.dp, horizontal = 16.dp),
//         verticalAlignment = Alignment.CenterVertically
//     ) {
//         Box(
//             modifier = Modifier
//                 .size(36.dp)
//                 .background(MaterialTheme.colorScheme.primary, CircleShape),
//             contentAlignment = Alignment.Center
//         ) {
//             Text(
//                 text = (index + 1).toString(),
//                 color = MaterialTheme.colorScheme.onPrimary,
//                 style = MaterialTheme.typography.bodyMedium
//             )
//         }
//         Spacer(modifier = Modifier.width(16.dp))
//         Text(
//             text = location,
//             style = MaterialTheme.typography.bodyMedium,
//             color = MaterialTheme.colorScheme.onSurface,
//             modifier = Modifier.weight(1f)
//         )
//         if (isStart) {
//             Icon(Icons.Default.Flag, contentDescription = "Start", tint = MaterialTheme.colorScheme.primary)
//         } else if (isEnd) {
//             Icon(Icons.Default.Flag, contentDescription = "End", tint = MaterialTheme.colorScheme.secondary)
//         }
//     }
// }
