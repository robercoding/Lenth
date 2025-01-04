package app.lenth.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.lenth.ui.SearchViewModel
import app.lenth.ui.components.LenthPrimaryButton
import app.lenth.ui.utils.BackHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTabContent(viewModel: SearchViewModel) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    var editingPlaceIndex by remember { mutableStateOf<Int?>(null) }

    val isSearchOptimalRouteVisible by remember(state.inputPlaces) {
        val placesNotEmpty = state.inputPlaces.filter { it.place.isNotEmpty() && it.selectedFromAutocomplete }
        val isVisible = placesNotEmpty.size >= 2
        mutableStateOf(isVisible)
    }
    BackHandler(
        isEnabled = state.autoCompleteResults.isNotEmpty(),
        onBack = {
            editingPlaceIndex?.let { placeIndex ->
                focusManager.clearFocus(force = true)
                viewModel.onBackHandler(placeIndex = placeIndex)
                editingPlaceIndex = null
            }
        },
    )


    Box {
        val cities = remember { mutableListOf("Valencia", "Barcelona", "Madrid", "Zaragoza", "Galicia", "Granada", "Malaga", "Cadiz") }
        var currentBottomOffset by remember { mutableStateOf(0.dp) }
        val animateCurrentBottomOffset by animateDpAsState(currentBottomOffset)
        // val scroll = scroll
        val scrollableState = rememberScrollState()
        var statusBarPaddingTop = with(LocalDensity.current) { WindowInsets.statusBars.getTop(this).toDp() }
        val statusBarPaddingBottom = with(LocalDensity.current) { WindowInsets.statusBars.getBottom(this).toDp() }
        val statusBarHeight = statusBarPaddingTop + statusBarPaddingBottom

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f, fill = false),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(state.inputPlaces) { inputPlace ->
                        val city = remember { cities.random() }
                        PlaceInputField(
                            text = inputPlace.place,
                            hint = "e.g. $city",
                            onQueryChanged = {
                                editingPlaceIndex?.let { placeIndex ->
                                    viewModel.onQueryChanged(query = it, placeIndex = placeIndex)
                                }
                            },
                            isSetByAutocomplete = inputPlace.selectedFromAutocomplete,
                            onClickDelete = {
                                viewModel.onClearInputPlace(placeIndex = state.inputPlaces.indexOf(inputPlace))
                            },
                            onFocused = {
                                editingPlaceIndex = state.inputPlaces.indexOf(inputPlace)
                                currentBottomOffset = it - statusBarHeight - 16.dp
                            },
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Box {
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
                visible = isSearchOptimalRouteVisible,
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


        AnimatedVisibility(state.autoCompleteResults.isNotEmpty(), enter = fadeIn() + expandVertically(expandFrom = Alignment.Top), exit = fadeOut()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().fillMaxHeight().offset(y = animateCurrentBottomOffset).background(Color.Black).padding(16.dp),
            ) {
                items(state.autoCompleteResults) { result ->
                    // Text(text = result, color = Color.White, modifier = Modifier.animateItem())
                    SearchResultItem(
                        result = result,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .animateItem()
                            .clickable {
                                editingPlaceIndex?.let { placeIndex ->
                                    viewModel.onClickResultAutoComplete(result = result, placeIndex = placeIndex)
                                    focusManager.clearFocus(force = true)
                                    editingPlaceIndex = null
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
            // colors = TextFieldDefaults.textFieldColors(
            //     focusedIndicatorColor = Color.Red,
            // ),

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