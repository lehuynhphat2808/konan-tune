package com.example.myapplication.controller

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.response.PaginatedResponse
import com.example.myapplication.api.service.CommentService
import com.example.myapplication.dto.CommentResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

open class CommentController : ViewModel() {
    private val TAG = "CommentViewModel"
    private val commentService = ApiClient.retrofit.create(CommentService::class.java)
    //The Observer Pattern allows to separate data processing logic (Model) from how it is displayed on the interface (View). The View doesn't need to care where it gets the data from.
    //When there is a change in the Model, it will proactively notify registered Views to update the interface. Avoid having the View always check the Model.

    private val _comments = MutableLiveData<List<CommentResponse>>()
    val comments: LiveData<List<CommentResponse>> = _comments

    fun fetchComment(
        productId: UUID,
    ) {
        commentService.getByProductId(productId)
            .enqueue(object : Callback<ApiResponse<PaginatedResponse<CommentResponse>>> {
                override fun onResponse(
                    call: Call<ApiResponse<PaginatedResponse<CommentResponse>>>,
                    response: Response<ApiResponse<PaginatedResponse<CommentResponse>>>) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            val commentPage = apiResponse?.data
                            if (commentPage != null) {
                                _comments.value = commentPage.elements
                            }
                            Log.d(TAG, "Yêu cầu API fetchComment thành công ${response.code()}")
                        } else {
                            // Xử lý lỗi ở đây
                            Log.e(TAG, "Yêu cầu API fetchComment thất bại: ${response.code()}")
                        }
                    }

                override fun onFailure(
                    call: Call<ApiResponse<PaginatedResponse<CommentResponse>>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "Yêu cầu API fetchComment onFailure: ${t.message}")
                }
            })
    }

}

