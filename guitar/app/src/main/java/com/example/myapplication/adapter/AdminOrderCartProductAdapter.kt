package com.example.myapplication.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.R
import com.example.myapplication.activity.AdminProductDetailActivity
import com.example.myapplication.activity.ProductDetailActivity
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.service.CommentService
import com.example.myapplication.model.CartProduct
import com.example.myapplication.model.Comment
import com.example.myapplication.dto.CommentUpdateRequest
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

//The Adapter Pattern allows to separate the data processing logic (model) and the way data is displayed on the interface (view).

class AdminOrderCartProductAdapter(private var cartProducts: MutableList<CartProduct>) :
    RecyclerView.Adapter<AdminOrderCartProductAdapter.ViewHolder>() {
    private val TAG = "AdminOrderCartProductAdapter"

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val TAG = "AdminOrderCartProductAdapter ViewHolder"
        val tvCartProductName: TextView = itemView.findViewById(R.id.tv_order_product_title)
        val ivCartProductImage: ImageView = itemView.findViewById(R.id.iv_order_product)
        val tvCartProductPrice: TextView = itemView.findViewById(R.id.tv_order_cart_product_price)
        lateinit var cartProduct: CartProduct
        val tvQuantity: TextView = itemView.findViewById(R.id.tv_order_cart_product_quantity)
        var cartProductId: String = ""

        lateinit var productId: UUID
        private val sharedPref = itemView.context.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        private val userIdString = sharedPref.getString("userId", "")
        val userId = UUID.fromString(userIdString)
        init {
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    var intent: Intent = Intent(itemView.context, AdminProductDetailActivity::class.java)
                    intent.putExtra("productId", productId.toString())
                    itemView.context.startActivity(intent)
                }
            })

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_order_cart_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartProduct = cartProducts[position]
        holder.cartProduct = cartProduct
        holder.cartProductId = cartProduct.id.toString()
        val productId: UUID = cartProduct.product.id!!
        holder.productId = productId
        holder.tvCartProductName.text = cartProduct.product.name
        holder.tvCartProductPrice.text = cartProduct.product.price.toString()
        holder.tvQuantity.text = cartProduct.quantity.toString()
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
