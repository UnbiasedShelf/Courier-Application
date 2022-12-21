package by.bstu.vs.stpms.courier_application.ui.customer.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bstu.vs.stpms.courier_application.model.database.entity.Order
import by.bstu.vs.stpms.courier_application.model.repository.OrderRepository
import by.bstu.vs.stpms.courier_application.model.util.event.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderedListViewModel: ViewModel() {
    var orders: Result<List<Order>>? by mutableStateOf(null)
    var isRefreshing by mutableStateOf(false)

    fun getCreatedOrders() {
        isRefreshing = true
        viewModelScope.launch(Dispatchers.IO) {
            orders = OrderRepository.getCreatedOrders()
            isRefreshing = false
        }
    }
}