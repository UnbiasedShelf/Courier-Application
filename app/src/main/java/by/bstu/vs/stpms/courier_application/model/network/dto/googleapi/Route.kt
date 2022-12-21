package by.bstu.vs.stpms.courier_application.model.network.dto.googleapi

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Route(
    @SerializedName("overview_polyline") val polyline: Polyline,
    @SerializedName("bounds") val bounds: Bounds,
) : Serializable