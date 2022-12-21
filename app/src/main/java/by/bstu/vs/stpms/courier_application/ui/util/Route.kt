package by.bstu.vs.stpms.courier_application.ui.util

import androidx.navigation.NavHostController
import by.bstu.vs.stpms.courier_application.model.database.entity.Order
import com.google.gson.Gson

object Route {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTRATION = "registration"
    const val COURIER = "courier"
    const val CUSTOMER = "customer"
    const val CREATE_NEW = "create"
    const val CREATE_DETAILS = "created_details"
    const val AVAILABLE_DETAILS = "available_details"
    const val ACTIVE_DETAILS = "active_details"
}

fun NavHostController.navigateWithOrder(route: String, order: Order) {
    navigate("$route/${order.id}")
}
fun NavHostController.navigateWithOrderAndCallNumber(route: String, order: Order, callNumber: Boolean) {
    navigate("$route/${order.id}/$callNumber")
}