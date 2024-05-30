package com.example.myapplication.controller

import android.util.Log
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.response.PaginatedResponse
import com.example.myapplication.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BestSellingController : ProductController() {
    private val TAG = "BestSellingViewModel"

    fun fetchProductBestSelling(
        pageIndex: Int = 0,
        pageSize: Int = 10
    ) {
        productService.getHighestSelled(pageIndex, pageSize)
            .enqueue(object : Callback<ApiResponse<PaginatedResponse<Product>>> {
                override fun onResponse(
                    call: Call<ApiResponse<PaginatedResponse<Product>>>,
                    response: Response<ApiResponse<PaginatedResponse<Product>>>
                ) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        val paginatedResponse = apiResponse?.data
                        _products.value = paginatedResponse?.elements
                        Log.d(TAG, "Yêu cầu API getHighestSelled thành công")
                    } else {
                        // Xử lý lỗi ở đây
                        Log.e(TAG, "Yêu cầu API getHighestSelled thất bại: ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<PaginatedResponse<Product>>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "Yêu cầu API getHighestSelled onFailure: ${t.message}")
                }
            })
    }


    fun fetchMoreProductsBestSelling(
        pageIndex: Int = 0,
        pageSize: Int = 10
    ) {
        productService.getHighestSelled(pageIndex, pageSize)
            .enqueue(object : Callback<ApiResponse<PaginatedResponse<Product>>> {
                override fun onResponse(
                    call: Call<ApiResponse<PaginatedResponse<Product>>>,
                    response: Response<ApiResponse<PaginatedResponse<Product>>>
                ) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        val paginatedResponse = apiResponse?.data
                        _products.value?.let { currentList ->
                            val updatedList = mutableListOf<Product>()
                            updatedList.addAll(currentList)
                            paginatedResponse?.elements?.let { updatedList.addAll(it) }
                            _products.value = updatedList
                        }
                        Log.d(TAG, "Yêu cầu API fetchMoreProductsBestSelling thành công")
                    } else {
                        // Xử lý lỗi ở đây
                        Log.e(TAG, "Yêu cầu API fetchMoreProductsBestSelling thất bại: ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<PaginatedResponse<Product>>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "Yêu cầu API fetchMoreProductsBestSelling onFailure: ${t.message}")
                }
            })
    }


}