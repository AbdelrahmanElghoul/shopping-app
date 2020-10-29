package com.example.shoppingapp_vendor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Interpolator
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoppingapp.Item
import com.example.shoppingapp.R
import com.example.shoppingapp.util.Firebase
import com.example.shoppingapp_vendor.adapter.ItemsAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.vendor.activity_main_vendor.*
import timber.log.Timber


class MainVendorActivity : AppCompatActivity() {
    lateinit var itemAdapter: ItemsAdapter
    var itemList = mutableListOf<Item>()
    var isFABOpen = false
    lateinit var transitionDrawable: TransitionDrawable
    private val mHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_vendor)
        setUI()


        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(Firebase.Items.ITEMS.Key)
        val postListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val tag = "firebase_mva"
                Timber.tag(tag).d("Added\n $snapshot")
                val item = snapshot.getValue(Item::class.java)
                item?.id = snapshot.key.toString()

                Timber.tag(tag).d("item=${item?.categoryId}")

                itemList.add(item!!)
                itemAdapter.notifyDataSetChanged()

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val tag = "firebase_mva"
                Timber.tag(tag).d("Changed\n $snapshot")
                val item = snapshot.getValue(Item::class.java)
                item?.id = snapshot.key.toString()
                Timber.tag(tag).d("item=${item?.categoryId}")

                itemList.removeIf { it.categoryId == item?.id }
                itemList.add(item!!)
                itemAdapter.notifyDataSetChanged()
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



        myRef.addChildEventListener(postListener)

        main_fab_amv.setOnClickListener {
            val codeToRun = Runnable {
                if (isFABOpen) {
                    transitionDrawable.reverseTransition(200)
                    closeFABMenu()
                }
            }

            if (isFABOpen) {
                mHandler.removeCallbacksAndMessages(null)
                codeToRun.run()
            } else {
                showFABMenu()
                transitionDrawable.startTransition(0)
                mHandler.postDelayed(codeToRun, 2000)
            }
        }

        fab_add_item_amv.setOnClickListener {
            Toast.makeText(this, "add", Toast.LENGTH_LONG).show()
        }
        fab_logout_item_amv.setOnClickListener {
            Toast.makeText(this, "logout", Toast.LENGTH_LONG).show()
        }
    }

    private fun setUI() {
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
}