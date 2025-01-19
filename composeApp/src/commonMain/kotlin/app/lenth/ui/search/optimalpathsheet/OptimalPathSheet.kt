package app.lenth.ui.search.optimalpathsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import app.lenth.domain.MinimumCostPath
import app.lenth.ui.components.LenthPrimaryButton
import app.lenth.ui.history.models.OptimalRouteUi
import app.lenth.ui.theme.ActionBlue
import app.lenth.ui.theme.OnActionBlue
import app.lenth.ui.utils.openGoogleMaps
import lenth.composeapp.generated.resources.Res
import lenth.composeapp.generated.resources.tab_search_sheet_optimal_route_action_open_google_maps
import lenth.composeapp.generated.resources.tab_search_sheet_optimal_route_title
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptimalPathSheet(
    optimalRouteUi: OptimalRouteUi?,
    onDismissMinimumCostPath: () -> Unit,
) {
    val stateSheet = rememberModalBottomSheetState()
    val showBottomSheet by remember(optimalRouteUi) {
        mutableStateOf(optimalRouteUi != null)
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
                            text = stringResource(Res.string.tab_search_sheet_optimal_route_title),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                        )
                        Spacer(Modifier.height(8.dp))

                        // Summary Card
                        optimalRouteUi?.let {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp)) {
                                SummaryCard(modifier = Modifier, totalDistance = it.distance)
                                Spacer(modifier = Modifier.height(8.dp))
                                LenthPrimaryButton(
                                    text = stringResource(Res.string.tab_search_sheet_optimal_route_action_open_google_maps),
                                    textColor = OnActionBlue,
                                    backgroundColor = ActionBlue,
                                    onClick = { openGoogleMaps(it.path) },
                                )
                            }
                        }

                        // Route List

                        optimalRouteUi?.let {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                itemsIndexed(it.path, key = { index, place -> place }) { index, place ->
                                    RouteItem(
                                        index = index,
                                        place = place,
                                        isStart = index == 0,
                                        isEnd = index == it.path.size - 1,
                                    )
                                }
                            }
                            //     it.path.forEachIndexed { index, place ->
                            //         RouteItem(
                            //             index = index,
                            //             place = place,
                            //             isStart = index == 0,
                            //             isEnd = index == it.path.size - 1,
                            //         )
                            //     }
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
