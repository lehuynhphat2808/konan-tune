package com.example.myapplication.controller

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.service.CartService
import com.example.myapplication.model.Cart
import com.example.myapplication.model.CartProduct
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

open class CartProductController : ViewModel() {
    private val TAG = "CartProductViewModel"
    private val cartService = ApiClient.retrofit.create(CartService::class.java)
    //The Observer Pattern allows to separate data processing logic (Model) from how it is displayed on the interface (View). The View doesn't need to care where it gets the data from.
    //When there is a change in the Model, it will proactively notify registered Views to update the interface. Avoid having the View always check the Model.

    private val _cartProducts = MutableLiveData<List<CartProduct>>()
    val cartProducts: LiveData<List<CartProduct>> = _cartProducts

    fun fetchCartProducts(
        cartId: UUID,
    ) {
        cartService.getCartById(cartId)
            .enqueue(object : Callback<ApiResponse<Cart>> {
                override fun onResponse(
                    call: Call<ApiResponse<Cart>>,
                    response: Response<ApiResponse<Cart>>) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            val cart = apiResponse?.data
                            _cartProducts.value = cart?.cartProducts
                            Log.d(TAG, "Yêu cầu API thành công")
                        } else {
                            // Xử lý lỗi ở đây
                            Log.e(TAG, "Yêu cầu API thất bại: ${response.code()}")
                        }
                    }

                override fun onFailure(
                    call: Call<ApiResponse<Cart>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "Yêu cầu API onFailure: ${t.message}")
                }
            })
    }

}

