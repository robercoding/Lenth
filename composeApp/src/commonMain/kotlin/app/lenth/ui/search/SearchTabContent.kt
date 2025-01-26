package app.lenth.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.lenth.ui.SearchViewModel
import app.lenth.ui.components.LenthPrimaryButton
import app.lenth.ui.components.indicator.ArrowIndicator
import app.lenth.ui.search.autocomplete.AutoCompleteInputList
import app.lenth.ui.search.dialog.ClearAllAlertDialog
import app.lenth.ui.search.dialog.DiscardCurrentPlaceInput
import app.lenth.ui.search.optimalpathsheet.OptimalPathSheet
import app.lenth.ui.theme.ActionBlue
import app.lenth.ui.theme.OnActionBlue
import app.lenth.ui.utils.BackHandler
import co.touchlab.kermit.Logger
import lenth.composeapp.generated.resources.Res
import lenth.composeapp.generated.resources.tab_search_action_search_optimal_route
import org.jetbrains.compose.resources.stringResource

internal val HeaderTopPadding = 8.dp
@Composable
fun SearchTabContent(viewModel: SearchViewModel, onClickImage: (ImageBitmap) -> Unit) {
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

    var isClearAllAlertDialogVisible by rememberSaveable { mutableStateOf(false) }
    var isDiscardCurrentInputAlertDialogVisible by rememberSaveable { mutableStateOf(false) }

    val isSearchingOrHasOptimalPath by remember(state.isOptimizingRoute, state.optimalRouteUi) {
        mutableStateOf(state.isOptimizingRoute || state.optimalRouteUi != null)
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
            if (inputPlace.place.isEmpty() && state.inputPlaces.lastIndex != indexNotNull) {
                viewModel.onClearInputPlace(indexNotNull)
            }
            Logger.i("Clicked clear focus")
            clearFocus()
            viewModel.onBackHandler()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
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
                // .padding(16.dp)
                .padding(horizontal = 16.dp)
                .padding(top = HeaderTopPadding)
                .navigationBarsPadding()
        ) {
            SearchTabHeader(
                focusedTextField = isTextFieldFocused,
                isSearchingOrHasOptimalPath = isSearchingOrHasOptimalPath,
                showFilterChips = showFilterChips,
                onCancelSearch = {
                    onBackOnFocusedPlace()
                },
                searchType = state.searchType,
                onSearchTypeChanged = viewModel::onSearchTypeChanged,
                onClearAll = { isClearAllAlertDialogVisible = true },
            )

            Box(
                modifier = Modifier
                    .onGloballyPositioned { listOffsetInParent = it.positionInParent().y },
            ) {
                Column {
                    Column(modifier = Modifier.weight(1f)) {
                        SearchTabListLocations(
                            Modifier
                                .padding(horizontal = 8.dp)
                                .weight(weight = 1f, fill = false)
                                .animateContentSize(),
                            lazyListState = lazyColumnState,
                            inputPlaces = state.inputPlaces,
                            isSearchingOrHasOptimalPath = isSearchingOrHasOptimalPath,
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

                        ArrowIndicator(lazyColumnState = lazyColumnState)
                    }

                    AnimatedVisibility(
                        modifier = Modifier.fillMaxWidth(),
                        visible = isButtonSearchOptimalRouteVisible,
                        enter = fadeIn() + expandHorizontally(expandFrom = Alignment.CenterHorizontally),
                        exit = fadeOut(),
                    ) {
                        LenthPrimaryButton(
                            text = stringResource(Res.string.tab_search_action_search_optimal_route),
                            textColor = OnActionBlue,
                            backgroundColor = ActionBlue,
                            isLoading = state.isOptimizingRoute,
                            modifier = Modifier.padding(16.dp),
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

        ClearAllAlertDialog(
            isClearAllAlertDialogVisible,
            onDismissRequest = { isClearAllAlertDialogVisible = false },
            onConfirmClearAll = {
                isClearAllAlertDialogVisible = false
                viewModel.resetInputPlaces()
            },
        )

        OptimalPathSheet(
            optimalRouteUi = state.optimalRouteUi,
            onDismissMinimumCostPath = { viewModel.onDismissMinimumCostPath() },
            onClickImage = onClickImage
        )
    }
}