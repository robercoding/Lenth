package app.lenth.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.lenth.ui.search.filter.SearchTypeChips
import app.lenth.ui.search.filter.SearchTypeUi

private const val ANIMATION_TIME = 200
@Composable
fun SearchTabHeader(
    showFilterChips: Boolean,
    onToggleFilterChips: () -> Unit,
    searchType: SearchTypeUi,
    onSearchTypeChanged: (SearchTypeUi) -> Unit,
) {
    Column {
        Row {
            Text(
                text = "Locations",
                style = MaterialTheme.typography.h6,
                color = Color.White,
                modifier = Modifier,
            )
            Spacer(modifier = Modifier.weight(1f))

            // Filter icon
            Icon(
                imageVector = Icons.Default.Tune,
                contentDescription = "Filter",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onToggleFilterChips()
                    },
            )
        }


        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(
            visible = showFilterChips,
            enter = fadeIn(tween(ANIMATION_TIME)) + expandVertically(expandFrom = Alignment.Top, animationSpec = tween(ANIMATION_TIME)),
            exit = fadeOut(tween(ANIMATION_TIME)) + shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(ANIMATION_TIME)),

            ) {
            Column {
                SearchTypeChips(
                    selectedSearchType = searchType,
                    onChipSelected = { searchType ->
                        onSearchTypeChanged(searchType)
                    },
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}