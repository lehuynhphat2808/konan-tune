package com.example.myapplication.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.myapplication.R
import com.example.myapplication.dto.CommentResponse

//The Adapter Pattern allows to separate the data processing logic (model) and the way data is displayed on the interface (view).

class CommentAdapter(private var comments: MutableList<CommentResponse>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    private val TAG = "CommentAdapter"

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val TAG = "CommentAdapter ViewHolder"
        val tvComment: TextView = itemView.findViewById(R.id.tv_comment)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar2)
        val tvUserName: TextView = itemView.findViewById(R.id.tv_userName)
        val ivUser: ImageView = itemView.findViewById(R.id.iv_comment_user)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        holder.tvUserName.text = comment.user.nickname
        holder.tvComment.text = comment.cmt
        holder.ratingBar.rating = comment.rate.toFloat()
        if(comment.user.linkImage != null) {
            holder.ivUser.load(comment.user.linkImage.replace("localhost", "10.0.2.2")) {
                placeholder(R.drawable.default_image)
                error(R.drawable.default_image)
            }
        }
    }

    fun setData(newComments: MutableList<CommentResponse>) {
        comments = newComments
        notifyDataSetChanged()
    }

}



