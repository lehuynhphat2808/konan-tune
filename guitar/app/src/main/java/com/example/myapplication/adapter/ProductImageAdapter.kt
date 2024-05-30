package com.example.myapplication.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.R

//The Adapter Pattern allows to separate the data processing logic (model) and the way data is displayed on the interface (view).

class ProductImageAdapter(private var productsImage: MutableList<String>) :
    RecyclerView.Adapter<ProductImageAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivProductImage: ImageView = itemView.findViewById(R.id.iv_product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("LoadImage", productsImage[position])
        holder.ivProductImage.load(productsImage[position].replace("localhost", "10.0.2.2")) {
            placeholder(R.drawable.default_image)
            error(R.drawable.default_image)
        }

    }

    override fun getItemCount(): Int {
        return productsImage.size
    }

    fun setData(newProductsImage: List<String>) {
        productsImage = newProductsImage.toMutableList()
        notifyDataSetChanged()
    }

}
