package by.bstu.vs.stpms.courier_application.ui.courier.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bstu.vs.stpms.courier_application.model.database.entity.Order
import by.bstu.vs.stpms.courier_application.model.repository.OrderRepository
import by.bstu.vs.stpms.courier_application.model.util.event.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AvailableOrderViewModel : ViewModel() {

    var orders: Result<List<Order>>? by mutableStateOf(null)
    var isRefreshing by mutableStateOf(false)

    fun getAvailableOrders() {
        isRefreshing = true
        viewModelScope.launch(Dispatchers.IO) {
            orders = OrderRepository.getAvailableOrders()
            isRefreshing = false
        }
    }
}