package com.example.myapplication.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.model.Product
import com.example.myapplication.R
import com.example.myapplication.activity.ProductDetailActivity

//The Adapter Pattern allows to separate the data processing logic (model) and the way data is displayed on the interface (view).

class ProductSuggestionAdapter(var products: List<Product>) :
    RecyclerView.Adapter<ProductSuggestionAdapter.ViewHolder>() {
    private val TAG = "ProductSuggestionAdapter"

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val TAG = "ProductSuggestionAdapter ViewHolder"
        val tvProductSuggestionName: TextView = itemView.findViewById(R.id.tv_product_suggestion_name)
        val ivProductSuggestion: ImageView = itemView.findViewById(R.id.iv_product_suggestion)
        var productId: String = ""
        init {
            itemView.setOnClickListener(object : View.OnClickListener {
                val id = productId
                override fun onClick(v: View?) {
                    var intent: Intent = Intent(itemView.context, ProductDetailActivity::class.java)
                    intent.putExtra("productId", productId)
                    itemView.context.startActivity(intent)
                }

            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.e(TAG, "onCreateViewHolder" + products.size.toString())
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_suggestion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        Log.e(TAG, "onBindViewHolder")
        holder.productId = product.id.toString()
        holder.tvProductSuggestionName.text = product.name
        if(product.linkImages?.isNotEmpty() == true) {
            holder.ivProductSuggestion.load(product.linkImages?.get(0)?.replace("localhost", "10.0.2.2")) {
                placeholder(R.drawable.default_image)
                error(R.drawable.default_image)
            }
        } else {
            holder.ivProductSuggestion.load(R.drawable.default_image)
        }
    }

    override fun getItemCount(): Int {
        Log.e(TAG, "getItemCount" + products.size.toString())
        return products.size
    }

    fun setData(newProducts: List<Product>) {
        products = newProducts
        Log.e(TAG, "products size" + products.size.toString())

        notifyDataSetChanged()
    }

}
