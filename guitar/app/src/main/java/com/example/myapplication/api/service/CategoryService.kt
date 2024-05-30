package com.example.myapplication.api.service

import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.response.PaginatedResponse
import com.example.myapplication.model.Category
import com.example.myapplication.model.Product
import com.example.myapplication.model.SellingProduct
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface CategoryService {
    @GET("/api/categories")  // Điều chỉnh đường dẫn tương ứng với API của bạn
    fun getAllCategoty(): Call<ApiResponse<List<Category>>> // Product là lớp dữ liệu tương ứng với dữ liệu API

}
