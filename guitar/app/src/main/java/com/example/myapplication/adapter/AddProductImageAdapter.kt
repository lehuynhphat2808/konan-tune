package com.example.myapplication.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.R
//The Adapter Pattern allows to separate the data processing logic (model) and the way data is displayed on the interface (view).
class AddProductImageAdapter(private var imageUris: ArrayList<Uri> = arrayListOf<Uri>()): RecyclerView.Adapter<AddProductImageAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivProductImage: ImageView = itemView.findViewById(R.id.iv_product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_image, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageUris.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uri = imageUris[position]
        holder.ivProductImage.load(uri)
    }

    fun setData(newImageUris: ArrayList<Uri>) {
        imageUris = newImageUris
        notifyDataSetChanged()
    }

}