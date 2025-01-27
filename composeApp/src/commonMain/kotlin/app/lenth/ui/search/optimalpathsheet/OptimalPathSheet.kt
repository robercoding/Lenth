package app.lenth.ui.search.optimalpathsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import app.lenth.ui.OverlayImage
import app.lenth.ui.components.DeleteIconButton
import app.lenth.ui.components.LenthPrimaryButton
import app.lenth.ui.history.models.OptimalRouteUi
import app.lenth.ui.search.dialog.ClearOptimalRouteAlertDialog
import app.lenth.ui.theme.ActionBlue
import app.lenth.ui.theme.OnActionBlue
import app.lenth.ui.theme.UpdateStatusBarColor
import app.lenth.ui.utils.openGoogleMaps
import kotlinx.coroutines.launch
import lenth.composeapp.generated.resources.Res
import lenth.composeapp.generated.resources.sheet_optimal_route_action_open_google_maps
import lenth.composeapp.generated.resources.sheet_optimal_route_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun OptimalPathSheet(
    optimalRouteUi: OptimalRouteUi?,
    onDismissMinimumCostPath: () -> Unit,
    onClickDelete: (Int) -> Unit,
) {
    val stateSheet = rememberModalBottomSheetState()
    val showBottomSheet by remember(optimalRouteUi) { mutableStateOf(optimalRouteUi != null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    var image by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(optimalRouteUi?.mapImage) {
        if (optimalRouteUi == null) {
            image = null
            return@LaunchedEffect
        }
        if (image != null) return@LaunchedEffect
        launch { image = optimalRouteUi.mapImage?.decodeToImageBitmap() }
    }

    var showOverlay by remember { mutableStateOf(false) }

    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.statusBarsPadding(),
            contentWindowInsets = { BottomSheetDefaults.windowInsets.only(WindowInsetsSides.Top) },
            content = {
                UpdateStatusBarColor(useDarkIcons = false)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    ) {
                        // Title
                        Text(
                            text = stringResource(Res.string.sheet_optimal_route_title),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                        )
                        Spacer(Modifier.height(8.dp))
                        // Summary Card
                        optimalRouteUi?.let {
                            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 0.dp, vertical = 8.dp)) {
                                SummaryCard(
                                    modifier = Modifier, totalDistance = it.distance, imageBitmap = image,
                                    onClickImage = {
                                        showOverlay = true
                                        // image?.let(onClickImage)
                                    },
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    LenthPrimaryButton(
                                        modifier = Modifier.weight(1f),
                                        text = stringResource(Res.string.sheet_optimal_route_action_open_google_maps),
                                        textColor = OnActionBlue,
                                        backgroundColor = ActionBlue,
                                        onClick = { openGoogleMaps(it.path.map { it.name }) },
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    DeleteIconButton(onClick = { showDeleteDialog = true })
                                }
                            }
                        }

                        // Route List

                        optimalRouteUi?.let {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                itemsIndexed(it.path, key = { _, place -> place.name }) { index, place ->
                                    RouteItem(
                                        index = index,
                                        place = place.name,
                                        isStart = index == 0,
                                        isEnd = index == it.path.size - 1,
                                    )
                                }
                            }
                        }
                    }

                    OverlayImage(
                        showOverlay = showOverlay,
                        selectedItem = image,
                        onDismiss = { showOverlay = false },
                    )

                    ClearOptimalRouteAlertDialog(
                        isDeleteOptimalRouteVisible = showDeleteDialog,
                        onDismissDelete = { showDeleteDialog = false },
                        onConfirmDelete = {
                            optimalRouteUi?.let { onClickDelete(optimalRouteUi.id) }
                            showDeleteDialog = false
                        },
                    )
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
