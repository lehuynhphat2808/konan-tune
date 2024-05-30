package com.example.myapplication.api.service

import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.model.Cart
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.math.BigDecimal
import java.util.UUID

interface CartService {

    @PUT("/api/cart/addProductToCart")
    fun addProductToCart(
        @Query("cartId") cartId: UUID,
        @Query("productId") productId: UUID,
        @Query("quantity") quantity: Int
    ): Call<ApiResponse<Boolean>>

    @GET("/api/cart/{id}")
    fun getCartById(@Path("id") cartId: UUID): Call<ApiResponse<Cart>>

    @GET("/api/cart/getTotalAmount/{cartId}")
    fun getSubTotalPrice(@Path("cartId") cartId: UUID): Call<ApiResponse<BigDecimal>>

    @POST("/api/cart/insert")
    fun insert(@Body cartProductIdList: MutableList<UUID>): Call<ApiResponse<UUID>>

}
