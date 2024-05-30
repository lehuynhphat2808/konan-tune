package com.example.myapplication.api.service

import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.response.PaginatedResponse
import com.example.myapplication.model.Product
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileUploadService {
    @Multipart
    @POST("/api/file/insert")
    fun uploadFiles(@Part file: MultipartBody.Part): Call<ApiResponse<String>>
}