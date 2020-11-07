package com.example.shoppingapp_customer

import android.os.Bundle
import android.transition.Transition
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.shoppingapp.R
import com.example.shoppingapp_customer.adapters.ViewPagerAdapter
import com.example.shoppingapp_customer.fragments.CartScreen
import com.example.shoppingapp_customer.fragments.HomeScreen
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.platform.MaterialFade
import timber.log.Timber

class MainShopScreenActivity : AppCompatActivity() {
    //need check
    var pager: ViewPager2? = null
    var tabLayout: TabLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition: Transition = MaterialFade()
        window.exitTransition = transition
        setContentView(R.layout.activity_main_shop_screen)
        BindView()
        val adapter = ViewPagerAdapter(this)
        adapter.addList(HomeScreen(), R.drawable.home_img)
        adapter.addList(CartScreen(), R.drawable.shopping_cart_img)
        adapter.addList(HomeScreen(), R.drawable.bookmark_img)
        adapter.addList(HomeScreen(), R.drawable.avatar_img)
        Timber.d((pager == null).toString())
        pager!!.adapter = adapter
        TabLayoutMediator(tabLayout!!, pager!!
        ) { tab: TabLayout.Tab, position: Int -> tab.setIcon(adapter.getIcon(position)) }.attach()
    }

    private fun BindView() {
        pager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)
    }


    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 1) finish() else super.onBackPressed()
    }
}