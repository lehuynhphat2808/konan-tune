package com.example.myapplication.api.service

import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.response.PaginatedResponse
import com.example.myapplication.model.Voucher
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface VoucherService {

    @GET("/api/vouchers/page")
    fun getPage(
        @Query("pageIndex") pageIndex: Int = 0,
        @Query("pageSize") pageSize: Int = 10
    ): Call<ApiResponse<PaginatedResponse<Voucher>>>

    @GET("/api/voucher/applyVoucher")
    fun applyVoucher(
        @Query("code") code: String,
        @Query("userId") userId: UUID
    ): Call<ApiResponse<Voucher>>

    @POST("/api/voucher/insert")
    fun insert(@Body voucher: Voucher): Call<ApiResponse<Boolean>>

    @GET("/api/voucher/addUser")
    fun addUser(
        @Query("code") code: String,
        @Query("userId") userId: UUID
    ): Call<ApiResponse<Boolean>>

    @GET("/api/voucher/{id}")
    fun getById(
        @Path("id") id: UUID
    ): Call<ApiResponse<Voucher>>

    @PUT("/api/voucher/update/{id}")
    fun update(
        @Path("id") userId: UUID,
        @Body voucher: Voucher
    ): Call<ApiResponse<Boolean>>
}
