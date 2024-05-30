package com.example.myapplication.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.ProductSellingAdapter
import com.example.myapplication.controller.ProductController
import java.util.UUID

class MySellingActivity : AppCompatActivity() {
    private lateinit var btnAddProduct: Button
    private lateinit var rcvProductSelling: RecyclerView
    private lateinit var productController: ProductController
    private lateinit var productSellingAdapter: ProductSellingAdapter
    private lateinit var USER_ID: UUID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_selling)
        btnAddProduct = findViewById(R.id.btn_add_product)
        btnAddProduct.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                val intent: Intent = Intent(this@MySellingActivity, AddProductActivity::class.java)
                startActivity(intent)
            }

        })
        // Set up rcvProductSelling
        rcvProductSelling = findViewById(R.id.rcv_product_selling)
        rcvProductSelling.layoutManager = LinearLayoutManager(this)
        productSellingAdapter = ProductSellingAdapter(mutableListOf())
        rcvProductSelling.adapter = productSellingAdapter
        productController = ViewModelProvider(this)[ProductController::class.java]
        productController.products.observe(this) { products->
            productSellingAdapter.setData(products.toMutableList())
        }
        val sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val userIdString = sharedPref?.getString("userId", "")
        if(!userIdString.equals("")) {
            USER_ID = UUID.fromString(userIdString)
        }
    }

    override fun onResume() {
        super.onResume()
        productController.fetchProductSelling(USER_ID)

    }
}