package app.lenth.ui.screens.start

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import app.lenth.ui.HistoryTabContent
import app.lenth.ui.SearchViewModel
import app.lenth.ui.TabPositionText
import app.lenth.ui.search.SearchTabContent

@Composable
fun LenthStart(
    selectedTab: Int,
    searchViewModel: SearchViewModel,
    onTabSelected: (Int) -> Unit,
) {
    val tabs = listOf("Search", "History")
    var tabPositionText by remember { mutableStateOf(TabPositionText(0.dp, 0.dp)) }
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { onTabSelected(index) },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTab == index)
                                MaterialTheme.colorScheme.onSurface
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.onPlaced {
                                tabPositionText = TabPositionText(
                                    width = it.size.width.dp,
                                    left = it.positionInRoot().x.dp,
                                )
                            },
                        )
                    },
                )
            }
        }

        // Content based on selected tab
        when (selectedTab) {
            0 -> SearchTabContent(viewModel = searchViewModel)
            1 -> HistoryTabContent()
        }
    }
}