package com.example.shoppingapp_vendor

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoppingapp.Item
import com.example.shoppingapp.R
import com.example.shoppingapp.util.Firebase
import com.example.shoppingapp_vendor.adapter.ItemsAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.vendor.activity_main_vendor.*
import timber.log.Timber


class MainVendorActivity : AppCompatActivity() {
    lateinit var itemAdapter: ItemsAdapter
    val tag = "firebase_mva"
    var itemList = mutableListOf<Item>()
    private var isFABOpen = false
    private lateinit var transitionDrawable: TransitionDrawable
    private val fabHandler = Handler()
    private val progressbarHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_vendor)
        setUI()

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(Firebase.Items.ITEMS.Key)
                .orderByChild(Firebase.Items.ITEM_VENDOR_ID.Key)
                .equalTo(Firebase.getUid(this))
        val postListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                progress_bar_amv.visibility = View.GONE
                Timber.tag(tag).d("Added\n $snapshot")
                val item = snapshot.getValue(Item::class.java)
                item?.id = snapshot.key.toString()

                Timber.tag(tag).d("item category=${item?.categoryId}")

                itemList.add(item!!)
                itemAdapter.notifyDataSetChanged()


            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                Timber.tag(tag).d("Changed\n $snapshot")
                val item = snapshot.getValue(Item::class.java)
                item?.id = snapshot.key.toString()
                Timber.tag(tag).d("item=${item?.categoryId}")

                for ((index,mItem) in itemList.iterator().withIndex())
                {
                    if(mItem.id==item?.id){
                        itemList[index]=item
                        break
                    }
                }
                itemAdapter.notifyDataSetChanged()

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Timber.tag(tag).d("Removed")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                Timber.tag(tag).d("Moved")
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.tag(tag).d("Cancelled")
            }

        }

        myRef.addChildEventListener(postListener)

        main_fab_amv.setOnClickListener {
            val codeToRun = Runnable {
                if (isFABOpen) {
                    transitionDrawable.reverseTransition(200)
                    closeFABMenu()
                }
            }

            if (isFABOpen) {
                fabHandler.removeCallbacksAndMessages(null)
                codeToRun.run()
            } else {
                showFABMenu()
                transitionDrawable.startTransition(0)
                fabHandler.postDelayed(codeToRun, 2000)
            }
        }
        layout_fab_add_item_amv.setOnClickListener {
//            Toast.makeText(this, "add", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, VendorActivity::class.java))
        }
        layout_fab_logout_item_amv.setOnClickListener {
            Firebase.logout(this)
        }

        fab_add_item_amv.setOnClickListener {
//            Toast.makeText(this, "add", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, VendorActivity::class.java))
        }
        fab_logout_item_amv.setOnClickListener {
            Firebase.logout(this)
        }
    }

    private fun setUI() {
        progress_bar_amv.visibility=View.VISIBLE
        progressbarHandler.postDelayed({
            if(progress_bar_amv.visibility==View.VISIBLE)
            {
                progress_bar_amv.visibility=View.GONE
                Toast.makeText(this,"no Item to display",Toast.LENGTH_LONG).show()
            }
        },15000)
        transitionDrawable = TransitionDrawable(arrayOf(
                bitmapDrawableFromVector(this, R.drawable.more_img),
                bitmapDrawableFromVector(this, R.drawable.img_close)
        ))
        transitionDrawable.isCrossFadeEnabled = true
        main_fab_amv.setImageDrawable(transitionDrawable)
        layout_fab_add_item_amv.alpha=0f
        layout_fab_logout_item_amv.alpha=0f

        itemAdapter = ItemsAdapter(this, itemList)
//        fab_add_item_amv.shrink()
        rv_items_amv.layoutManager = GridLayoutManager(this, 2)
        rv_items_amv.setHasFixedSize(true)
        rv_items_amv.adapter = itemAdapter
    }

    private fun showFABMenu() {
        isFABOpen = true
        layout_fab_add_item_amv.animate().translationY(-resources.getDimension(R.dimen.standard_55)).alpha(1f)
        layout_fab_logout_item_amv.animate().translationY(-resources.getDimension(R.dimen.standard_105)).alpha(1f)

    }

    private fun closeFABMenu() {
        isFABOpen = false
        layout_fab_add_item_amv.animate().translationY(0f).alpha(0f)
        layout_fab_logout_item_amv.animate().translationY(0f).alpha(0f)

    }

    private fun bitmapDrawableFromVector(context: Context, drawableId: Int): BitmapDrawable {
        return BitmapDrawable(context.resources, getBitmapFromVectorDrawable(context, drawableId))
    }

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap = Bitmap.createBitmap(
                drawable!!.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888,
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun onStop() {
        super.onStop()
        progressbarHandler.removeCallbacksAndMessages(null)
    }

}