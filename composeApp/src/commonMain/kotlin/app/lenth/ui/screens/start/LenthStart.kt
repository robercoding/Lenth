package app.lenth.ui.screens.start

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import app.lenth.ui.SearchViewModel
import app.lenth.ui.TabPositionText
import app.lenth.ui.history.ExpandedImageOverlay
import app.lenth.ui.history.HistoryTabContent
import app.lenth.ui.search.SearchTabContent
import lenth.composeapp.generated.resources.Res
import lenth.composeapp.generated.resources.tab_history_section
import lenth.composeapp.generated.resources.tab_search_section
import org.jetbrains.compose.resources.stringResource

@Composable
fun LenthStart(
    selectedTab: Int,
    searchViewModel: SearchViewModel,
    onTabSelected: (Int) -> Unit,
    onClickImage: (String) -> Unit,
) {

    val tabs = listOf(stringResource(Res.string.tab_search_section), stringResource(Res.string.tab_history_section))
    var tabPositionText by remember { mutableStateOf(TabPositionText(0.dp, 0.dp)) }



    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        modifier = Modifier.clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
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
                1 -> HistoryTabContent(onClickImage =onClickImage)
            }

        }
    }
}