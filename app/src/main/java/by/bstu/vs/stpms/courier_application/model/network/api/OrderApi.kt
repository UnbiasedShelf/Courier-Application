package by.bstu.vs.stpms.courier_application.model.network.api

import by.bstu.vs.stpms.courier_application.model.network.dto.OrderDto
import retrofit2.Response
import retrofit2.http.*

interface OrderApi {

    @GET("{id}")
    suspend fun getById(@Path("id") id: Long): Response<OrderDto?>

    @GET("available")
    suspend fun getAvailableOrders(): Response<List<OrderDto>?>

    @GET("active")
    suspend fun getActiveOrders(): Response<List<OrderDto>?>

    @GET("delivered")
    suspend fun getDeliveredOrders(): Response<List<OrderDto>?>

    @GET("created")
    suspend fun getCreatedOrders(): Response<List<OrderDto>?>

    @GET("history")
    suspend fun getHistory(): Response<List<OrderDto>?>

    @GET("accept/{id}")
    suspend fun acceptOrder(@Path("id") id: Long): Response<Unit>

    @GET("decline/{id}")
    suspend fun declineOrder(@Path("id") id: Long): Response<Unit>

    @POST(".")
    suspend fun save(@Body orderDto: OrderDto): Response<Unit>

    @PUT("updateState")
    suspend fun updateState(
        @Query("id") id: Long,
        @Query("state") newState: String?
    ): Response<Unit>
}