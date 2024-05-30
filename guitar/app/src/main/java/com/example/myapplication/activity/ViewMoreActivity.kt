package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.ProductAdapter
import com.example.myapplication.model.Product
import com.example.myapplication.controller.BestSellingController
import java.util.UUID

class ViewMoreActivity : AppCompatActivity() {
    private lateinit var rcvViewMore: RecyclerView
    private lateinit var viewMoreAdapter: ProductAdapter
    private lateinit var viewMoreViewModel: BestSellingController
    private var productList: List<Product> = ArrayList()
    private var currentPageIndex: Int = 0
    private var categoryId: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_more)
        categoryId = intent.getStringExtra("categoryId")
        rcvViewMore = findViewById(R.id.rcv_view_more)
        rcvViewMore.layoutManager = GridLayoutManager(this, 2)
        viewMoreAdapter = ProductAdapter(ArrayList())
        rcvViewMore.adapter = viewMoreAdapter
        viewMoreViewModel = ViewModelProvider(this)[BestSellingController::class.java]
        viewMoreViewModel.products.observe(this) {products ->
            productList = products
            viewMoreAdapter.setData(productList)
        }
        if(categoryId.equals("BEST_SELLING")) {
            viewMoreViewModel.fetchProductBestSelling()
            rcvViewMore.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        // Đã cuộn đến cuối danh sách, hãy tải thêm sản phẩm ở đây
                        loadMoreBestSelling()
                    }
                }
            })
        } else {
            viewMoreViewModel.fetchProducts(categoryId = UUID.fromString(this.categoryId))
            rcvViewMore.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                        // Đã cuộn đến cuối danh sách, hãy tải thêm sản phẩm ở đây
                        loadMoreProducts(categoryId)
                    }
                }
            })
        }


    }

    private fun loadMoreProducts(categoryId: String? = "") {
        currentPageIndex++
        viewMoreViewModel.fetchMoreProducts(
            categoryId = UUID.fromString(categoryId),
            pageIndex = currentPageIndex
        )
        Log.e("XXX", "loadMoreProducts " + categoryId)
    }

    private fun loadMoreBestSelling() {
        currentPageIndex++
        viewMoreViewModel.fetchMoreProductsBestSelling(
            pageIndex = currentPageIndex
        )
        Log.e("XXX", "loadMoreProducts Selling")
    }
}