package app.lenth.ui.search.optimalpathsheet

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

// @Composable
// fun CanvasWithCircleAndLine(
//     index: Int,
//     isNotEnd: Boolean,
// ) {
//     Box(
//         contentAlignment = Alignment.Center,
//         modifier = Modifier.size(40.dp), // Adjust size for circle and line
//     ) {
//         Canvas(modifier = Modifier.fillMaxSize()) {
//             // Draw the circle
//             drawCircle(
//                 color = Color.White,
//                 radius = size.minDimension / 2,
//                 style = Stroke(width = 2.dp.toPx()), // Make the circle an outline
//             )
//
//             // Draw the line below the circle
//             if (isNotEnd) { // Only draw the line if it's not the last item
//                 drawLine(
//                     color = Color.White,
//                     start = Offset(x = size.width / 2, y = size.height),
//                     end = Offset(x = size.width / 2, y = size.height + 40), // Adjust line length
//                     strokeWidth = 2.dp.toPx(),
//                 )
//             }
//         }
//
//         // Draw the index inside the circle
//             Text(
//                 text = "${index + 1}",
//                 color = Color.White, // Adjust text color for contrast
//                 style = MaterialTheme.typography.body2,
//             )
//     }
// }

@Composable
fun CanvasWithCircleAndLine(
    index: Int,
    isNotEnd: Boolean,
) {
    // val onSurfaceColor = MaterialTheme.colorScheme.onSurfaceVariant
    val onSurfaceColor =  MaterialTheme.colorScheme.onSurface
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(40.dp),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw the circle
            drawCircle(
                color = onSurfaceColor, // Use theme color
                radius = size.minDimension / 2,
                style = Stroke(width = 2.dp.toPx()), // Outline stroke
            )

            // Draw the line below the circle
            if (isNotEnd) {
                drawLine(
                    color = onSurfaceColor, // Subtle color for the line
                    start = Offset(x = size.width / 2, y = size.height),
                    end = Offset(x = size.width / 2, y = size.height + 40),
                    strokeWidth = 2.dp.toPx(),
                )
            }
        }

        // Draw the index inside the circle
        Text(
            text = "${index + 1}",
            color = onSurfaceColor, // Match the circle color
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

