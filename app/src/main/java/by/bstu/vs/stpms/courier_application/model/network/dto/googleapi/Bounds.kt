package by.bstu.vs.stpms.courier_application.model.network.dto.googleapi

import com.google.android.gms.maps.model.LatLngBounds
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Bounds(
    @SerializedName("northeast") val northEast: BoundPoint,
    @SerializedName("southwest") val southWest: BoundPoint
) : Serializable {
    fun toLatLngBounds() = LatLngBounds(southWest.toLatLng(), northEast.toLatLng())
}