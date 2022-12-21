package by.bstu.vs.stpms.courier_application.model.network.api

import by.bstu.vs.stpms.courier_application.BuildConfig
import by.bstu.vs.stpms.courier_application.model.network.dto.googleapi.RouteResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleApi {
    @GET("maps/api/directions/json")
    suspend fun getRoute(
        @Query("origin") origin: String,
        @Query("key") key: String = BuildConfig.MAPS_API_KEY,
        @Query("mode") mode: String,
        @Query("destination") destination: String
    ): Response<RouteResponse>
}