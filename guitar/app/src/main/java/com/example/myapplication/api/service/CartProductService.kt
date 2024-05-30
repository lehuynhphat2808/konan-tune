package com.example.myapplication.api.service

import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.response.PaginatedResponse
import com.example.myapplication.model.Cart
import com.example.myapplication.model.Product
import com.example.myapplication.model.SellingProduct
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Objects
import java.util.UUID

interface CartProductService {

    @PUT("/api/cartProduct/updateIsSelected")
    fun updateIsSelected(
        @Query("cartProductId") cartProductId: UUID,
        @Query("selected") selected: Boolean,
    ): Call<ApiResponse<Boolean>>

}
