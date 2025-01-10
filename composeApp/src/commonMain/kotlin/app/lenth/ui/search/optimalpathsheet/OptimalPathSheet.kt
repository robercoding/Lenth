package app.lenth.ui.search.optimalpathsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.lenth.domain.MinimumCostPath
import app.lenth.ui.utils.openGoogleMaps

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptimalPathSheet(
    minimumCostPath: MinimumCostPath?,
    onDismissMinimumCostPath: () -> Unit,
) {
    val stateSheet = rememberModalBottomSheetState()
    val showBottomSheet by remember(minimumCostPath) {
        mutableStateOf(minimumCostPath != null)
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface),
                ) {
                    // Content Column
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    ) {
                        // Title
                        Text(
                            text = "Optimal Route",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                        )
                        Spacer(Modifier.height(8.dp))

                        // Summary Card
                        minimumCostPath?.let {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp)) {
                                SummaryCard(modifier = Modifier.weight(1f),totalDistance = it.cost)
                                Spacer(modifier = Modifier.width(16.dp))
                                FloatingActionButton(
                                    onClick = { openGoogleMaps(it.path) },
                                    modifier = Modifier
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Map,
                                        contentDescription = "Open in Google Maps",
                                    )
                                }
                            }

                        }

                        // Route List
                        minimumCostPath?.let {
                            it.path.forEachIndexed { index, place ->
                                RouteItem(
                                    index = index,
                                    place = place,
                                    isStart = index == 0,
                                    isEnd = index == it.path.size - 1,
                                )
                            }
                        }
                    }

                }
            },
            sheetState = stateSheet,
            containerColor = MaterialTheme.colorScheme.surface,
            onDismissRequest = {
                onDismissMinimumCostPath()
            },
        )
    }
}

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
        CanvasWithCircleAndLine(index = index, isNotEnd = !isEnd)

        Spacer(modifier = Modifier.width(8.dp))

        // Display the place text
        Text(
            text = place,
            color = MaterialTheme.colorScheme.onSurface, // Updated to use theme color
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Start and End Indicators
        if (isStart) {
            Icon(
                imageVector = Icons.Default.Flag,
                contentDescription = "Start point",
                tint = MaterialTheme.colorScheme.primary, // Start icon color
                modifier = Modifier.size(20.dp),
            )
        } else if (isEnd) {
            Icon(
                imageVector = Icons.Default.Flag,
                contentDescription = "End point",
                tint = MaterialTheme.colorScheme.secondary, // End icon color
                modifier = Modifier.size(20.dp),
            )
        }
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
