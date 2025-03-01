package app.lenth.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.lenth.domain.SearchPlacesByInputQueryUseCase
import app.lenth.domain.history.DeleteOptimalRouteUseCase
import app.lenth.domain.history.GetOptimalRouteUseCase
import app.lenth.domain.history.InsertOptimalRouteUseCase
import app.lenth.domain.mapper.toDomain
import app.lenth.ui.history.models.OptimalRouteUi
import app.lenth.ui.search.filter.SearchTypeUi
import app.lenth.ui.search.filter.toDomain
import kotlin.random.Random
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val mockInitialState = listOf(
    InputPlace(place = "Valencia", selectedFromAutocomplete = true),
    InputPlace(place = "Zaragoza", selectedFromAutocomplete = true),
    InputPlace(place = "Barcelona", selectedFromAutocomplete = true),
    InputPlace(place = "Madrid", selectedFromAutocomplete = true),
    InputPlace(place = "Seville", selectedFromAutocomplete = true),
    InputPlace(place = "Bilbao", selectedFromAutocomplete = true),
    InputPlace(place = "Malaga", selectedFromAutocomplete = true),
    InputPlace(place = "Granada", selectedFromAutocomplete = true),
    InputPlace(place = "Castellon", selectedFromAutocomplete = true),
    InputPlace(place = "Estivella", selectedFromAutocomplete = true),
    InputPlace(place = "Alicante", selectedFromAutocomplete = true),
    InputPlace(place = "Albacete", selectedFromAutocomplete = true),
    InputPlace(place = "Huesca", selectedFromAutocomplete = true),
    InputPlace(place = "", selectedFromAutocomplete = false),
)

class SearchViewModel(
    private val getOptimalRouteUseCase: GetOptimalRouteUseCase,
    private val searchPlacesByInputQueryUseCase: SearchPlacesByInputQueryUseCase,
    private val insertOptimalRouteUseCase: InsertOptimalRouteUseCase,
    private val deleteOptimalRouteUseCase: DeleteOptimalRouteUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<SearchState> = MutableStateFlow(
        SearchState(
            inputPlaces = mockInitialState,
        ),
    )

    val state: StateFlow<SearchState> = _state

    private var job: Job? = null

    fun onQueryChanged(query: String, placeIndex: Int) {
        _state.update {
            val places = it.inputPlaces.toMutableList()
            places.apply {
                set(placeIndex, get(placeIndex).copy(place = query, selectedFromAutocomplete = false))
            }
            it.copy(inputPlaces = places)
        }
        job?.cancel()
        job = viewModelScope.launch {
            val currentValue = _state.value

            val filter = searchPlacesByInputQueryUseCase(query, currentValue.searchType.toDomain())
                .filter { foundCitiesByQuery ->
                    !currentValue.inputPlaces.any { inputPlace -> inputPlace.place.equals(foundCitiesByQuery, ignoreCase = true) }
                }

            _state.update {
                val places = it.inputPlaces.toMutableList()
                it.copy(inputPlaces = places, autoCompleteResults = filter)
            }
        }
    }

    fun onSearch() {
        viewModelScope.launch {
            _state.update {
                it.copy(isOptimizingRoute = true, autoCompleteResults = emptyList())
            }

            val minimumCostPath = getOptimalRouteUseCase(_state.value.inputPlaces.map { it.place })
            insertOptimalRouteUseCase(minimumCostPath.toDomain())
            _state.update {
                it.copy(isOptimizingRoute = false, optimalRouteUi = minimumCostPath)
            }
        }
    }

    fun onDismissMinimumCostPath() {
        _state.update {
            it.copy(optimalRouteUi = null)
        }
    }

    fun resetInputPlaces() {
        _state.update {
            it.copy(inputPlaces = listOf(InputPlace(place = "", selectedFromAutocomplete = false), InputPlace(place = "", selectedFromAutocomplete = false)))
        }
    }

    fun onClearInputPlace(placeIndex: Int) {
        clearInputPlace(placeIndex)
    }

    private fun clearInputPlace(index: Int) {
        _state.update {
            val places = it.inputPlaces.toMutableList()
            if (index < places.indices.last && places.size > 2) {
                places.apply {
                    removeAt(index)
                    if (places.last().place.isNotEmpty()) {
                        add(InputPlace(place = "", selectedFromAutocomplete = false))
                    }
                }
            } else {
                places.apply {
                    set(index, InputPlace(place = "", selectedFromAutocomplete = false))
                }
            }

            it.copy(inputPlaces = places, autoCompleteResults = emptyList())
        }
    }

    fun onClickResultAutoComplete(result: String, placeIndex: Int) {
        _state.update {
            val places = it.inputPlaces.toMutableList()
            places.apply {
                set(placeIndex, get(placeIndex).copy(place = result, selectedFromAutocomplete = true))
            }

            val count = places.count { it.selectedFromAutocomplete }
            if (count >= 2 && places.last().place.isNotEmpty()) {
                places.add(InputPlace(place = "", selectedFromAutocomplete = false))
            }
            it.copy(inputPlaces = places, autoCompleteResults = emptyList())
        }
    }

    fun onBackHandler() {
        _state.update { it.copy(autoCompleteResults = emptyList()) }
    }

    fun onSearchTypeChanged(searchType: SearchTypeUi) {
        _state.update { it.copy(searchType = searchType) }
    }

    fun onClickDeleteOptimalRoute(id: Int) {
        viewModelScope.launch {
            _state.update { it.copy(optimalRouteUi = null) }
            deleteOptimalRouteUseCase(id)
        }
    }
}

@Immutable
data class SearchState(
    val inputPlaces: List<InputPlace> = listOf(
        InputPlace(place = "", selectedFromAutocomplete = false),
        InputPlace(place = "", selectedFromAutocomplete = false),
    ),
    val autoCompleteResults: List<String> = emptyList(),
    val isOptimizingRoute: Boolean = false,
    val searchType: SearchTypeUi = SearchTypeUi.ALL,
    val optimalRouteUi: OptimalRouteUi? = null,
)

@Immutable
data class InputPlace(
    val id: Long = Random.nextLong(),
    val place: String,
    val selectedFromAutocomplete: Boolean,
)