package app.lenth.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.lenth.ui.SearchViewModel
import app.lenth.ui.components.LenthPrimaryButton
import app.lenth.ui.search.optimalpathsheet.OptimalPathSheet
import app.lenth.ui.utils.BackHandler
import co.touchlab.kermit.Logger

@Composable
fun SearchTabContent(viewModel: SearchViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    var focusedPlaceIndex by rememberSaveable { mutableStateOf<Int?>(null) }
    var isTextFieldFocused by rememberSaveable { mutableStateOf(false) }


    val lazyColumnState = rememberLazyListState()

    var listOffsetInParent by rememberSaveable { mutableStateOf(0f) } // Y offset of the TextField
    var textFieldHeight by rememberSaveable { mutableStateOf(0) }

    // val cities = remember { mutableListOf("Valencia", "Barcelona", "Madrid", "Zaragoza", "Galicia", "Granada", "Malaga", "Cadiz") }
    var alreadyScrolled by rememberSaveable { mutableStateOf(false) }
    val shouldShowArrow = lazyColumnState.canScrollForward && !alreadyScrolled
    var isDiscardCurrentInputAlertDialogVisible by rememberSaveable { mutableStateOf(false) }

    val isButtonSearchOptimalRouteVisible by remember(state.inputPlaces) {
        val placesNotEmpty = state.inputPlaces.filter { it.place.isNotEmpty() && it.selectedFromAutocomplete }
        val isVisible = placesNotEmpty.size >= 2
        mutableStateOf(isVisible)
    }

    fun clearFocus() {
        focusManager.clearFocus(force = true)
        isTextFieldFocused = false
    }

    fun onBackOnFocusedPlace() {
        val indexNotNull = focusedPlaceIndex ?: return
        val inputPlace = state.inputPlaces.getOrNull(indexNotNull) ?: return

        if (!inputPlace.selectedFromAutocomplete && inputPlace.place.isNotEmpty()) {
            isDiscardCurrentInputAlertDialogVisible = true
        } else {
            if(inputPlace.place.isEmpty() && state.inputPlaces.lastIndex != indexNotNull) {
                viewModel.onClearInputPlace(indexNotNull)
            }
            Logger.i("Clicked clear focus")
            clearFocus()
            viewModel.onBackHandler()
        }
    }

    BackHandler(
        isEnabled = isTextFieldFocused,
        onBack = {
            focusedPlaceIndex?.let { placeIndex ->
                onBackOnFocusedPlace()
            }
        },
    )



    LaunchedEffect(lazyColumnState.firstVisibleItemIndex) {
        if (alreadyScrolled) {
            return@LaunchedEffect
        }
        if (lazyColumnState.firstVisibleItemIndex > 0) {
            alreadyScrolled = true
        }
    }

    var showFilterChips by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(16.dp),
        ) {
            SearchTabHeader(
                showFilterChips = showFilterChips,
                onToggleFilterChips = { showFilterChips = !showFilterChips },
                searchType = state.searchType,
                onSearchTypeChanged = viewModel::onSearchTypeChanged,
            )

            Box(
                modifier = Modifier
                    .onGloballyPositioned {

                        Logger.i("Size of List: ${it.size.height}")
                        Logger.i("Position in parent: ${it.positionInParent().y}")
                        Logger.i("Position in root: ${it.positionInRoot().y}")
                        listOffsetInParent = it.positionInParent().y
                    },
            ) {
                Column {
                    Column(modifier = Modifier.weight(1f)) {
                        SearchTabListLocations(
                            Modifier.padding(horizontal = 8.dp)
                                .weight(weight = 1f, fill = false),
                            lazyListState = lazyColumnState,
                            inputPlaces = state.inputPlaces,
                            isTextFieldFocused = isTextFieldFocused,
                            focusedPlaceIndex = focusedPlaceIndex,
                            onQueryChanged = viewModel::onQueryChanged,
                            onClearInputPlace = {
                                clearFocus()
                                viewModel.onClearInputPlace(it)
                            },
                            onUpdateTextFieldHeight = { height ->
                                textFieldHeight = height
                            },
                            onUpdateFocusedPlaceIndex = { index ->
                                focusedPlaceIndex = index
                            },
                            onFinishPlaceInputAnimateBack = {
                                focusedPlaceIndex = null
                            },
                            onEndCalculationFocus = {
                                isTextFieldFocused = true
                            },
                        )

                        Spacer(modifier = Modifier.height(8.dp))

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

                    AnimatedVisibility(
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

                if (showFilterChips) {
                    Box(
                        modifier = Modifier.matchParentSize().clickable {
                            showFilterChips = false
                        },
                    )
                }
            }
        }

        AnimatedVisibility(isTextFieldFocused, enter = fadeIn(tween(delayMillis = 100)) + expandVertically(expandFrom = Alignment.Top), exit = fadeOut()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .absoluteOffset(
                        x = 0.dp,
                        y = with(LocalDensity.current) { (listOffsetInParent + textFieldHeight).toDp() + 16.dp },
                    )
                    .background(Color.Black, RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp)
                    .padding(top = 12.dp)
                    .clickable { onBackOnFocusedPlace() },
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
                                    clearFocus()
                                }
                            },
                    )
                }
            }
        }

        AnimatedVisibility(
            isDiscardCurrentInputAlertDialogVisible,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
            exit = fadeOut(),
        ) {
            androidx.compose.material.AlertDialog(
                onDismissRequest = { isDiscardCurrentInputAlertDialogVisible = false },
                title = { Text(
                    text = "Discard current input?",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                ) },
                text = { Text(
                    text = "You haven't selected any place from the autocomplete list. Do you want to discard the current input?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                ) },
                confirmButton = {
                    Button(
                        onClick = {
                            isDiscardCurrentInputAlertDialogVisible = false
                            viewModel.onClearInputPlace(focusedPlaceIndex ?: return@Button)
                            clearFocus()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    ) {
                        Text(
                            "Discard",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { isDiscardCurrentInputAlertDialogVisible = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Cancel", style = MaterialTheme.typography.bodyMedium)
                    }
                },
                shape = RoundedCornerShape(16.dp),
                backgroundColor = Color.DarkGray,
            )
        }

        OptimalPathSheet(
            minimumCostPath = state.minimumCostPath,
            onDismissMinimumCostPath = { viewModel.onDismissMinimumCostPath() },
        )
    }

}