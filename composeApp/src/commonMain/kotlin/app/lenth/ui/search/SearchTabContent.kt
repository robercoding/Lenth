package app.lenth.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.lenth.ui.SearchViewModel
import app.lenth.ui.components.LenthPrimaryButton
import app.lenth.ui.components.PlaceInputField
import app.lenth.ui.search.optimalpathsheet.OptimalPathSheet
import app.lenth.ui.utils.BackHandler
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch

@Composable
fun SearchTabContent(viewModel: SearchViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    var focusedPlaceIndex by remember { mutableStateOf<Int?>(null) }
    var isTextFieldFocused by remember { mutableStateOf(false) }

    val isButtonSearchOptimalRouteVisible by remember(state.inputPlaces) {
        val placesNotEmpty = state.inputPlaces.filter { it.place.isNotEmpty() && it.selectedFromAutocomplete }
        val isVisible = placesNotEmpty.size >= 2
        mutableStateOf(isVisible)
    }

    BackHandler(
        isEnabled = state.autoCompleteResults.isNotEmpty(),
        onBack = {
            focusedPlaceIndex?.let { placeIndex ->
                focusManager.clearFocus(force = true)
                viewModel.onBackHandler(placeIndex = placeIndex)
                focusedPlaceIndex = null
            }
        },
    )

    val lazyColumnState = rememberLazyListState()
    var currentTopOffset by remember { mutableStateOf(0.dp) }
    val currentInputFieldOffsetAnimated by animateDpAsState(if (isTextFieldFocused) currentTopOffset else 0.dp, tween(250))
    val density = LocalDensity.current

    val cities = remember { mutableListOf("Valencia", "Barcelona", "Madrid", "Zaragoza", "Galicia", "Granada", "Malaga", "Cadiz") }
    var alreadyScrolled by rememberSaveable { mutableStateOf(false) }
    val shouldShowArrow = lazyColumnState.canScrollForward && !alreadyScrolled

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                // LazyColumn for input places
                LazyColumn(
                    state = lazyColumnState,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .weight(weight = 1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    itemsIndexed(state.inputPlaces) { index, inputPlace ->
                        val city = remember { mutableStateOf("e.g. Valencia") }
                        val scope = rememberCoroutineScope()

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = if (focusedPlaceIndex == index) currentInputFieldOffsetAnimated else 0.dp),
                        ) {
                            PlaceInputField(
                                text = inputPlace.place,
                                hint = city.value,
                                onQueryChanged = { query ->
                                    if (focusedPlaceIndex == index) {
                                        viewModel.onQueryChanged(query, index)
                                    }
                                },
                                isSetByAutocomplete = inputPlace.selectedFromAutocomplete,
                                onClickDelete = {
                                    viewModel.onClearInputPlace(index)
                                },
                                onFocused = {
                                    val currentPosition = lazyColumnState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
                                    val currentItemOffsetPosition = currentPosition?.offset ?: 0
                                    with(density) {
                                        scope.launch {
                                            val currentHeight = currentPosition?.size ?: 0
                                            val viewportEnd = lazyColumnState.layoutInfo.viewportEndOffset
                                            val exceedsViewportEnd = (currentItemOffsetPosition + currentHeight) > viewportEnd
                                            val exceedsViewportStart = currentItemOffsetPosition < 0
                                            val scrollToShowEntirely = if (exceedsViewportEnd) {
                                                currentItemOffsetPosition + currentHeight - viewportEnd
                                            } else if (exceedsViewportStart) {
                                                currentItemOffsetPosition
                                            } else {
                                                0
                                            }
                                            Logger.i("NeedToScroll: $scrollToShowEntirely")
                                            lazyColumnState.scrollBy(scrollToShowEntirely.toFloat())

                                            val newCurrentPositionOffset =
                                                lazyColumnState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }?.offset ?: 0
                                            val offsetToAnimate = (-newCurrentPositionOffset)
                                            Logger.i("OffsetToAnimate: $offsetToAnimate")
                                            currentTopOffset = offsetToAnimate.toDp()
                                            focusedPlaceIndex = index
                                            isTextFieldFocused = true
                                        }
                                    }
                                },
                            )
                        }
                    }
                }

                LaunchedEffect(lazyColumnState.firstVisibleItemIndex) {
                    if (alreadyScrolled) {
                        return@LaunchedEffect
                    }
                    if (lazyColumnState.firstVisibleItemIndex > 0) {
                        alreadyScrolled = true
                    }
                }

                androidx.compose.animation.AnimatedVisibility(
                    modifier = Modifier.fillMaxWidth().animateContentSize(),
                    visible = shouldShowArrow && lazyColumnState.canScrollForward,
                    enter = fadeIn() + expandVertically(expandFrom = Alignment.CenterVertically),
                    exit = fadeOut() + shrinkVertically(),
                ) {
                    val infiniteTransition = rememberInfiniteTransition()
                    val arrowOffset by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 10f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 450, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse,
                        ),
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Scroll down",
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .offset(y = arrowOffset.dp)
                            .size(24.dp),
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                // Button to clear all input
                Box(
                    modifier = Modifier.padding(16.dp),
                ) {
                    LenthPrimaryButton(
                        text = "Clear all",
                        textColor = Color.White,
                        backgroundColor = Color.DarkGray,
                        onClick = { viewModel.resetInputPlaces() },
                    )
                }
            }

            androidx.compose.animation.AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = isButtonSearchOptimalRouteVisible,
                enter = fadeIn() + expandHorizontally(expandFrom = Alignment.CenterHorizontally),
                exit = fadeOut(),
            ) {
                LenthPrimaryButton(
                    text = "Search optimal route",
                    textColor = Color.White,
                    isLoading = state.isOptimizingRoute,
                    modifier = Modifier.padding(16.dp),
                    backgroundColor = Color(53, 132, 220),
                    onClick = { viewModel.onSearch() },
                )
            }
        }

        // List of suggestions
        AnimatedVisibility(isTextFieldFocused, enter = fadeIn(tween(delayMillis = 100)) + expandVertically(expandFrom = Alignment.Top), exit = fadeOut()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .offset(y = 72.dp) // Height of the place input field
                    .zIndex(1f) // not sure if this is needed
                    .background(Color.Black, RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp)
                    .clickable {
                        focusManager.clearFocus(force = true)
                        isTextFieldFocused = false
                    }, // Show below the input field,.
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                itemsIndexed(state.autoCompleteResults, key = { index, result -> result }) { index, result ->
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
                            .clickable {
                                focusedPlaceIndex?.let { index ->
                                    viewModel.onClickResultAutoComplete(result, index)
                                    focusManager.clearFocus(force = true)
                                    isTextFieldFocused = false
                                }
                            },
                    )
                }

            }
        }

        OptimalPathSheet(
            minimumCostPath = state.minimumCostPath,
            onDismissMinimumCostPath = { viewModel.onDismissMinimumCostPath() },
        )
    }
}