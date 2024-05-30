package com.example.myapplication.api.service

import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.dto.LoginRequest
import com.example.myapplication.dto.LoginResponse
import com.example.myapplication.dto.RegisterRequest
import com.example.myapplication.dto.UserDetail
import com.example.myapplication.model.Product
import com.example.myapplication.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface UserService {

    @POST("api/login")
    fun login(@Body loginRequest: LoginRequest): Call<ApiResponse<LoginResponse>>

    @POST("/api/user/register")
    fun register(@Body registerRequest: RegisterRequest): Call<ApiResponse<LoginResponse>>

    @POST("/api/user/forgotPassword")
    fun forgotPassword(@Query("email") email: String): Call<ApiResponse<Boolean>>

    @GET("api/user/{id}")
    fun getById(@Path("id") userId: UUID): Call<ApiResponse<UserDetail>>

    @PUT("/api/user/updateInfo/{id}")
    fun updateUser(
        @Path("id") userId: UUID,
        @Body userDetail: UserDetail
    ): Call<ApiResponse<Boolean>>

    @PUT("/api/user/updateInfo/{id}")
    fun changePassword(
        @Path("id") userId: UUID,
        @Body newPassword: UserDetail
    ): Call<ApiResponse<Boolean>>
}