package com.example.myapplication.controller

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.response.PaginatedResponse
import com.example.myapplication.api.service.VoucherService
import com.example.myapplication.model.Voucher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class VoucherController : ViewModel() {
    private val TAG = "VoucherViewModel"
    private val voucherService = ApiClient.retrofit.create(VoucherService::class.java)
    //The Observer Pattern allows to separate data processing logic (Model) from how it is displayed on the interface (View). The View doesn't need to care where it gets the data from.
    //When there is a change in the Model, it will proactively notify registered Views to update the interface. Avoid having the View always check the Model.

    private val _vouchers = MutableLiveData<List<Voucher>>()
    val vouchers: LiveData<List<Voucher>> = _vouchers

    fun fetchVoucher(
    ) {
        voucherService.getPage()
            .enqueue(object : Callback<ApiResponse<PaginatedResponse<Voucher>>> {
                override fun onResponse(
                    call: Call<ApiResponse<PaginatedResponse<Voucher>>>,
                    response: Response<ApiResponse<PaginatedResponse<Voucher>>>) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            val voucherPage = apiResponse?.data
                            if (voucherPage != null) {
                                _vouchers.value = voucherPage.elements
                            }
                            Log.d(TAG, "Yêu cầu API fetchVoucher thành công ${response.code()}")
                        } else {
                            // Xử lý lỗi ở đây
                            Log.e(TAG, "Yêu cầu API fetchVoucher thất bại: ${response.code()}")
                        }
                    }

                override fun onFailure(
                    call: Call<ApiResponse<PaginatedResponse<Voucher>>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "Yêu cầu API fetchVoucher onFailure: ${t.message}")
                }
            })
    }

}

