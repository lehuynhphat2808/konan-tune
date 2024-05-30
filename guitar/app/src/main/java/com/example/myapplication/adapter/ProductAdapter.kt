package com.example.myapplication.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.model.Product
import com.example.myapplication.R
import com.example.myapplication.activity.ProductDetailActivity

//The Adapter Pattern allows to separate the data processing logic (model) and the way data is displayed on the interface (view).

class ProductAdapter(private var products: MutableList<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private val TAG = "ProductAdapter"

    class ViewHolder(itemView: View, private val products: List<Product>) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tv_product_title)
        val priceTextView: TextView = itemView.findViewById(R.id.tv_cart_product_price)
        val imageView: ImageView = itemView.findViewById(R.id.iv_product)
        val tvSumarry: TextView = itemView.findViewById(R.id.tv_sumary)
        init {
            itemView.setOnClickListener(object : OnClickListener {
                override fun onClick(v: View?) {
                    var intent: Intent = Intent(itemView.context, ProductDetailActivity::class.java)
                    intent.putExtra("productId", products[adapterPosition].id.toString()) // Pass the selected product to the ProductDetailActivity
                    itemView.context.startActivity(intent)
                }

            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.e(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(view, products)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e(TAG, "onBindViewHolder")
        val product = products[position]
        holder.titleTextView.text = product.name
        holder.priceTextView.text = product.price.toString()
        holder.tvSumarry.text = product.summary
        if(product.linkImages?.isNotEmpty() == true) {
            holder.imageView.load(product.linkImages!![0].replace("localhost", "10.0.2.2")) {
                placeholder(R.drawable.default_image)
                error(R.drawable.default_image)
            }
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setData(newProducts: List<Product>) {
        products = newProducts.toMutableList()
        Log.e(TAG, "setData products.size: " + products.size)
        notifyDataSetChanged()
    }

    fun addProduct(newProducts: List<Product>) {
        products.addAll(newProducts)
        notifyItemInserted(products.size - 1)
    }



}
