package com.example.myapplication.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.R
import com.example.myapplication.activity.ProductDetailActivity
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.service.CartProductService
import com.example.myapplication.api.service.CartService
import com.example.myapplication.model.CartProduct
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.util.UUID

//The Adapter Pattern allows to separate the data processing logic (model) and the way data is displayed on the interface (view).

class CartProductAdapter(private var cartProducts: MutableList<CartProduct>, val cartId: UUID, var tvSubtotal: TextView, val context: Context?) :
    RecyclerView.Adapter<CartProductAdapter.ViewHolder>() {
    private val TAG = "CartProductAdapter"

    inner class ViewHolder(itemView: View,
                     private val cartId: UUID) : RecyclerView.ViewHolder(itemView) {
        private val TAG = "CartProductAdapter ViewHolder"
        val tvCartProductName: TextView = itemView.findViewById(R.id.tv_product_title)
        val ivCartProductImage: ImageView = itemView.findViewById(R.id.iv_product)
        val tvCartProductPrice: TextView = itemView.findViewById(R.id.tv_cart_product_price)
        val cb_selected: CheckBox = itemView.findViewById(R.id.cb_selected)
        lateinit var cartProduct: CartProduct
        private val btnPlus: Button = itemView.findViewById(R.id.btn_plus)
        private val btnMinus: Button = itemView.findViewById(R.id.btn_minus)
        val tvQuantity: TextView = itemView.findViewById(R.id.tv_quantity)
        var cartProductId: String = ""
        lateinit var productId: UUID
        init {
            getSubTotalPrice(cartId)
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    var intent: Intent = Intent(itemView.context, ProductDetailActivity::class.java)
                    intent.putExtra("productId", productId.toString())
                    itemView.context.startActivity(intent)
                }
            })
            btnPlus.setOnClickListener { addQuantityProduct(cartId, productId, 1, cartProduct) }
            btnMinus.setOnClickListener { addQuantityProduct(cartId, productId, -1, cartProduct) }
            cb_selected.setOnCheckedChangeListener(object : OnCheckedChangeListener {
                override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                    if(cartProduct.selected != isChecked) {
                        changeStateCheckBox(cartProduct, isChecked)
                    }
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
                        tvSubtotal.text = response.body()?.data.toString()
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

        fun changeStateCheckBox(cartProduct: CartProduct, selected: Boolean) {
            val cartProductService = ApiClient.retrofit.create(CartProductService::class.java)
            cartProductService.updateIsSelected(cartProduct.id, selected).enqueue(object :
                Callback<ApiResponse<Boolean>> {
                override fun onResponse(
                    call: Call<ApiResponse<Boolean>>,
                    response: Response<ApiResponse<Boolean>>
                ) {
                    if (response.isSuccessful) {
                        cartProduct.selected = selected
                        getSubTotalPrice(cartId)

                        notifyDataSetChanged()
                    }
                    else {
                        // Xử lý lỗi ở đây
                        Log.e(TAG, "Yêu cầu API thất bại: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Boolean>>, t: Throwable) {
                    Log.e(TAG, "Yêu cầu API onFailure: ${t.message}")
                }
            })
        }

        fun addQuantityProduct(cartId: UUID, productId: UUID, quantity: Int, cartProduct: CartProduct) {
            Log.e(TAG, "##### " + cartProduct.quantity)
            if((cartProduct.quantity + quantity) > cartProduct.product.quantity!!) {
                Toast.makeText(context, "Product quantity not enough", Toast.LENGTH_SHORT).show()
                return
            }
            val cartService = ApiClient.retrofit.create(CartService::class.java)
            cartService.addProductToCart(cartId, productId, quantity).enqueue(object :
                Callback<ApiResponse<Boolean>> {
                override fun onResponse(
                    call: Call<ApiResponse<Boolean>>,
                    response: Response<ApiResponse<Boolean>>
                ) {
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        val result = apiResponse?.data
                        Log.e(TAG, response.toString())
                        cartProduct.quantity += quantity
                        if(cartProduct.quantity <= 0) {
                            cartProducts.remove(cartProduct)
                        }
                        notifyDataSetChanged()
                        getSubTotalPrice(cartId)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart_product, parent, false)
        return ViewHolder(view, cartId)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartProduct = cartProducts[position]
        holder.cartProduct = cartProduct
        holder.cartProductId = cartProduct.id.toString()
        val productId: UUID = cartProduct.product.id!!
        holder.productId = productId
        holder.tvCartProductName.text = cartProduct.product.name
        holder.tvCartProductPrice.text = cartProduct.product.price.toString()
        holder.cb_selected.isChecked = cartProduct.selected
        if(cartProduct.product.quantity == 0) {
            cartProduct.quantity = 0
            holder.tvQuantity.text = "0"
        } else {
            holder.tvQuantity.text = cartProduct.quantity.toString()
        }
        if(cartProduct.product.linkImages?.isNotEmpty() == true){
            holder.ivCartProductImage.load(cartProduct.product.linkImages!![0].replace("localhost", "10.0.2.2")) {
                placeholder(R.drawable.default_image)
                error(R.drawable.default_image)
            }
        }




    }

    override fun getItemCount(): Int {
        return cartProducts.size
    }

    fun setData(newCartProducts: MutableList<CartProduct>) {
        cartProducts = newCartProducts
        notifyDataSetChanged()
    }



}
