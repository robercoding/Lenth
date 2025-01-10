package app.lenth.ui.search.autocomplete

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import app.lenth.ui.search.SearchResultItem

@Composable
fun AutoCompleteInputList(
    autoCompleteResults: List<String>,
    isTextFieldFocused: Boolean,
    listOffsetInParent: Float,
    textFieldHeight: Int,
    onBackOnFocusedPlace: () -> Unit,
    onClickResultAutoComplete: (String) -> Unit,
) {
    AnimatedVisibility(
        isTextFieldFocused,
        enter = fadeIn(tween(delayMillis = 100)) + expandVertically(expandFrom = Alignment.Top),
        exit = fadeOut()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .absoluteOffset(
                    x = 0.dp,
                    y = with(LocalDensity.current) { (listOffsetInParent + textFieldHeight).toDp() + 16.dp },
                )
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp)
                .clickable(
                    indication = null,
                    interactionSource = null
                ) { onBackOnFocusedPlace() },
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(autoCompleteResults, key = { index, result -> result }) { index, result ->
                SearchResultItem(
                    result = result,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                        .animateItem(
                            fadeInSpec = tween(
                                durationMillis = 100 + (index * 40),
                                easing = LinearOutSlowInEasing,
                            ),
                        )
                        .clickable { onClickResultAutoComplete(result) },
                )
            }
        }
    }
}
