package by.bstu.vs.stpms.courier_application.model.database.entity.enums

import by.bstu.vs.stpms.courier_application.R

enum class OrderState {
    Ordered, Delivering, Delivered, Canceled;

    val next: OrderState
        get() {
            val states = values()
            val nextIndex = ordinal + 1
            return if (nextIndex == states.size) {
                states[0]
            } else {
                states[nextIndex]
            }
        }
}

fun OrderState.getResId(): Int = when(this) {
    OrderState.Ordered -> R.string.ordered
    OrderState.Delivering -> R.string.delivering
    OrderState.Delivered -> R.string.delivered
    OrderState.Canceled -> R.string.canceled
}