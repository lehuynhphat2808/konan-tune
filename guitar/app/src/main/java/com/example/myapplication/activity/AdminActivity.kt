package com.example.myapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.adapter.AdminViewPagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var viewPager: ViewPager2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        viewPager = findViewById(R.id.viewPager)
        val viewPagerAdapter = AdminViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = viewPagerAdapter
        viewPager.isUserInputEnabled = false


        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
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