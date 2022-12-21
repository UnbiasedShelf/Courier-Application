package by.bstu.vs.stpms.courier_application.model.network.dto.googleapi

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RouteResponse(
    @SerializedName("available_travel_modes") val modes: List<String>?,
    @SerializedName("geocoded_waypoints") val geocodedWaypoints: List<GeocodedWaypoint>,
    @SerializedName("routes") val routes: List<Route>,
) : Serializable