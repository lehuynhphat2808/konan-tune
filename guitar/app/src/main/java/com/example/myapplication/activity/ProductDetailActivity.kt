package com.example.myapplication.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.CommentAdapter
import com.example.myapplication.adapter.ProductAdapter
import com.example.myapplication.adapter.ProductImageAdapter
import com.example.myapplication.adapter.ProductImageAdapter2
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.service.CartService
import com.example.myapplication.api.service.ProductService
import com.example.myapplication.model.SellingProduct
import com.example.myapplication.controller.CommentController
import com.example.myapplication.controller.SameProductController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var rcvProductImage: RecyclerView
    private lateinit var rcvProductImage2: RecyclerView
    private lateinit var tvProductTitle: TextView
    private lateinit var tvProductPrice: TextView
    private lateinit var tvProductContent: TextView
    private lateinit var tvProductQuantity: TextView
    private lateinit var tvProductSumary: TextView
    private lateinit var tvProductBrand: TextView
    private lateinit var tvProductColor: TextView
    private lateinit var btnAddToCart: Button
    private lateinit var btnReadFull: Button
    private lateinit var sameProductViewModel: SameProductController
    private lateinit var sameProductAdapter: ProductAdapter
    private lateinit var rcvSameProduct: RecyclerView
    private lateinit var productId: UUID

    private lateinit var rcvComment: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var commentController: CommentController

    private val TAG = "PRODUCT_DETAIL_ACTIVITY"

    private lateinit var productImageAdapter: ProductImageAdapter
    private lateinit var productImageAdapter2: ProductImageAdapter2



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        btnReadFull = findViewById(R.id.btn_read_full)
        rcvProductImage = findViewById(R.id.rcv_product_image)
        rcvProductImage2 = findViewById(R.id.rcv_product_image2)
        tvProductTitle = findViewById(R.id.tv_product_name)
        tvProductPrice = findViewById(R.id.tv_product_price)
        tvProductContent = findViewById(R.id.tv_product_content)
        tvProductQuantity = findViewById(R.id.tv_quantity_product)
        tvProductSumary = findViewById(R.id.tv_product_sumary)
        tvProductBrand = findViewById(R.id.tv_product_brand)
        tvProductColor = findViewById(R.id.tv_product_color)
        rcvProductImage.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rcvProductImage2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        btnAddToCart = findViewById(R.id.btn_add_to_cart)
        rcvSameProduct = findViewById(R.id.rcv_same_product)
        sameProductViewModel = ViewModelProvider(this)[SameProductController::class.java]
        sameProductAdapter = ProductAdapter(ArrayList())
        rcvSameProduct.adapter = sameProductAdapter
        rcvSameProduct.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        rcvSameProduct.layoutManager = GridLayoutManager(this, 2, LinearLayoutManager.HORIZONTAL, false)
        rcvSameProduct = findViewById(R.id.rcv_same_product)

        productImageAdapter = ProductImageAdapter(ArrayList())
        rcvProductImage.adapter = productImageAdapter
        productImageAdapter2 = ProductImageAdapter2(mutableListOf())
        rcvProductImage2.adapter = productImageAdapter2

        sameProductViewModel.products.observe(this) {products ->
            sameProductAdapter.setData(products)
        }
        sameProductViewModel.fetchProducts()

        ////////////////////////Show Product Detail//////////////////////////////
        var intent = intent
        if (intent.hasExtra("productId")) {
            val productIdString = intent.getStringExtra("productId")
            productId = UUID.fromString(productIdString)
            showProductDetail(productId)
        }
        //////////////////////////////////////////////////////////////
        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val cartIdString = sharedPref.getString("cartId", "")
        btnAddToCart.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                if(cartIdString.equals("")) {
                    val intent = Intent(this@ProductDetailActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    // Add to cart
                    Toast.makeText(this@ProductDetailActivity, "add to cart", Toast.LENGTH_SHORT).show()
                    val cartId = UUID.fromString(cartIdString)
                    addProductToCart(cartId, productId, 1)
                }
            }

        })

        // Setup rcv Comment
        rcvComment = findViewById(R.id.rcv_comment);
        rcvComment.layoutManager = LinearLayoutManager(this)
        commentAdapter = CommentAdapter(mutableListOf())
        rcvComment.adapter = commentAdapter
        commentController = ViewModelProvider(this)[CommentController::class.java]
        commentController.comments.observe(this){ comments ->
            commentAdapter.setData(comments.toMutableList())
        }
        commentController.fetchComment(productId)
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
                        it.linkImages?.let { it1 ->
                            productImageAdapter.setData(it1)
                            productImageAdapter2.setData(it1)
                            Log.e(TAG, "linkImages: ${it.linkImages}");
                        }
                        tvProductTitle.text = product.name
                        tvProductPrice.text = product.price.toString()
                        tvProductQuantity.text = product.quantity.toString()
                        tvProductSumary.text= product.summary
                        tvProductContent.text = product.content
                        tvProductBrand.text= product.brand
                        tvProductColor.text= product.color
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

        productImageAdapter2.setOnItemClickListener {
            val position = it
            // scroll đến position tương ứng trong rcvProductImage
            rcvProductImage.scrollToPosition(position)
        }
        productImageAdapter2.setOnItemClickListener {
            rcvProductImage.scrollToPosition(it)
        }
        btnReadFull.setOnClickListener {
            tvProductContent.layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            btnReadFull.visibility = GONE
        }
    }


    private fun addProductToCart(cartId: UUID, productId: UUID, quantity: Int) {
        val cartService = ApiClient.retrofit.create(CartService::class.java)
        cartService.addProductToCart(cartId, productId, quantity).enqueue(object : Callback<ApiResponse<Boolean>> {
            override fun onResponse(
                call: Call<ApiResponse<Boolean>>,
                response: Response<ApiResponse<Boolean>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val result = apiResponse?.data
                    // Sử dụng thông tin của sản phẩm ở đây
                    result?.let {
                        Toast.makeText(this@ProductDetailActivity, "add product to cart successfully", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Xử lý lỗi ở đây
                    Log.e(TAG, response.toString())
                    Log.e(TAG, "Yêu cầu API thất bại: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Boolean>>, t: Throwable) {
                Log.e(TAG, "Yêu cầu API onFailure: ${t.message}")
            }
        })
    }
}