package com.example.myapplication.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.AdminOrderCartProductAdapter
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.service.CartService
import com.example.myapplication.api.service.OrderService
import com.example.myapplication.model.CartProduct
import com.example.myapplication.model.Order
import com.example.myapplication.model.OrderStatus
import com.example.myapplication.controller.CartProductController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.util.UUID
import android.widget.Toast


class AdminOrderDetailActivity : AppCompatActivity() {
    private lateinit var rcvOrderCartProduct: RecyclerView
    private lateinit var orderCartProductAdapter: AdminOrderCartProductAdapter
    private lateinit var orderId: UUID
    private val TAG = "AdminOrderDetailActivity"
    private lateinit var cartProductController: CartProductController
    private lateinit var cartProducts: List<CartProduct>
    private lateinit var tvSubTotalPrice: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvAddress: TextView
    private lateinit var tvContactPhoneNumber: TextView
    private lateinit var tvStatus: TextView
    private lateinit var btnDone: Button
    private lateinit var tvId: TextView
    private lateinit var ibtnCopy: ImageButton





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_order_detail)

        tvId = findViewById(R.id.tv_order_id)
        ibtnCopy = findViewById(R.id.ibtn_copy)
        var intent = intent
        if (intent.hasExtra("orderId")) {
            val orderIdString = intent.getStringExtra("orderId")
            orderId = UUID.fromString(orderIdString)
        }
        tvSubTotalPrice = findViewById(R.id.tv_price)
        tvDate = findViewById(R.id.tv_order_date)
        tvAddress = findViewById(R.id.tv_order_address)
        tvContactPhoneNumber = findViewById(R.id.tv_order_contact_phone_number)
        tvStatus = findViewById(R.id.tv_order_status)

        getOrderDetail()
        // Set up recyclerView
        rcvOrderCartProduct = findViewById(R.id.rcv_order_cart_product)
        cartProductController = ViewModelProvider(this)[CartProductController::class.java]
        rcvOrderCartProduct.layoutManager = LinearLayoutManager(this)
        orderCartProductAdapter = AdminOrderCartProductAdapter(mutableListOf())
        rcvOrderCartProduct.adapter = orderCartProductAdapter
        // Observe product view model
        cartProductController.cartProducts.observe(this) { cartProducts ->
            this.cartProducts = cartProducts
            orderCartProductAdapter.setData(cartProducts.toMutableList())
        }
        btnDone = findViewById(R.id.btn_done)
        ibtnCopy.setOnClickListener{
            Log.e(TAG, "ibtnCopy")
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("order_id", tvId.text)
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show()
        }

    }


    private fun getOrderDetail() {
        val orderService = ApiClient.retrofit.create(OrderService::class.java)
        orderService.getOrderById(orderId).enqueue(object : Callback<ApiResponse<Order>> {
            override fun onResponse(
                call: Call<ApiResponse<Order>>,
                response: Response<ApiResponse<Order>>
            ) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    val data = apiResponse?.data
                    // Sử dụng thông tin của sản phẩm ở đây
                    Log.e(TAG, "Yêu cầu API getOrderDetail thanh cong: ${response.code()}")
                    Log.e(TAG, "cartId: " + data?.cartId)
                    data?.let {order->
                        tvId.text = order.id.toString()
                        tvDate.text = order.orderDate.toString()
                        tvAddress.text = order.address
                        tvContactPhoneNumber.text = order.contactPhoneNumber
                        tvStatus.text = order.status.toString()
                        cartProductController.fetchCartProducts(order.cartId)
                        getSubTotalPrice(order.cartId)
                        if(order.status == OrderStatus.PENDING) {
                            order.id?.let { id -> setUpBtnDone(id, OrderStatus.PROCESSING) }
                            btnDone.text = "Done"
                        } else if(order.status == OrderStatus.PROCESSING) {
                            order.id?.let { id -> setUpBtnDone(id, OrderStatus.PENDING) }
                            btnDone.text = "Cancel"
                        } else {
                            btnDone.visibility = View.GONE
                        }
                    }

                } else {
                    // Xử lý lỗi ở đây
                    Log.e(TAG, "Yêu cầu API getOrderDetail thất bại: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Order>>, t: Throwable) {
                Log.e(TAG, "Yêu cầu API getOrderDetail onFailure")
            }

        })
    }

    fun getSubTotalPrice(cartId: UUID) {
        val cartService = ApiClient.retrofit.create(CartService::class.java)
        cartService.getSubTotalPrice(cartId).enqueue(object :
            Callback<ApiResponse<BigDecimal>> {
            override fun onResponse(
                call: Call<ApiResponse<BigDecimal>>,
                response: Response<ApiResponse<BigDecimal>>
            ) {
                if (response.isSuccessful) {
                    tvSubTotalPrice.text = response.body()?.data.toString()
                }
                else {
                    // Xử lý lỗi ở đây
                    Log.e(TAG, "Yêu cầu API getSubTotalPrice thất bại: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<BigDecimal>>, t: Throwable) {
                Log.e(TAG, "Yêu cầu API getSubTotalPrice onFailure: ${t.message}")
            }
        })
    }

    fun updateStatus(orderId: UUID, newStatus: OrderStatus) {
        val orderService = ApiClient.retrofit.create(OrderService::class.java)
        orderService.updateStatus(orderId, newStatus).enqueue(object :
            Callback<ApiResponse<Boolean>> {
            override fun onResponse(
                call: Call<ApiResponse<Boolean>>,
                response: Response<ApiResponse<Boolean>>
            ) {
                if (response.isSuccessful) {
//                    tvStatus.text = newStatus.toString()
                    getOrderDetail()
                }
                else {
                    // Xử lý lỗi ở đây
                    Log.e(TAG, "Yêu cầu API updateStatus thất bại: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Boolean>>, t: Throwable) {
                Log.e(TAG, "Yêu cầu API updateStatus onFailure: ${t.message}")
            }
        })
    }

    private fun setUpBtnDone(orderId: UUID, orderStatus: OrderStatus) {
        btnDone.setOnClickListener{updateStatus(orderId, orderStatus)}
    }




}