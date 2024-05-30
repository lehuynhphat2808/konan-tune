package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.ProductImageAdapter
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.service.ProductService
import com.example.myapplication.model.SellingProduct
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class AdminProductDetailActivity : AppCompatActivity() {
    private lateinit var rcvProductImage: RecyclerView
    private lateinit var tvProductTitle: TextView
    private lateinit var tvProductPrice: TextView
    private lateinit var tvProductContent: TextView
    private lateinit var tvProductSumary: TextView
    private lateinit var tvProductBrand: TextView
    private lateinit var tvProductColor: TextView
    private lateinit var btnAddToCart: Button
    private lateinit var productId: UUID


    private val TAG = "ADMIN_PRODUCT_DETAIL_ACTIVITY"

    private lateinit var productImageAdapter: ProductImageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_product_detail)

        rcvProductImage = findViewById(R.id.rcv_product_image)
        tvProductTitle = findViewById(R.id.tv_product_name)
        tvProductPrice = findViewById(R.id.tv_product_price)
        tvProductContent = findViewById(R.id.tv_product_content)
        tvProductSumary = findViewById(R.id.tv_product_sumary)
        tvProductBrand = findViewById(R.id.tv_product_brand)
        tvProductColor = findViewById(R.id.tv_product_color)
        rcvProductImage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        btnAddToCart = findViewById(R.id.btn_add_to_cart)
        productImageAdapter = ProductImageAdapter(ArrayList())
        rcvProductImage.adapter = productImageAdapter

        ////////////////////////Show Product Detail//////////////////////////////
        var intent = intent
        if (intent.hasExtra("productId")) {
            val productIdString = intent.getStringExtra("productId")
            productId = UUID.fromString(productIdString)
            showProductDetail(productId)
        }
        //////////////////////////////////////////////////////////////
        btnAddToCart.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                enableSelling(productId)
            }

        })

    }

    private fun showProductDetail(productId: UUID) {
        val productService = ApiClient.retrofit.create(ProductService::class.java)
        productService.getProduct(productId).enqueue(object : Callback<ApiResponse<SellingProduct>> {
            override fun onResponse(
                call: Call<ApiResponse<SellingProduct>>,
                response: Response<ApiResponse<SellingProduct>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val product = apiResponse?.data
                    // Sử dụng thông tin của sản phẩm ở đây
                    product?.let {
                        it.linkImages?.let { it1 -> productImageAdapter.setData(it1) }
                        tvProductTitle.text = product.name
                        tvProductPrice.text = product.price.toString()
                        tvProductSumary.text= product.summary
                        tvProductContent.text = product.content
                        tvProductBrand.text= product.brand
                        tvProductColor.text= product.color
                        if(product.deleted == true) {
                            btnAddToCart.text = "ENABLE"
                        } else {
                            btnAddToCart.text = "DISABLE"
                        }

                    }
                } else {
                    // Xử lý lỗi ở đây
                    Log.e("API_CALL", "Yêu cầu API thất bại: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<SellingProduct>>, t: Throwable) {
                Log.e("API_CALL", "Yêu cầu API onFailure: ${t.message}")
            }
        })
    }



    private fun enableSelling(productId: UUID) {
        val productService = ApiClient.retrofit.create(ProductService::class.java)
        productService.enableSelling(productId).enqueue(object : Callback<ApiResponse<Boolean>> {
            override fun onResponse(
                call: Call<ApiResponse<Boolean>>,
                response: Response<ApiResponse<Boolean>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val result = apiResponse?.data
                    // Sử dụng thông tin của sản phẩm ở đây
                    showProductDetail(productId)
                } else {
                    // Xử lý lỗi ở đây
                    Log.e(TAG, response.toString())
                    Log.e(TAG, "Yêu cầu API enableSelling thất bại: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Boolean>>, t: Throwable) {
                Log.e(TAG, "Yêu cầu API enableSelling onFailure: ${t.message}")
            }
        })
    }
}