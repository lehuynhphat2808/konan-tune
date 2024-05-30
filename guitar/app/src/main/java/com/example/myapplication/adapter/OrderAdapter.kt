package com.example.myapplication.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.activity.OrderDetailActivity
import com.example.myapplication.activity.ProductDetailActivity
import com.example.myapplication.model.Order
import java.util.UUID

//The Adapter Pattern allows to separate the data processing logic (model) and the way data is displayed on the interface (view).

class OrderAdapter(private var orders: MutableList<Order>) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    private val TAG = "CartProductAdapter"

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val TAG = "OrderAdapter ViewHolder"
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvAddress: TextView = itemView.findViewById(R.id.tv_address)
        val tvStatus: TextView = itemView.findViewById(R.id.tv_status)
        var orderId: UUID? = null
        init {
            itemView.setOnClickListener(object : View.OnClickListener {
                val id = orderId
                override fun onClick(v: View?) {
                    var intent: Intent = Intent(itemView.context, OrderDetailActivity::class.java)
                    intent.putExtra("orderId", orderId.toString())
                    itemView.context.startActivity(intent)
                }

            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = orders[position]
        holder.orderId = order.id
        holder.tvDate.text = order.orderDate.toString()
        holder.tvAddress.text = order.address
        holder.tvStatus.text = order.status.toString()
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    fun setData(newOrders: MutableList<Order>) {
        orders = newOrders
        notifyDataSetChanged()
    }



}
