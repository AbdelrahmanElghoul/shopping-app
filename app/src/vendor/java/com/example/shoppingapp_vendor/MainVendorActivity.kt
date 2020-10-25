package com.example.shoppingapp_vendor

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Intent
import android.graphics.Interpolator
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.Item
import com.example.shoppingapp.R
import com.example.shoppingapp.util.Firebase
import com.example.shoppingapp_vendor.adapter.ItemsAdapter
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.vendor.activity_main_vendor.*
import timber.log.Timber

class MainVendorActivity : AppCompatActivity() {
lateinit var itemAdapter:ItemsAdapter
var itemList= mutableListOf<Item>()
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

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(Firebase.Items.ITEMS.Key)
        val postListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Timber.tag("firebase_mva").d("Added\n $snapshot")

                val item=Item()
                item.id= snapshot.key.toString()
                item.name= snapshot.child(item.id).child(Firebase.Items.ITEM_NAME.Key).value.toString()
                item.price= snapshot.child(item.id).child(Firebase.Items.ITEM_PRICE.Key).value.toString()
                if(snapshot.child(item.id).hasChild(Firebase.Items.ITEM_IMG_URL.toString()))
                    item.iconURL= snapshot.child(item.id).child(Firebase.Items.ITEM_IMG_URL.Key).value.toString()
                Timber.tag("firebase_mva").d("Added\n ${snapshot.child(item.id).child(Firebase.Items.ITEM_IMG_URL.Key).value.toString()}")
                itemList.add(item)
                itemAdapter.notifyDataSetChanged()

            }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                Timber.tag("firebase_mva").d("Changed\n$snapshot")
            }
            override fun onChildRemoved(snapshot: DataSnapshot) {
                Timber.d("Removed")
            }
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Timber.d("Moved")
            }
            override fun onCancelled(error: DatabaseError) {
                Timber.d("Cancelled")
            }

        }
//            myRef.addValueEventListener(postListener)
        myRef.addChildEventListener(postListener)
    }

    private fun setUI(){
        itemAdapter= ItemsAdapter(this,itemList)
        fab_add_item_amv.shrink()
        rv_items_amv.layoutManager=GridLayoutManager(this, 2)
        rv_items_amv.setHasFixedSize(true)
        rv_items_amv.adapter=itemAdapter
    }

}