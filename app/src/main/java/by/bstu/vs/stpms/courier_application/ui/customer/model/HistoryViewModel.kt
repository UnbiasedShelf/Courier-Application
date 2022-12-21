package by.bstu.vs.stpms.courier_application.ui.customer.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bstu.vs.stpms.courier_application.model.database.entity.Order
import by.bstu.vs.stpms.courier_application.model.repository.OrderRepository
import by.bstu.vs.stpms.courier_application.model.util.event.Result
import kotlinx.coroutines.launch

class HistoryViewModel: ViewModel() {
    var historyList: Result<List<Order>>? by mutableStateOf(null)
    var isRefreshing by mutableStateOf(false)

    fun getHistory() {
        isRefreshing = true
        viewModelScope.launch {
            historyList = OrderRepository.getHistory()
            isRefreshing = false
        }
    }
}