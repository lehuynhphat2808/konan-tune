package com.example.myapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.adapter.ViewPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.viewpager2.widget.ViewPager2

class ProductActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        viewPager = findViewById(R.id.viewPager)
        val viewPagerAdapter : ViewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = viewPagerAdapter
        viewPager.isUserInputEnabled = false


        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> bottomNavigationView.menu.findItem(R.id.item_home).isChecked = true
                    1 -> bottomNavigationView.menu.findItem(R.id.item_order).isChecked = true
                    2 -> bottomNavigationView.menu.findItem(R.id.item_voucher).isChecked = true
                    3 -> bottomNavigationView.menu.findItem(R.id.item_user).isChecked = true
                }
            }
        })

        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.item_home -> viewPager.setCurrentItem(0,false)
                R.id.item_order -> viewPager.setCurrentItem(1,false)
                R.id.item_voucher -> viewPager.setCurrentItem(2,false)
                R.id.item_user -> viewPager.setCurrentItem(3,false)
            }
            true
        }

    }
}
