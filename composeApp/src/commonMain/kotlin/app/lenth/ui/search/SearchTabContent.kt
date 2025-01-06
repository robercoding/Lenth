package app.lenth.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Delete
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.lenth.ui.SearchViewModel
import app.lenth.ui.components.LenthPrimaryButton
import app.lenth.ui.utils.BackHandler
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
                                    // val itemHeight = currentPosition?.
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
                            animation = tween(durationMillis = 800, easing = LinearEasing),
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
                    // .offset(y = 80.dp)
                    .offset(y = 72.dp)
                    // .offset(y = currentInputFieldOffsetAnimated + 72.dp)
                    .zIndex(1f)
                    .background(Color.Black, RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp)
                    .clickable {
                        focusManager.clearFocus(force = true)
                        isTextFieldFocused = false
                    }, // Show below the input field,.
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(state.autoCompleteResults) { result ->
                    SearchResultItem(
                        result = result,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
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

        val stateSheet = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        var showBottomSheet by remember(state.minimumCostPath) {
            mutableStateOf(state.minimumCostPath != null)
        }
        LaunchedEffect(key1 = stateSheet) {
            // state.hide()
        }
        LaunchedEffect(Unit) {
            // state.hide()
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                content = {
                    Box(modifier = Modifier.fillMaxWidth().background(Color.DarkGray), contentAlignment = Alignment.CenterStart) {
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            viewModel.state.value.minimumCostPath?.let {
                                it.path.forEachIndexed { index, place ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(vertical = 8.dp),
                                    ) {
                                        // Draw circle with index inside and line below
                                        CanvasWithCircleAndLine(index = index, totalItems = it.path.size)

                                        Spacer(modifier = Modifier.width(8.dp))

                                        // Display the place text
                                        Text(
                                            text = place,
                                            color = Color.White,
                                            style = MaterialTheme.typography.body1,
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                sheetState = stateSheet,
                containerColor = Color.DarkGray,
                onDismissRequest = {
                    viewModel.onDismissMinimumCostPath()
                },

                )
        }
    }
}

@Composable
fun CanvasWithCircleAndLine(index: Int, totalItems: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(40.dp), // Adjust size for circle and line
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw the circle
            drawCircle(
                color = Color.White,
                radius = size.minDimension / 2,
                style = Stroke(width = 2.dp.toPx()), // Make the circle an outline
            )

            // Draw the line below the circle
            if (index < totalItems - 1) { // Only draw the line if it's not the last item
                drawLine(
                    color = Color.White,
                    start = Offset(x = size.width / 2, y = size.height),
                    end = Offset(x = size.width / 2, y = size.height + 40), // Adjust line length
                    strokeWidth = 2.dp.toPx(),
                )
            }
        }

        // Draw the index inside the circle
        Text(
            text = "${index + 1}",
            color = Color.White, // Adjust text color for contrast
            style = MaterialTheme.typography.body2,
        )
    }
}

@Composable
fun SearchResultItem(
    result: String,
    modifier: Modifier,
) {
    Card(
        modifier = modifier,
        backgroundColor = Color.DarkGray,
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "Location",
                tint = Color.White,
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = result,
                color = Color.White,
                style = MaterialTheme.typography.body1,
            )
        }
    }
}

@Composable
fun PlaceInputField(
    text: String,
    isSetByAutocomplete: Boolean,
    hint: String,
    onQueryChanged: (String) -> Unit,
    onClickDelete: () -> Unit,
    onFocused: (Dp) -> Unit,
) {
    var bottomOffset by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    var isFocused by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(if (isFocused) Color.White else Color.Transparent)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.DarkGray, RoundedCornerShape(8.dp))
            .onGloballyPositioned {
                // bottomOffset = it.positionInRoot().y.toInt() // Close 1
                // bottomOffset = it.positionOnScreen().y.toInt() // Close 2
                // bottomOffset = it.positionInParent().y.toInt() // Doesn't work
                // bottomOffset = it.positionInWindow().y.toInt() // Far
                // bottomOffset = it.positionInRoot().y.toInt() // Close 3
                bottomOffset = it.boundsInRoot().top.toInt()
            },
        // .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = text,
            onValueChange = {
                onQueryChanged(it)
            },
            placeholder = { Text(hint, color = Color.Gray) },
            modifier = Modifier
                .weight(1f)
                .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    if (focusState.isFocused) {
                        onFocused(with(density) { bottomOffset.toDp() - 32.dp })
                    }
                },
            trailingIcon = {
                androidx.compose.animation.AnimatedVisibility(
                    visible = isSetByAutocomplete,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut(),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Clear",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp).clickable(onClick = onClickDelete),
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                backgroundColor = Color.Transparent,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                placeholderColor = Color.Gray,
            ),
        )
    }
}