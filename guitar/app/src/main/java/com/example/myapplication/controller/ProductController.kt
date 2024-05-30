package com.example.myapplication.controller

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.response.PaginatedResponse
import com.example.myapplication.api.service.ProductService
import com.example.myapplication.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.util.UUID

open class ProductController : ViewModel() {
    private val TAG = "ProductViewModel"
    protected val productService = ApiClient.retrofit.create(ProductService::class.java)
    //The Observer Pattern allows to separate data processing logic (Model) from how it is displayed on the interface (View). The View doesn't need to care where it gets the data from.
    //When there is a change in the Model, it will proactively notify registered Views to update the interface. Avoid having the View always check the Model.
    protected val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    fun fetchProducts(
        categoryId: UUID? = null,
        keyword: String? = null,
        pageIndex: Int = 0,
        pageSize: Int = 10
    ) {
        productService.getPage(categoryId, keyword, pageIndex, pageSize)
            .enqueue(object : Callback<ApiResponse<PaginatedResponse<Product>>> {
                override fun onResponse(
                    call: Call<ApiResponse<PaginatedResponse<Product>>>,
                    response: Response<ApiResponse<PaginatedResponse<Product>>>) {
                        if (response.isSuccessful) {
                            val apiResponse = response.body()
                            val paginatedResponse = apiResponse?.data
                            _products.value = paginatedResponse?.elements
                            Log.d(TAG, "Yêu cầu API fetchProducts thành công")
                        } else {
                            // Xử lý lỗi ở đây
                            Log.e(TAG, "Yêu cầu API fetchProducts thất bại: ${response.code()}")
                        }
                    }

                override fun onFailure(
                    call: Call<ApiResponse<PaginatedResponse<Product>>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "Yêu cầu API fetchProducts onFailure: ${t.message}")
                }
            })
    }

    fun fetchMoreProducts(
        categoryId: UUID? = null,
        keyword: String? = null,
        pageIndex: Int = 0,
        pageSize: Int = 10
    ) {
        productService.getPage(categoryId, keyword, pageIndex, pageSize)
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
                        Log.d(TAG, "Yêu cầu API fetchMoreProducts thành công")
                    } else {
                        // Xử lý lỗi ở đây
                        Log.e(TAG, "Yêu cầu API fetchMoreProducts thất bại: ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<PaginatedResponse<Product>>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "Yêu cầu API fetchMoreProducts onFailure: ${t.message}")
                }
            })
    }

    fun fetchMoreProductsFilter(
        categoryId: UUID? = null,
        keyword: String? = null,
        brand: String? = null,
        color: String? = null,
        minPrice: BigDecimal? = null,
        maxPrice: BigDecimal? = null,
        sortField: String? = null,
        deleted: Boolean = false,
        pageIndex: Int = 0,
        pageSize: Int = 10
    ) {
        productService.getProductFilter(categoryId, keyword, brand, color, minPrice, maxPrice, sortField, pageIndex, pageSize, deleted)
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
                        Log.d(TAG, "Yêu cầu API fetchMoreProducts thành công")
                    } else {
                        // Xử lý lỗi ở đây
                        Log.e(TAG, "Yêu cầu API fetchMoreProducts thất bại: ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<PaginatedResponse<Product>>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "Yêu cầu API fetchMoreProducts onFailure: ${t.message}")
                }
            })
    }
    fun fetchProductSelling(
        userId: UUID,
        pageIndex: Int = 0,
        pageSize: Int = 10
    ) {
        productService.getProductSelling(userId, pageIndex, pageSize)
            .enqueue(object : Callback<ApiResponse<PaginatedResponse<Product>>> {
                override fun onResponse(
                    call: Call<ApiResponse<PaginatedResponse<Product>>>,
                    response: Response<ApiResponse<PaginatedResponse<Product>>>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        val paginatedResponse = apiResponse?.data
                        _products.value = paginatedResponse?.elements
                        Log.d("API_CALL", "Yêu cầu API fetchProducts thành công")
                    } else {
                        // Xử lý lỗi ở đây
                        Log.e("API_CALL", "Yêu cầu API fetchProducts thất bại: ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<PaginatedResponse<Product>>>,
                    t: Throwable
                ) {
                    Log.e("API_CALL", "Yêu cầu API fetchProducts onFailure: ${t.message}")
                }
            })
    }

    fun fetchProductsFilter(
        categoryId: UUID? = null,
        keyword: String? = null,
        brand: String? = null,
        color: String? = null,
        minPrice: BigDecimal? = null,
        maxPrice: BigDecimal? = null,
        sortField: String? = null,
        deleted: Boolean = false,
        pageIndex: Int = 0,
        pageSize: Int = 10
    ) {
        productService.getProductFilter(categoryId, keyword, brand, color, minPrice, maxPrice, sortField, pageIndex, pageSize, deleted)
            .enqueue(object : Callback<ApiResponse<PaginatedResponse<Product>>> {
                override fun onResponse(
                    call: Call<ApiResponse<PaginatedResponse<Product>>>,
                    response: Response<ApiResponse<PaginatedResponse<Product>>>) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        val paginatedResponse = apiResponse?.data
                        _products.value = paginatedResponse?.elements
                        Log.d(TAG, "Yêu cầu API fetchProductsFilter thành công")
                    } else {
                        // Xử lý lỗi ở đây
                        Log.e(TAG, "Yêu cầu API fetchProductsFilter thất bại: ${response.code()}")
                    }
                }

                override fun onFailure(
                    call: Call<ApiResponse<PaginatedResponse<Product>>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "Yêu cầu API fetchProductsFilter onFailure: ${t.message}")
                }
            })
    }



}

