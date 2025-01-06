package app.lenth.ui.search.optimalpathsheet

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun CanvasWithCircleAndLine(index: Int, totalItems: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(40.dp), // Adjust size for circle and line
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw the circle
            drawCircle(
                color = Color.White,
                radius = size.minDimension / 2,
                style = Stroke(width = 2.dp.toPx()), // Make the circle an outline
            )

            // Draw the line below the circle
            if (index < totalItems - 1) { // Only draw the line if it's not the last item
                drawLine(
                    color = Color.White,
                    start = Offset(x = size.width / 2, y = size.height),
                    end = Offset(x = size.width / 2, y = size.height + 40), // Adjust line length
                    strokeWidth = 2.dp.toPx(),
                )
            }
        }

        // Draw the index inside the circle
            Text(
                text = "${index + 1}",
                color = Color.White, // Adjust text color for contrast
                style = MaterialTheme.typography.body2,
            )
    }
}