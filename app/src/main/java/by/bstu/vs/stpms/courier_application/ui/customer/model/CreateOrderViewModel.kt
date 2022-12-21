package by.bstu.vs.stpms.courier_application.ui.customer.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import by.bstu.vs.stpms.courier_application.model.database.entity.Destination
import by.bstu.vs.stpms.courier_application.model.database.entity.Order
import by.bstu.vs.stpms.courier_application.model.database.entity.Ordered
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.OrderState
import by.bstu.vs.stpms.courier_application.model.repository.OrderRepository
import by.bstu.vs.stpms.courier_application.model.util.event.Result
import by.bstu.vs.stpms.courier_application.ui.util.formatPhone
import java.sql.Timestamp
import java.time.Instant

class CreateOrderViewModel : ViewModel() {
    var additionalInfo by mutableStateOf("")

    var senderAddress by mutableStateOf("")
    var senderName by mutableStateOf("")
    var senderPhone by mutableStateOf("")
    var pickUpRangeStart: Timestamp? by mutableStateOf(getNextTimestamp())
    var pickUpRangeEnd: Timestamp? by mutableStateOf(Timestamp(getNextTimestamp().time + (1000 * 60 * 60)))

    var recipientAddress by mutableStateOf("")
    var recipientName by mutableStateOf("")
    var recipientPhone by mutableStateOf("")
    var deliveryRangeStart: Timestamp? by mutableStateOf(Timestamp(getNextTimestamp().time + (2 * 1000 * 60 * 60)))
    var deliveryRangeEnd: Timestamp? by mutableStateOf(Timestamp(getNextTimestamp().time + (3 * 1000 * 60 * 60)))

    var products = mutableStateListOf<Ordered>()


    val totalItems: Int
        get() = products.sumOf { it.amount }

    val totalWeight: Double
        get() = products.sumOf { it.amount * it.product.weight }

    suspend fun save(): Result<Unit> {
        val order = Order().apply {
            this.info = additionalInfo
            this.ordered = products
            this.state = OrderState.Ordered
            this.sender = Destination().apply {
                this.address = senderAddress
                this.name = senderName
                this.phone = senderPhone.formatPhone()
                this.rangeStart = pickUpRangeStart
                this.rangeEnd = pickUpRangeEnd
            }
            this.recipient = Destination().apply {
                this.address = recipientAddress
                this.name = recipientName
                this.phone = recipientPhone.formatPhone()
                this.rangeStart = deliveryRangeStart
                this.rangeEnd = deliveryRangeEnd
            }
        }
        return OrderRepository.save(order)
    }

    private fun getNextTimestamp(): Timestamp {
        val now = Timestamp(Timestamp.from(Instant.now()).time)
        return Timestamp(now.time - (now.time % (1000 * 60 * 30)) + (1000 * 60 * 30))
    }

    fun calculateCost() = when {
        totalWeight < 9.0  -> 5.0
        totalWeight in 9.0..25.0 -> 10.0
        else -> 25.0
    }
}