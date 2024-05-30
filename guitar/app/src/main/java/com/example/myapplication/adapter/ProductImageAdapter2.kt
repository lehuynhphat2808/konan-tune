package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.R

//The Adapter Pattern allows to separate the data processing logic (model) and the way data is displayed on the interface (view).

class ProductImageAdapter2(private var productsImage: MutableList<String>) :
    RecyclerView.Adapter<ProductImageAdapter2.ViewHolder>() {
    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
            }
        }

        val ivProductImage: ImageView = itemView.findViewById(R.id.iv_product)
        init {
            itemView.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_image2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.ivProductImage.load(productsImage[position].replace("localhost", "10.0.2.2")) {
            placeholder(R.drawable.default_image)
            error(R.drawable.default_image)
        }
        holder.bind()



    }

    override fun getItemCount(): Int {
        return productsImage.size
    }

    fun setData(newProductsImage: List<String>) {
        productsImage = newProductsImage.toMutableList()
        notifyDataSetChanged()
    }


}
