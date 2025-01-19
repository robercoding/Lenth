package app.lenth.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.lenth.ui.components.AnimatedText
import app.lenth.ui.search.filter.SearchTypeChips
import app.lenth.ui.search.filter.SearchTypeUi
import lenth.composeapp.generated.resources.Res
import lenth.composeapp.generated.resources.tab_search_action_clear_all
import lenth.composeapp.generated.resources.tab_search_title_locations
import lenth.composeapp.generated.resources.tab_search_title_searching
import org.jetbrains.compose.resources.stringResource

private const val ANIMATION_TIME = 200

@Composable
fun SearchTabHeader(
    focusedTextField: Boolean,
    isSearchingOrHasOptimalPath: Boolean,
    showFilterChips: Boolean,
    onCancelSearch: () -> Unit,
    searchType: SearchTypeUi,
    onSearchTypeChanged: (SearchTypeUi) -> Unit,
    onClearAll: () -> Unit,
) {

    val clearAllTextColor by animateColorAsState(
        targetValue = if (!focusedTextField) MaterialTheme.colorScheme.onSurface else Color.Transparent,
        animationSpec = tween(ANIMATION_TIME),
    )
    val searching = stringResource(Res.string.tab_search_title_searching)
    val locations = stringResource(Res.string.tab_search_title_locations)
    val headerText by remember(focusedTextField) {
        val headerText = if (focusedTextField) searching else locations
        mutableStateOf(headerText)
    }

    Column(modifier = Modifier) {
        Row(modifier = Modifier.height(54.dp), verticalAlignment = Alignment.CenterVertically) {
            AnimatedText(
                modifier = Modifier.weight(1f),
                text = headerText,
            ) { animatedText ->
                Text(
                    text = animatedText,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier,
                )
            }

            Box(
                contentAlignment = Alignment.CenterEnd,
            ) {
                TextButton(
                    enabled = !isSearchingOrHasOptimalPath && !focusedTextField,
                    onClick = { onClearAll() },
                ) {
                    Text(
                        text = stringResource(Res.string.tab_search_action_clear_all),
                        color = clearAllTextColor,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                androidx.compose.animation.AnimatedVisibility(
                    visible = focusedTextField,
                    enter = fadeIn(tween(ANIMATION_TIME)),
                    exit = fadeOut(tween(ANIMATION_TIME)),
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = Icons.Default.Close.name,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50f))
                            .clickable { onCancelSearch() }
                            .size(24.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
        }

        // Spacer(modifier = Modifier.height(16.dp))
        // AnimatedVisibility(
        //     visible = showFilterChips,
        //     enter = fadeIn(tween(ANIMATION_TIME)) + expandVertically(
        //         expandFrom = Alignment.Top,
        //         animationSpec = tween(ANIMATION_TIME),
        //     ),
        //     exit = fadeOut(tween(ANIMATION_TIME)) + shrinkVertically(
        //         shrinkTowards = Alignment.Top,
        //         animationSpec = tween(ANIMATION_TIME),
        //     ),
        // ) {
        //     Column {
        //         SearchTypeChips(
        //             selectedSearchType = searchType,
        //             onChipSelected = { searchType ->
        //                 onSearchTypeChanged(searchType)
        //             },
        //         )
        //         Spacer(modifier = Modifier.height(16.dp))
        //     }
        // }
    }
}
