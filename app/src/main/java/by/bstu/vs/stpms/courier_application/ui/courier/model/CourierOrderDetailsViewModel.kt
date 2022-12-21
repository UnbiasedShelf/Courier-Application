package by.bstu.vs.stpms.courier_application.ui.courier.model

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.neverEqualPolicy
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import by.bstu.vs.stpms.courier_application.model.database.entity.Order
import by.bstu.vs.stpms.courier_application.model.database.entity.enums.CourierType
import by.bstu.vs.stpms.courier_application.model.network.NetworkService
import by.bstu.vs.stpms.courier_application.model.network.dto.googleapi.Route
import by.bstu.vs.stpms.courier_application.model.repository.OrderRepository
import by.bstu.vs.stpms.courier_application.model.repository.UserRepository
import by.bstu.vs.stpms.courier_application.model.util.event.Result
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng


class CourierOrderDetailsViewModel : ViewModel() {
    var order: Result<Order?>? by mutableStateOf(null)
    var route: Result<Route?>? by mutableStateOf(null, policy = neverEqualPolicy())
    var cameraPosition: CameraPosition? by mutableStateOf(null, policy = neverEqualPolicy())

    suspend fun getOrder(id: Long) {
        order = OrderRepository.getOrderById(id)
    }

    suspend fun acceptOrder(): Result<Unit>? {
        return if (order?.isSuccessful == true && order?.data != null) {
            val orderData = order?.data!!
            OrderRepository.accept(orderData)
        } else {
            order?.t?.let { Result.error(it) }
        }
    }

    suspend fun declineOrder() = order?.data?.let { OrderRepository.decline(it) }

    suspend fun updateState(): Result<Order?>? {
        order = order?.data?.let { OrderRepository.updateState(it) }
        return order
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun loadMap(geocoder: Geocoder) {
        try {
            if (order?.isSuccessful == true && order?.data != null) {
                val orderData = order?.data!!
                val originAddress =
                    geocoder.getFromLocationName(orderData.sender.address, 1)!!.first()
                val origin = "${originAddress.latitude}, ${originAddress.longitude}"
                val destinationAddress =
                    geocoder.getFromLocationName(orderData.recipient.address, 1)!!.first()
                val destination = "${destinationAddress.latitude}, ${destinationAddress.longitude}"

                val defaultAddress =
                    geocoder.getFromLocationName("Minsk, Belarus", 1)!!.first()
                cameraPosition = CameraPosition.fromLatLngZoom(
                    LatLng(
                        defaultAddress.latitude,
                        defaultAddress.longitude
                    ), 10f
                )

                val courierType = UserRepository.getCurrentUser().run {
                    if (isSuccessful && data != null && data.courierType != CourierType.NotCourier)
                        data.courierType
                    else
                        CourierType.Car
                }
                var response = NetworkService.googleApi.value.getRoute(
                    origin = origin,
                    destination = destination,
                    mode = courierType.mode
                )
                response.body()?.let {
                    if (it.routes.isNullOrEmpty()) {
                        val newMode = when {
                            it.modes?.contains("WALKING") == true -> CourierType.Walk
                            it.modes?.contains("DRIVING") == true -> CourierType.Car
                            else -> {
                                throw Exception("Unable to build route")
                            }
                        }
                        response = NetworkService.googleApi.value.getRoute(
                            origin = origin,
                            destination = destination,
                            mode = newMode.mode
                        )
                    }
                }
                route = Result.success(response.body()?.routes?.firstOrNull())
            }
        } catch (e: Exception) {
            route = Result.error(e)
            Log.d("GoogleMaps", "Error: ${e.message}")
        }
    }

    fun checkNotNull() =
        order != null && route != null && route?.data != null && cameraPosition != null

    fun isOnline(context: Context) = NetworkService.isOnline(context)
}