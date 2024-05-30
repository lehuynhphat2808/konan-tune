package com.example.myapplication.api.service

import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.response.PaginatedResponse
import com.example.myapplication.model.Comment
import com.example.myapplication.dto.CommentResponse
import com.example.myapplication.dto.CommentUpdateRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface CommentService {

    @GET("/api/comment/getByUserIdAndProductId")
    fun getByUserIdAndProductId(
        @Query("userId") userId: UUID,
        @Query("productId") productId: UUID,
    ): Call<ApiResponse<Comment>>

    @POST("/api/comment/insert")
    fun insert(@Body comment: Comment): Call<ApiResponse<Boolean>>

    @PUT("/api/comment/updateInfo/{id}")
    fun update(@Path("id") commentId: UUID, @Body commentUpdateRequest: CommentUpdateRequest): Call<ApiResponse<Boolean>>

    @GET("/api/comment/getByProductId/{id}")
    fun getByProductId(
        @Path("id") productId: UUID,
        @Query("pageIndex") pageIndex: Int = 0,
        @Query("pageSize") pageSize: Int = 10
    ): Call<ApiResponse<PaginatedResponse<CommentResponse>>>
}
