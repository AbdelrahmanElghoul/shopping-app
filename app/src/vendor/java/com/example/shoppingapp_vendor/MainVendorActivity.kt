package com.example.shoppingapp_vendor

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoppingapp.R
import com.example.shoppingapp_vendor.adapter.ItemsAdapter
import kotlinx.android.synthetic.vendor.activity_main_vendor.*

class MainVendorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_vendor)
        setUI()

        fab_add_item_amv.setOnClickListener {
            if(fab_add_item_amv.isExtended) startActivity(Intent(this,VendorActivity::class.java))
           fab_add_item_amv.extend()
            val mHandler = Handler()
            val codeToRun = Runnable {
                fab_add_item_amv.shrink()
            }
            mHandler.postDelayed(codeToRun, 2000)
        }

    }

    private fun setUI(){
        fab_add_item_amv.shrink()
        rv_items_amv.layoutManager=GridLayoutManager(this, 2)
        rv_items_amv.setHasFixedSize(true)
        rv_items_amv.adapter=ItemsAdapter(this)
    }
}