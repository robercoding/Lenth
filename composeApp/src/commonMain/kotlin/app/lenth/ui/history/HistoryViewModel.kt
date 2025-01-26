package app.lenth.ui.history

import androidx.lifecycle.ViewModel
import app.lenth.domain.PlaceUi
import app.lenth.domain.history.GetAllOptimalRouteUseCase
import app.lenth.domain.history.GetStaticImageUseCase
import app.lenth.domain.mapper.toUiModel
import kotlinx.coroutines.flow.map

class HistoryViewModel(
    private val getAllOptimalRouteUseCase: GetAllOptimalRouteUseCase,
    private val getStaticImageUseCase: GetStaticImageUseCase,
) : ViewModel() {

    internal val state = getAllOptimalRouteUseCase().map { list ->
        list.map { domain -> domain.toUiModel() }
    }

    fun onClickItem(placeUi: List<PlaceUi>) {
        // viewModelScope.launch(Dispatchers.IO) {
            // getStaticImageUseCase(placeUi.map { it.toDomain() })
        // }
        // Add the logic to obtain the image here
    }
}