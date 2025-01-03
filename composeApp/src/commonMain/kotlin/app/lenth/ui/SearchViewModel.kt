package app.lenth.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SearchViewModel: ViewModel() {
    private val _state: MutableStateFlow<SearchState> = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state

    fun onQueryChanged(query: String) {
        _state.update {
            it.copy(query = query)
        }
    }

    fun onSearch() {
        _state.update {
            it.copy(results = listOf("Result 1", "Result 2", "Result 3"))
        }
    }

    fun onResultSelected(result: String) {
        // Handle result selection
    }
}

data class SearchState(
    val query: String = "",
    val results: List<String> = emptyList()
)