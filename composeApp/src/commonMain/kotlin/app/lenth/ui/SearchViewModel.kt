package app.lenth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.lenth.domain.FindHamiltonianCycleMinimumCostUseCase
import app.lenth.domain.MinimumCostPath
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val cities = listOf("Valencia", "Barcelona", "Madrid", "Seville", "Bilbao", "Zaragoza", "Malaga", "Granada", "Alicante", "Murcia", "Ma")
class SearchViewModel(
    private val findHamiltonianCycleMinimumCostUseCase: FindHamiltonianCycleMinimumCostUseCase
): ViewModel() {
    private val _state: MutableStateFlow<SearchState> = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state

    // private val inputPlaces = MutableStateFlow(mutableListOf(InputPlace("", false), InputPlace("", false)))
    // private val autoCompleteResults = MutableStateFlow(emptyList<String>())
    //
    // val newState = combine(inputPlaces, autoCompleteResults) { inputPlaces, autoCompleteResults ->
    //     // Add another inputPlace based on the amount of selected by autocomplete
    //     inputPlaces.count { it.selectedFromAutocomplete }.let { selectedCount ->
    //         if (selectedCount < inputPlaces.size) {
    //             inputPlaces.add(InputPlace("", false))
    //         }
    //     }
    //
    //     SearchState(inputPlaces, autoCompleteResults)
    // }

    fun onQueryChanged(query: String, placeIndex: Int) {
        val currentValue = _state.value
        val filter = cities.filter { city -> city.contains(query, ignoreCase = true) && !currentValue.inputPlaces.any { inputPlace ->inputPlace.place.contains(city) }} // Simulate cities search
        _state.update {
            val places = it.inputPlaces.toMutableList()
            places.apply {
                set(placeIndex, InputPlace(query, false))
            }
            it.copy(inputPlaces = places, autoCompleteResults = filter)
        }
    }

    fun onSearch() {
        viewModelScope.launch {
            _state.update {
                it.copy(isOptimizingRoute = true, autoCompleteResults = emptyList())
            }
            // Check for network connection use konnectivity library
            // Check if you can travel it with car, walking or any other terrestrial vehicle. If not, send back an error
            val minimumCostPath = findHamiltonianCycleMinimumCostUseCase(_state.value.inputPlaces.map { it.place })
            _state.update {
                it.copy(isOptimizingRoute = false, minimumCostPath = minimumCostPath)
            }
        }
    }

    fun onDismissMinimumCostPath() {
        _state.update {
            it.copy(minimumCostPath = null)
        }
    }

    fun resetInputPlaces() {
        _state.update {
            it.copy(inputPlaces = listOf(InputPlace("", false), InputPlace("", false)))
        }
    }

    fun onClearInputPlace(placeIndex: Int) {
        clearInputPlace(placeIndex)
    }

    private fun clearInputPlace(index: Int) {
        _state.update {
            val places = it.inputPlaces.toMutableList()
            if(index < places.indices.last && places.size > 2) {
                places.apply {
                    removeAt(index)
                    if (places.last().place.isNotEmpty()) {
                        add(InputPlace("", false))
                    }
                }
            } else {
                places.apply {
                    set(index, InputPlace("", false))
                }
            }

            it.copy(inputPlaces = places)
        }
    }

    fun onClickResultAutoComplete(result: String, placeIndex: Int) {
        _state.update {
            val places = it.inputPlaces.toMutableList()
            places.apply {
                set(placeIndex, InputPlace(result, true))
            }

            val count = places.count { it.selectedFromAutocomplete }
            if (count >= 2 && places.last().place.isNotEmpty()) {
                places.add(InputPlace("", false))
            }
            it.copy(inputPlaces = places, autoCompleteResults = emptyList())
        }
    }

    fun onBackHandler(placeIndex: Int) {
        clearInputPlace(placeIndex)
        _state.update { it.copy(autoCompleteResults = emptyList()) }
    }
}

data class SearchState(
    val inputPlaces: List<InputPlace> = listOf(InputPlace("", false), InputPlace("", false)),
    val autoCompleteResults: List<String> = emptyList(),
    val isOptimizingRoute: Boolean = false,
    val minimumCostPath: MinimumCostPath? = null
)

data class InputPlace (
    val place: String,
    val selectedFromAutocomplete: Boolean
)