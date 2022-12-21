package by.bstu.vs.stpms.courier_application.ui.customer.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import by.bstu.vs.stpms.courier_application.model.database.entity.Order
import by.bstu.vs.stpms.courier_application.model.repository.OrderRepository
import by.bstu.vs.stpms.courier_application.model.util.event.Result

class CreatedOrderDetailsViewModel : ViewModel() {
    var order: Result<Order?>? by mutableStateOf(null)

    suspend fun getOrder(id: Long) {
        order = OrderRepository.getOrderById(id)
    }
}