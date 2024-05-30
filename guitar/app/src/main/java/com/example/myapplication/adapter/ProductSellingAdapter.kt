package com.example.myapplication.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.model.Product
import com.example.myapplication.R
import com.example.myapplication.activity.ProductDetailActivity
import com.example.myapplication.activity.UpdateProductActivity
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.service.CartService
import com.example.myapplication.api.service.ProductService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.util.UUID

//The Adapter Pattern allows to separate the data processing logic (model) and the way data is displayed on the interface (view).

class ProductSellingAdapter(private var products: MutableList<Product>) :
    RecyclerView.Adapter<ProductSellingAdapter.ViewHolder>() {
    private val TAG = "ProductSellingAdapter"

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val TAG = "ProductSellingAdapter ViewHolder"
        val tvProductSellingName: TextView = itemView.findViewById(R.id.tv_product_selling_name)
        val ivProductSelling: ImageView = itemView.findViewById(R.id.iv_product_selling)
        val btnUpdate: Button = itemView.findViewById(R.id.btn_update)
        val btnDelete: Button = itemView.findViewById(R.id.btn_delete)
        val tvProductSellingPrice: TextView = itemView.findViewById(R.id.tv_product_selling_price)

        var productId: String = ""
        init {
            btnUpdate.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    var intent: Intent = Intent(itemView.context, UpdateProductActivity::class.java)
                    intent.putExtra("productId", productId)
                    itemView.context.startActivity(intent)
                }

            })

            btnDelete.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    deleteProduct(UUID.fromString(productId))
                }

            })


        }

        private fun deleteProduct(productId: UUID) {
            val productService = ApiClient.retrofit.create(ProductService::class.java)
            productService.deleteProduct(productId).enqueue(object : Callback<ApiResponse<Boolean>> {
                override fun onResponse(
                    call: Call<ApiResponse<Boolean>>,
                    response: Response<ApiResponse<Boolean>>
                ) {
                    if (response.isSuccessful) {
                        Log.e(TAG, "Yêu cầu API deleteProduct thanh cong: ${response.code()}")
                        val iterator = products.iterator()
                        while (iterator.hasNext()) {
                            val product = iterator.next()
                            if (product.id == productId) {
                                iterator.remove()
                                notifyDataSetChanged()
                                break
                            }
                        }
                    }
                    else {
                        // Xử lý lỗi ở đây
                        Log.e(TAG, "Yêu cầu API deleteProduct thất bại: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Boolean>>, t: Throwable) {
                    Log.e(TAG, "Yêu cầu API deleteProduct onFailure: ${t.message}")
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.e(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_selling, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e(TAG, "onBindViewHolder")
        val product = products[position]
        holder.productId = product.id.toString()
        holder.tvProductSellingName.text = product.name
        holder.tvProductSellingPrice.text = product.price.toString()
        if(product.linkImages?.isNotEmpty() == true) {
            Log.e(TAG, product.linkImages!![0])
            holder.ivProductSelling.load(product.linkImages!![0].replace("localhost", "10.0.2.2")) {
                placeholder(R.drawable.default_image)
                error(R.drawable.default_image)
            }
        }

    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setData(newProducts: MutableList<Product>) {
        products = newProducts
        Log.e(TAG, "setData product.size: " + products.size)
        notifyDataSetChanged()
    }



}
