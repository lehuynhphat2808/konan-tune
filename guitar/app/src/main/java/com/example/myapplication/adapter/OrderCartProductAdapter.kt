package com.example.myapplication.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.example.myapplication.activity.ProductDetailActivity
import com.example.myapplication.api.ApiClient
import com.example.myapplication.api.response.ApiResponse
import com.example.myapplication.api.service.CommentService
import com.example.myapplication.model.CartProduct
import com.example.myapplication.model.Comment
import com.example.myapplication.dto.CommentUpdateRequest
import com.example.myapplication.model.OrderStatus
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

//The Adapter Pattern allows to separate the data processing logic (model) and the way data is displayed on the interface (view).

class OrderCartProductAdapter(private var cartProducts: MutableList<CartProduct>, var orderStatus: OrderStatus) :
    RecyclerView.Adapter<OrderCartProductAdapter.ViewHolder>() {
    private val TAG = "OrderCartProductAdapter"

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val TAG = "OrderCartProductAdapter ViewHolder"
        private lateinit var dialog: Dialog
        val tvCartProductName: TextView = itemView.findViewById(R.id.tv_order_product_title)
        val ivCartProductImage: ImageView = itemView.findViewById(R.id.iv_order_product)
        val tvCartProductPrice: TextView = itemView.findViewById(R.id.tv_order_cart_product_price)
        lateinit var cartProduct: CartProduct
        val tvQuantity: TextView = itemView.findViewById(R.id.tv_order_cart_product_quantity)
        var cartProductId: String = ""
        private var btnFeedback: Button = itemView.findViewById(R.id.btn_feedback)
        private lateinit var btnSubmit: Button
        private lateinit var btnCancel: Button
        private lateinit var tipComment: TextInputEditText
        private lateinit var ratingBar: RatingBar

        lateinit var productId: UUID
        private val sharedPref = itemView.context.getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        private val userIdString = sharedPref.getString("userId", "")
        val userId = UUID.fromString(userIdString)
        init {
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    var intent: Intent = Intent(itemView.context, ProductDetailActivity::class.java)
                    intent.putExtra("productId", productId.toString())
                    itemView.context.startActivity(intent)
                }
            })
            if(orderStatus == OrderStatus.COMPLETED) {
                btnFeedback.visibility = View.VISIBLE
                btnFeedback.setOnClickListener(object : OnClickListener {
                    override fun onClick(v: View?) {
                        showDialog()
                    }

                })
            }
        }

        private fun showDialog() {
            dialog = Dialog(itemView.context)
            dialog.setContentView(R.layout.dialog_comment)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(true)
            btnSubmit = dialog.findViewById(R.id.btn_comment_submit)
            btnCancel = dialog.findViewById(R.id.btn_comment_cancel)
            tipComment = dialog.findViewById(R.id.tip_comment)
            ratingBar = dialog.findViewById(R.id.ratingBar)

            btnCancel.setOnClickListener{dialog.dismiss()}


            val commentService = ApiClient.retrofit.create(CommentService::class.java)
            commentService.getByUserIdAndProductId(userId, productId).enqueue(object :
                Callback<ApiResponse<Comment>> {
                override fun onResponse(
                    call: Call<ApiResponse<Comment>>,
                    response: Response<ApiResponse<Comment>>
                ) {
                    Log.e(TAG, "Yêu cầu API commentService.getByUserIdAndProductId thanh cong: ${response.code()}")
                    if (response.isSuccessful) {
                        val comment: Comment? = response.body()?.data
                        if (comment != null) {
                            ratingBar.rating = comment.rate.toFloat()
                            tipComment.setText(comment.cmt)
                            btnSubmit.setOnClickListener{
                                updateComment(comment.id!!, CommentUpdateRequest(tipComment.text.toString(), ratingBar.rating.toInt()))
                            }
                        } else {
                            btnSubmit.setOnClickListener {
                                insertComment(tipComment.text.toString(), ratingBar.rating.toInt())
                            }
                        }
                    }
                    else {
                        // Xử lý lỗi ở đây
                        Log.e(TAG, "Yêu cầu API commentService.getByUserIdAndProductId thất bại: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Comment>>, t: Throwable) {
                    Log.e(TAG, "Yêu cầu API commentService.getByUserIdAndProductId onFailure: ${t.message}")
                }
            })
            dialog.show()
        }

        private fun insertComment(cmt: String, rate: Int) {
            val commentService = ApiClient.retrofit.create(CommentService::class.java)
            commentService.insert(Comment(null, userId, productId, cmt, rate)).enqueue(object :
                Callback<ApiResponse<Boolean>> {
                override fun onResponse(
                    call: Call<ApiResponse<Boolean>>,
                    response: Response<ApiResponse<Boolean>>
                ) {
                    Log.e(TAG, "Yêu cầu API commentService.getByUserIdAndProductId thanh cong: ${response.code()}")
                    if (response.isSuccessful) {
                        dialog.dismiss()
                    }
                    else {
                        // Xử lý lỗi ở đây
                        Log.e(TAG, "Yêu cầu API commentService.getByUserIdAndProductId thất bại: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ApiResponse<Boolean>>, t: Throwable) {
                    Log.e(TAG, "Yêu cầu API commentService.getByUserIdAndProductId onFailure: ${t.message}")
                }
            })

        }

        private fun updateComment(commentId: UUID, commentUpdateRequest: CommentUpdateRequest) {
            val commentService = ApiClient.retrofit.create(CommentService::class.java)
            commentService.update(commentId, commentUpdateRequest).enqueue(object :
                Callback<ApiResponse<Boolean>> {
                override fun onResponse(
                    call: Call<ApiResponse<Boolean>>,
                    response: Response<ApiResponse<Boolean>>
                ) {
                    if (response.isSuccessful) {
                        Log.e(TAG, "Yêu cầu API updateComment thanh cong: ${response.code()}")
                        dialog.dismiss()
                    }
                    else {
                        // Xử lý lỗi ở đây
                        Log.e(TAG, "Yêu cầu API updateComment thất bại: ${response.code()}")

                    }
                }

                override fun onFailure(call: Call<ApiResponse<Boolean>>, t: Throwable) {
                    Log.e(TAG, "Yêu cầu API updateComment onFailure: ${t.message}")
                }
            })

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_cart_product, parent, false)
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
