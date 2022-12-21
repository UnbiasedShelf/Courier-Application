package by.bstu.vs.stpms.courier_application.model.network.dto.googleapi

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BoundPoint(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lng") val longitude: Double
) : Serializable {
    fun toLatLng() = LatLng(latitude, longitude)
}