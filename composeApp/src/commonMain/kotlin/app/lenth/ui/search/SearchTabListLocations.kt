package app.lenth.ui.search

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import app.lenth.ui.InputPlace
import app.lenth.ui.components.PlaceInputField
import kotlinx.coroutines.launch

@Composable
fun SearchTabListLocations(
    modifier: Modifier,
    lazyListState: LazyListState,
    inputPlaces: List<InputPlace>,
    isTextFieldFocused: Boolean,
    focusedPlaceIndex: Int?,
    onQueryChanged: (String, Int) -> Unit,
    onClearInputPlace: (Int) -> Unit,
    onUpdateTextFieldHeight: (Int) -> Unit,
    onUpdateFocusedPlaceIndex: (Int) -> Unit,
    onEndCalculationFocus: () -> Unit,
    onFinishPlaceInputAnimateBack: () -> Unit,
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    var currentTopTranslationY by remember { mutableStateOf(0f) }
    val currentInputFieldTranslationYAnimated by animateFloatAsState(
        targetValue = if (isTextFieldFocused) currentTopTranslationY else 0f, tween(250),
        finishedListener =  {
            if(!isTextFieldFocused) {
                onFinishPlaceInputAnimateBack()
            }
        }
    )

    LazyColumn(
        state = lazyListState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = !isTextFieldFocused,
    ) {
        itemsIndexed(
            inputPlaces,
            key = { index, inputPlace -> "${inputPlace.id}" },
        ) { index, inputPlace ->
            val city = remember { mutableStateOf("e.g. Valencia") }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        translationY = if (focusedPlaceIndex == index) currentInputFieldTranslationYAnimated else 0f
                    }
                    .background(Color.Black)
                    .animateItem(),
            ) {
                PlaceInputField(
                    modifier = Modifier.onGloballyPositioned {
                        if (focusedPlaceIndex == index) {
                            onUpdateTextFieldHeight(it.size.height)
                        }
                    },
                    text = inputPlace.place,
                    hint = city.value,
                    onQueryChanged = { query ->
                        if (focusedPlaceIndex == index) {
                            onQueryChanged(query, index)
                        }
                    },
                    isSetByAutocomplete = inputPlace.selectedFromAutocomplete,
                    onClickDelete = {
                        onClearInputPlace(index)
                    },
                    onFocused = {
                        scope.launch {
                        val currentPosition = lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
                        val currentItemOffsetPosition = currentPosition?.offset ?: 0
                        with(density) {
                                val currentHeight = currentPosition?.size ?: 0
                                val viewportEnd = lazyListState.layoutInfo.viewportEndOffset
                                val exceedsViewportEnd = (currentItemOffsetPosition + currentHeight) > viewportEnd
                                val exceedsViewportStart = currentItemOffsetPosition < 0
                                val scrollToShowEntirely = if (exceedsViewportEnd) {
                                    currentItemOffsetPosition + currentHeight - viewportEnd
                                } else if (exceedsViewportStart) {
                                    currentItemOffsetPosition
                                } else {
                                    0
                                }
                                lazyListState.scrollBy(scrollToShowEntirely.toFloat())

                                val newCurrentPositionOffset =
                                    lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }?.offset ?: 0
                                val offsetToAnimate = (-newCurrentPositionOffset)
                                currentTopTranslationY = offsetToAnimate.toFloat()
                                onUpdateFocusedPlaceIndex(index)
                                onEndCalculationFocus()
                            }
                        }
                    },
                )

                // AnimatedVisibility(
                //     visible = focusedPlaceIndex != index && focusedPlaceIndex != null,
                //     enter = fadeIn(),
                //     exit = fadeOut(tween(250)),
                //     modifier = Modifier.matchParentSize().background(Color.Black, shape = RoundedCornerShape(8.dp))
                // ) {
                //     Box(modifier = Modifier.matchParentSize().background(Color.Black, shape = RoundedCornerShape(8.dp)))
                // }
            }
        }
    }
}