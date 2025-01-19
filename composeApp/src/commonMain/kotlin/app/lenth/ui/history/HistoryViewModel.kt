package app.lenth.ui.history

import androidx.lifecycle.ViewModel
import app.lenth.domain.history.GetAllOptimalRouteUseCase
import app.lenth.domain.mapper.toUiModel
import kotlinx.coroutines.flow.map

class HistoryViewModel(private val getAllOptimalRouteUseCase: GetAllOptimalRouteUseCase) : ViewModel() {

    internal val state = getAllOptimalRouteUseCase().map { list ->
        list.map { domain -> domain.toUiModel() }
    }
}