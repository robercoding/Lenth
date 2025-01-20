package app.lenth.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.lenth.ui.history.models.OptimalRouteUi
import app.lenth.ui.search.optimalpathsheet.OptimalPathSheet
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HistoryTabContent(
    historyViewModel: HistoryViewModel = koinViewModel(),
    onClickImage: (String) -> Unit,
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                contentAlignment = androidx.compose.ui.Alignment.CenterStart,
            ) {
                Text(
                    text = "Recent optimal routes",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier,
                )
            }
            state?.let {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(it, key = { item -> item.id }) {
                        CustomListItem(
                            modifier = Modifier.fillMaxWidth(),
                            imageUrl = it.imageStart,
                            title = "From ${it.path.first()} to ${it.path.last()}",
                            locations = it.path.size,
                            distance = it.distance,
                            onClickImage = onClickImage,
                            onClick = { selectedOptimalRoute = it },
                        )
                    }
                }
            }
        }


        selectedOptimalRoute?.let {
            OptimalPathSheet(
                optimalRouteUi = it,
                onDismissMinimumCostPath = {
                    selectedOptimalRoute = null
                }
            )
        }
    }
}
