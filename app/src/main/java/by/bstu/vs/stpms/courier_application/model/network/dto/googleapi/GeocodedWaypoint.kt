package by.bstu.vs.stpms.courier_application.model.network.dto.googleapi

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GeocodedWaypoint(
    @SerializedName("geocoder_status") val status: String,
    @SerializedName("place_id") val placeId: String,
    @SerializedName("types") val types: List<String>
) : Serializable