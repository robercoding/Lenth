package app.lenth.ui.search.optimalpathsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
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
                Box(modifier = Modifier.fillMaxWidth().background(Color.DarkGray), contentAlignment = Alignment.CenterStart) {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        minimumCostPath?.let {
                            it.path.forEachIndexed { index, place ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                ) {
                                    // Draw circle with index inside and line below
                                    CanvasWithCircleAndLine(index = index, totalItems = it.path.size)

                                    Spacer(modifier = Modifier.width(8.dp))

                                    // Display the place text
                                    Text(
                                        text = place,
                                        color = Color.White,
                                        style = MaterialTheme.typography.body1,
                                    )
                                }
                            }
                        }
                    }
                }
            },
            sheetState = stateSheet,
            containerColor = Color.DarkGray,
            onDismissRequest = {
                onDismissMinimumCostPath()
            },
        )
    }
}