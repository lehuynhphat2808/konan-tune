package com.example.myapplication.api.service

import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.response.PaginatedResponse
import com.example.myapplication.model.Product
import com.example.myapplication.model.SellingProduct
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.math.BigDecimal
import java.util.UUID

interface ProductService {

    @GET("/api/products")  // Điều chỉnh đường dẫn tương ứng với API của bạn
    fun getAllProducts(): Call<ApiResponse<List<Product>>> // Product là lớp dữ liệu tương ứng với dữ liệu API


    @GET("/api/products/page")
    fun getPage(
        @Query("categoryId") categoryId: UUID? = null,
        @Query("keyword") keyword: String? = null,
        @Query("pageIndex") pageIndex: Int = 0,
        @Query("pageSize") pageSize: Int = 10,
    ): Call<ApiResponse<PaginatedResponse<Product>>>

    @GET("/api/products/best-selling")
    fun getHighestSelled(
        @Query("pageIndex") pageIndex: Int = 0,
        @Query("pageSize") pageSize: Int = 10
    ): Call<ApiResponse<PaginatedResponse<Product>>>

    @GET("/api/products/page/user-selling")
    fun getProductSelling(
        @Query("userId") userId: UUID,
        @Query("pageIndex") pageIndex: Int = 0,
        @Query("pageSize") pageSize: Int = 10
    ): Call<ApiResponse<PaginatedResponse<Product>>>

    @GET("/api/products/page/filter")
    fun getProductFilter(
        @Query("categoryId") categoryId: UUID? = null,
        @Query("keyword") keyword: String? = null,
        @Query("brand") brand: String? = null,
        @Query("color") color: String? = null,
        @Query("minPrice") minPrice: BigDecimal? = null,
        @Query("maxPrice") maxPrice: BigDecimal? = null,
        @Query("sortField") sortField: String? = null,
        @Query("pageIndex") pageIndex: Int = 0,
        @Query("pageSize") pageSize: Int = 10,
        @Query("deleted") deleted: Boolean = false,

        ): Call<ApiResponse<PaginatedResponse<Product>>>


    @GET("/api/product/{id}")
    fun getProduct(@Path("id") productId: UUID): Call<ApiResponse<SellingProduct>>

    @POST("/api/product/insert")
    fun insertProduct(@Body newSellingProduct: SellingProduct?): Call<ApiResponse<String>>

    @PUT("/api/product/update/{id}")
    fun updateProduct(
        @Path("id") productId: UUID,
        @Body product: Product
    ): Call<ApiResponse<Boolean>>

    @DELETE("/api/product/delete/{id}")
    fun deleteProduct(
        @Path("id") productId: UUID,
    ): Call<ApiResponse<Boolean>>

    @PUT("/api/product/enableSelling/{id}")
    fun enableSelling(
        @Path("id") productId: UUID,
    ): Call<ApiResponse<Boolean>>
}
