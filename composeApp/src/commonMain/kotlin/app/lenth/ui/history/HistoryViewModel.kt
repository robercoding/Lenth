package app.lenth.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.lenth.domain.PlaceUi
import app.lenth.domain.history.ClearAllOptimalRoutesUseCase
import app.lenth.domain.history.DeleteOptimalRouteUseCase
import app.lenth.domain.history.GetAllOptimalRouteUseCase
import app.lenth.domain.history.GetStaticImageUseCase
import app.lenth.domain.mapper.toUiModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val getAllOptimalRouteUseCase: GetAllOptimalRouteUseCase,
    private val clearAllOptimalRoutesUseCase: ClearAllOptimalRoutesUseCase,
    private val deleteOptimalRouteUseCase: DeleteOptimalRouteUseCase,
) : ViewModel() {

    internal val state = getAllOptimalRouteUseCase().map { list ->
        list.map { domain -> domain.toUiModel() }
    }

    fun onClickClearAll() {
        viewModelScope.launch {
            clearAllOptimalRoutesUseCase()
        }
    }

    fun onClickDeleteOptimalRoute(id: Int) {
        viewModelScope.launch {
            deleteOptimalRouteUseCase(id)
        }
    }

    fun onClickItem(placeUi: List<PlaceUi>) {
        // viewModelScope.launch(Dispatchers.IO) {
            // getStaticImageUseCase(placeUi.map { it.toDomain() })
        // }
        // Add the logic to obtain the image here
    }
}