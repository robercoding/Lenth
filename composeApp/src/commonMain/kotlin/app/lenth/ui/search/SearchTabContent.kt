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
import app.lenth.ui.search.autocomplete.AutoCompleteInputList
import app.lenth.ui.search.dialog.DiscardCurrentPlaceInput
import app.lenth.ui.search.indicator.ArrowIndicator
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
    val isButtonSearchOptimalRouteVisible by remember(state.inputPlaces) {
        val placesNotEmpty = state.inputPlaces.filter { it.place.isNotEmpty() && it.selectedFromAutocomplete }
        val isVisible = placesNotEmpty.size >= 2
        mutableStateOf(isVisible)
    }

    var isDiscardCurrentInputAlertDialogVisible by rememberSaveable { mutableStateOf(false) }
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

                        ArrowIndicator(lazyColumnState = lazyColumnState,)
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

                // Overlay to close the filter chips
                if (showFilterChips) {
                    Box(
                        modifier = Modifier.matchParentSize().clickable {
                            showFilterChips = false
                        },
                    )
                }
            }
        }

        AutoCompleteInputList(
            autoCompleteResults = state.autoCompleteResults,
            isTextFieldFocused = isTextFieldFocused,
            listOffsetInParent = listOffsetInParent,
            textFieldHeight = textFieldHeight,
            onBackOnFocusedPlace = { onBackOnFocusedPlace() },
            onClickResultAutoComplete = { result ->
                focusedPlaceIndex?.let { index ->
                    viewModel.onClickResultAutoComplete(result, index)
                }
                clearFocus()
            },
        )

        DiscardCurrentPlaceInput(
            isDiscardCurrentInputAlertDialogVisible,
            onDismissRequest = { isDiscardCurrentInputAlertDialogVisible = false },
            onConfirmDiscard = {
                isDiscardCurrentInputAlertDialogVisible = false
                focusedPlaceIndex?.let { viewModel.onClearInputPlace(it) }
                clearFocus()
            },
        )

        OptimalPathSheet(
            minimumCostPath = state.minimumCostPath,
            onDismissMinimumCostPath = { viewModel.onDismissMinimumCostPath() },
        )
    }

}