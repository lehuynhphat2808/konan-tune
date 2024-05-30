package com.example.myapplication.api.service

import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.response.PaginatedResponse
import com.example.myapplication.dto.InsertOrderRequest
import com.example.myapplication.model.Order
import com.example.myapplication.model.OrderStatus
import com.example.myapplication.model.Product
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface OrderService {
    @POST("/api/order/insert")
    fun insertOrder(@Body order: InsertOrderRequest): Call<ApiResponse<Boolean>>
    @GET("/api/orders/getByUserId")
    fun getByUserId(
        @Query("userId") userId: UUID,
        @Query("orderStatus") orderStatus: OrderStatus,
        @Query("pageIndex") pageIndex: Int = 0,
        @Query("pageSize") pageSize: Int = 10
    ): Call<ApiResponse<PaginatedResponse<Order>>>

    @GET("/api/orders/page")
    fun getPage(
        @Query("orderStatus") orderStatus: OrderStatus,
        @Query("pageIndex") pageIndex: Int = 0,
        @Query("pageSize") pageSize: Int = 10
    ): Call<ApiResponse<PaginatedResponse<Order>>>

    @GET("/api/order/{id}")
    fun getOrderById(@Path("id") orderId: UUID): Call<ApiResponse<Order>>

    @PUT("/api/order/updateStatus/{id}")
    fun updateStatus(
        @Path("id") orderId: UUID,
        @Query("newStatus") newStatus: OrderStatus,
        ): Call<ApiResponse<Boolean>>

}
