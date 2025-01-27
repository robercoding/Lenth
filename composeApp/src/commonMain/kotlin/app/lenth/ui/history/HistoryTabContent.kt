package app.lenth.ui.history

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.lenth.ui.components.LenthPrimaryButton
import app.lenth.ui.history.models.OptimalRouteUi
import app.lenth.ui.search.optimalpathsheet.OptimalPathSheet
import app.lenth.ui.theme.ActionBlue
import app.lenth.ui.theme.OnActionBlue
import lenth.composeapp.generated.resources.Res
import lenth.composeapp.generated.resources.tab_search_action_clear_all
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HistoryTabContent(
    historyViewModel: HistoryViewModel = koinViewModel(),
    onClickImage: (ImageBitmap) -> Unit,
    onClickGoSearch: () -> Unit,
) {
    // Add the History Tab's content here
    val state = historyViewModel.state.collectAsStateWithLifecycle(null).value
    var selectedOptimalRoute by remember { mutableStateOf<OptimalRouteUi?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
                .navigationBarsPadding(),
        ) {

            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
                },
            ) { state ->

                when (state?.size) {
                    null -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                    }

                    0 -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column {
                                Text(
                                    text = "You have not searched for any optimal routes yet, start searching now!",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                LenthPrimaryButton(
                                    text = "Start searching",
                                    onClick = {
                                        onClickGoSearch()
                                    },
                                    textColor = OnActionBlue,
                                    backgroundColor = ActionBlue,
                                )

                            }
                        }
                    }

                    else -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier.height(54.dp),
                                    contentAlignment = Alignment.CenterStart,
                                ) {
                                    Text(
                                        text = "Recent optimal routes",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier,
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                TextButton(
                                    enabled = true,
                                    onClick = {},
                                ) {
                                    Text(
                                        text = stringResource(Res.string.tab_search_action_clear_all),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                            }

                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp),) {
                                items(state, key = { item -> item.id }) {
                                    CustomListItem(
                                        modifier = Modifier.fillMaxWidth(),
                                        byteArray = it.mapImage,
                                        title = "From ${it.path.first().name} to ${it.path.last().name}",
                                        locations = it.path.size,
                                        distance = it.distance,
                                        onClickImage = onClickImage,
                                        onClick = {
                                            // historyViewModel.onClickItem(it.path)
                                            selectedOptimalRoute = it
                                        },
                                    )
                                }
                            }
                        }
                    }
                }

            }

        }


        selectedOptimalRoute?.let {
            OptimalPathSheet(
                optimalRouteUi = it,
                onDismissMinimumCostPath = {
                    selectedOptimalRoute = null
                },
                onClickImage = onClickImage,
            )
        }
    }
}
