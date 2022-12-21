package by.bstu.vs.stpms.courier_application.model.network.dto.googleapi

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Polyline(
    @SerializedName("points") val points: String,
) : Serializable