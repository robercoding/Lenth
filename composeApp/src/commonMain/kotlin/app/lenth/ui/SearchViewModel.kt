package app.lenth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.lenth.domain.FindHamiltonianCycleMinimumCostUseCase
import app.lenth.domain.MinimumCostPath
import app.lenth.domain.SearchPlacesByInputQueryUseCase
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

private val mockInitialState = SearchState(
    inputPlaces = listOf(
        InputPlace("Valencia", true),
        InputPlace("Barcelona", true),
        InputPlace("Zaragoza", true),
        InputPlace("Madrid", true),
        InputPlace("Seville", true),
        InputPlace("Bilbao", true),
        InputPlace("Malaga", true),
        InputPlace("Granada", true),
        InputPlace("Alicante", true),
        InputPlace("Albacete", true),
        InputPlace("", false),
    ),
)
class SearchViewModel(
    private val findHamiltonianCycleMinimumCostUseCase: FindHamiltonianCycleMinimumCostUseCase,
    private val searchPlacesByInputQueryUseCase: SearchPlacesByInputQueryUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<SearchState> = MutableStateFlow(
        SearchState(
            // inputPlaces = mockInitialState
        ),
    )
    val state: StateFlow<SearchState> = _state

    var job: Job? = null

    fun onQueryChanged(query: String, placeIndex: Int) {
        _state.update {
            val places = it.inputPlaces.toMutableList()
            places.apply {
                set(placeIndex, InputPlace(query, false))
            }
            it.copy(inputPlaces = places)
        }
        job?.cancel()
        job = viewModelScope.launch {
            val currentValue = _state.value

            val filter = searchPlacesByInputQueryUseCase(query)
                .filter { city ->
                    city.contains(
                        query,
                        ignoreCase = true,
                    ) && !currentValue.inputPlaces.any { inputPlace -> inputPlace.place.contains(city) }
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
                places.apply {
                    set(placeIndex, InputPlace(query, false))
                }
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
            it.copy(inputPlaces = listOf(InputPlace("", false), InputPlace("", false)))
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
    val minimumCostPath: MinimumCostPath? = null,
)

data class InputPlace(
    val place: String,
    val selectedFromAutocomplete: Boolean,
)