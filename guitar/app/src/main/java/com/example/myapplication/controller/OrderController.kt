package com.example.myapplication.controller

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.response.PaginatedResponse
import com.example.myapplication.api.service.OrderService
import com.example.myapplication.model.Order
import com.example.myapplication.model.OrderStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

open class OrderController : ViewModel() {
    private val TAG = "OrderViewModel"
    private val orderService = ApiClient.retrofit.create(OrderService::class.java)
    //The Observer Pattern allows to separate data processing logic (Model) from how it is displayed on the interface (View). The View doesn't need to care where it gets the data from.
    //When there is a change in the Model, it will proactively notify registered Views to update the interface. Avoid having the View always check the Model.

    private val _order = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _order

    fun fetchOrders(
        userId: UUID,
        orderStatus: OrderStatus
    ) {
        orderService.getByUserId(userId, orderStatus)
            .enqueue(object : Callback<ApiResponse<PaginatedResponse<Order>>> {
                override fun onResponse(
                    call: Call<ApiResponse<PaginatedResponse<Order>>>,
                    response: Response<ApiResponse<PaginatedResponse<Order>>>) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            Log.d(TAG, apiResponse.toString())
                            val data = apiResponse?.data?.elements
                            _order.value = data!!
                            Log.d(TAG, "Yêu cầu API getByUserId thành công")
                        } else {
                            // Xử lý lỗi ở đây
                            Log.e(TAG, "Yêu cầu API getByUserId thất bại: ${response.code()}")
                        }
                    }

                override fun onFailure(
                    call: Call<ApiResponse<PaginatedResponse<Order>>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "Yêu cầu API getByUserId onFailure: ${t.message}")
                }
            })
    }

    fun fetchOrdersAdmin(
        orderStatus: OrderStatus,
        pageIndex: Int = 0,
        pageSize: Int = 10
    ) {
        orderService.getPage(orderStatus, pageIndex, pageSize)
            .enqueue(object : Callback<ApiResponse<PaginatedResponse<Order>>> {
                override fun onResponse(
                    call: Call<ApiResponse<PaginatedResponse<Order>>>,
                    response: Response<ApiResponse<PaginatedResponse<Order>>>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        Log.d(TAG, apiResponse.toString())
                        val data = apiResponse?.data?.elements
                        _order.value = data!!
                        Log.d(TAG, "Yêu cầu API fetchOrdersAdmin thành công")
                    } else {
                        // Xử lý lỗi ở đây
                        Log.e(TAG, "Yêu cầu API fetchOrdersAdmin thất bại: ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<PaginatedResponse<Order>>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "Yêu cầu API fetchOrdersAdmin onFailure: ${t.message}")
                }
            })
    }

    fun fetchMoreOrders(
        orderStatus: OrderStatus,
        pageIndex: Int = 0,
        pageSize: Int = 10
    ) {
        orderService.getPage(orderStatus, pageIndex, pageSize)
            .enqueue(object : Callback<ApiResponse<PaginatedResponse<Order>>> {
                override fun onResponse(
                    call: Call<ApiResponse<PaginatedResponse<Order>>>,
                    response: Response<ApiResponse<PaginatedResponse<Order>>>
                ) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        val paginatedResponse = apiResponse?.data
                        _order.value?.let { currentList ->
                            val updatedList = mutableListOf<Order>()
                            updatedList.addAll(currentList)
                            paginatedResponse?.elements?.let { updatedList.addAll(it) }
                            _order.value = updatedList
                        }
                        Log.d(TAG, "Yêu cầu API fetchMoreOrders thành công")
                    } else {
                        // Xử lý lỗi ở đây
                        Log.e(TAG, "Yêu cầu API fetchMoreOrders thất bại: ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<PaginatedResponse<Order>>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "Yêu cầu API fetchMoreOrders onFailure: ${t.message}")
                }
            })
    }

    fun fetchMoreOrdersByUserId(
        userId: UUID,
        orderStatus: OrderStatus,
        pageIndex: Int = 0,
        pageSize: Int = 10
    ) {
        orderService.getByUserId(userId, orderStatus, pageIndex, pageSize)
            .enqueue(object : Callback<ApiResponse<PaginatedResponse<Order>>> {
                override fun onResponse(
                    call: Call<ApiResponse<PaginatedResponse<Order>>>,
                    response: Response<ApiResponse<PaginatedResponse<Order>>>
                ) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        val paginatedResponse = apiResponse?.data
                        _order.value?.let { currentList ->
                            val updatedList = mutableListOf<Order>()
                            updatedList.addAll(currentList)
                            paginatedResponse?.elements?.let { updatedList.addAll(it) }
                            _order.value = updatedList
                        }
                        Log.d(TAG, "Yêu cầu API fetchMoreOrdersByUserId thành công")
                    } else {
                        // Xử lý lỗi ở đây
                        Log.e(TAG, "Yêu cầu API fetchMoreOrdersByUserId thất bại: ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<PaginatedResponse<Order>>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "Yêu cầu API fetchMoreOrdersByUserId onFailure: ${t.message}")
                }
            })
    }


}

