package app.lenth.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.lenth.domain.FindHamiltonianCycleMinimumCostUseCase
import app.lenth.domain.MinimumCostPath
import app.lenth.domain.SearchPlacesByInputQueryUseCase
import app.lenth.ui.search.filter.SearchTypeUi
import app.lenth.ui.search.filter.toDomain
import kotlin.random.Random
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private val mockExamplesToFilter = listOf(
    "Valencia",
    "Barcelona",
    "Madrid",
    "Seville",
    "Bilbao",
    "Zaragoza",
    "Malaga",
    "Granada",
    "Alicante",
    "Albacete",
    "Murcia",
    "Cordoba",
    "Toledo",
    "Salamanca",
    "Santander",
    "Valladolid",
    "Pamplona",
    "Logrono",
    "Oviedo",
    "Gijon",
    "Avila",
    "Segovia",
    "Cuenca",
    "Caceres",
    "Merida",
    "Badajoz",
    "Huelva",
    "Cadiz",
    "Almeria",
    "Jaen",
    "Huesca",
    "Teruel",
    "Guadalajara",
    "Soria",
    "Palencia",
    "Leon",
    "Zamora",
    "Burgos",
    "Vitoria",
    "San Sebastian",
    "Pontevedra",
    "Ourense",
    "Lugo",
    "A Coruna",
    "Santiago de Compostela",
    "Lleida",
    "Tarragona",
    "Girona",
    "Castellon",
    "Alcoy",
    "Elche",
    "Orihuela",
    "Cartagena",
    "Lorca",
    "Almeria",
    "Roquetas de Mar",
    "El Ejido",
    "Motril",
    "Almunecar",
    "Marbella",
    "Estepona",
    "Ronda",
    "Antequera",
    "Torremolinos",
    "Fuengirola",
    "Benalmadena",
    "Mijas",
    "Torrox",
    "Nerja",
    "Velez-Malaga",
    "Torre del Mar",
    "Alhaurin de la Torre",
    "Coin",
    "Alhaurin el Grande",
    "Mijas",
    "Rincon de la Victoria",
    "Torrox",
    "Nerja",
    "Velez-Malaga",
    "Torre del Mar",
    "Alhaurin de la Torre",
    "Coin",
    "Alhaurin el Grande",
    "Mijas",
    "Rincon de la Victoria",
    "Torrox",
    "Nerja",
    "Velez-Malaga",
    "Torre del Mar",
    "Alhaurin de la Torre",
    "Coin",
    "Alhaurin el Grande",
    "Mijas",
    "Rincon de la Victoria",
    "Torrox",
    "Nerja",
    "Velez-Malaga",
    "Torre del Mar",
    "Alhaurin de la Torre",
    "Coin",
)

private val mockInitialState =listOf(
        // InputPlace("Valencia", true),
        InputPlace(place = "Barcelona",  selectedFromAutocomplete = true),
        InputPlace(place = "Zaragoza", selectedFromAutocomplete = true),
        InputPlace(place = "Madrid", selectedFromAutocomplete = true),
        InputPlace(place = "Seville",selectedFromAutocomplete =  true),
        InputPlace(place = "Bilbao", selectedFromAutocomplete = true),
        InputPlace(place = "Malaga", selectedFromAutocomplete = true),
        InputPlace(place = "Granada",selectedFromAutocomplete =  true),
        InputPlace(place = "Alicante",selectedFromAutocomplete =  true),
        InputPlace(place = "Albacete", selectedFromAutocomplete = true),
        InputPlace(place = "", selectedFromAutocomplete = false),
)

class SearchViewModel(
    private val findHamiltonianCycleMinimumCostUseCase: FindHamiltonianCycleMinimumCostUseCase,
    private val searchPlacesByInputQueryUseCase: SearchPlacesByInputQueryUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<SearchState> = MutableStateFlow(
        SearchState(
            inputPlaces = mockInitialState,
        )
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

            // Simulate cities search
            // val filter = mockExamplesToFilter.filter { city ->
            //     city.contains(
            //         query,
            //         ignoreCase = true,
            //     ) && !currentValue.inputPlaces.any { inputPlace -> inputPlace.place.contains(city) }
            // } // Simulate cities search

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
            it.copy(inputPlaces = listOf(InputPlace(place ="", selectedFromAutocomplete = false), InputPlace(place = "", selectedFromAutocomplete =  false)))
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
                        add(InputPlace(place = "", selectedFromAutocomplete =  false))
                    }
                }
            } else {
                places.apply {
                    set(index, InputPlace(place ="", selectedFromAutocomplete = false))
                }
            }

            it.copy(inputPlaces = places, autoCompleteResults = emptyList())
        }
    }

    fun onClickResultAutoComplete(result: String, placeIndex: Int) {
        _state.update {
            val places = it.inputPlaces.toMutableList()
            places.apply {
                set(placeIndex, get(placeIndex).copy(place = result, selectedFromAutocomplete =  true))
            }

            val count = places.count { it.selectedFromAutocomplete }
            if (count >= 2 && places.last().place.isNotEmpty()) {
                places.add(InputPlace(place = "", selectedFromAutocomplete =  false))
            }
            it.copy(inputPlaces = places, autoCompleteResults = emptyList())
        }
    }

    fun onBackHandler(placeIndex: Int) {
        clearInputPlace(placeIndex)
        _state.update { it.copy(autoCompleteResults = emptyList()) }
    }

    fun onSearchTypeChanged(searchType: SearchTypeUi) {
        _state.update { it.copy(searchType = searchType) }
    }
}

@Immutable
data class SearchState(
    val inputPlaces: List<InputPlace> = listOf(InputPlace(place = "", selectedFromAutocomplete =  false), InputPlace(place = "", selectedFromAutocomplete =  false)),
    val autoCompleteResults: List<String> = emptyList(),
    val isOptimizingRoute: Boolean = false,
    val searchType: SearchTypeUi = SearchTypeUi.ALL,
    val minimumCostPath: MinimumCostPath? = null,
)

@Immutable
data class InputPlace(
    val id: Long = Random.nextLong(),
    val place: String,
    val selectedFromAutocomplete: Boolean,
)