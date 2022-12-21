package by.bstu.vs.stpms.courier_application.model.network.api

import by.bstu.vs.stpms.courier_application.model.network.dto.StatsDto
import by.bstu.vs.stpms.courier_application.model.network.dto.UserDto
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface UserApi {
    @GET("logout")
    suspend fun logout(): Response<Unit>

    @GET("profile")
    suspend fun currentUser(): Response<UserDto>

    @GET("profile/stats")
    suspend fun getStats(): Response<StatsDto>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("username") email: String,
        @Field("password") password: String,
        @Field("remember-me") rememberMe: Boolean
    ): Response<UserDto>

    @POST("registration")
    suspend fun signUp(@Body userDto: UserDto): Response<Unit>

    @PUT("profile/changetype")
    suspend fun changeType(@Body presentation: RequestBody): Response<Unit>


}