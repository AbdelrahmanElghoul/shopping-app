package com.example.shoppingapp_customer.fragments

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.shoppingapp.Item
import com.example.shoppingapp.R
import com.example.shoppingapp.util.Firebase
import com.example.shoppingapp.util.UpdateUI
import com.example.shoppingapp_customer.adapters.SearchItemAdapter
import com.example.shoppingapp_customer.util.SpacesItemDecoration
import com.google.firebase.database.*
import kotlinx.android.synthetic.customer.fragment_checkout_screen.*
import kotlinx.android.synthetic.customer.fragment_items_screen.*
import kotlinx.android.synthetic.customer.fragment_items_screen.img_back_isf
import timber.log.Timber
import timber.log.Timber.*

class ItemsScreen : Fragment() {

    private lateinit var searchItemAdapter: SearchItemAdapter
    private lateinit var categoryList: Pair<Array<String>, TypedArray>
    private var index:Int=0
    lateinit var valueListener:ValueEventListener
    lateinit var myRef:Query
    var cancelled=false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_items_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        //arguments!!.getInt(getString(R.string.COLOR_TAG))
         index=requireArguments().getInt(getString(R.string.SELECTED_CATEGORY_TAG))

        img_back_isf.setOnClickListener { activity?.onBackPressed() }
        myRef = FirebaseDatabase.getInstance().getReference(Firebase.Items.ITEMS.Key)
                .orderByChild(Firebase.Items.ITEMS_CATEGORY.Key)
                .equalTo("$index")

        valueListener=object : ValueEventListener {
            val tag="fb_is"
            override fun onDataChange(snapshot: DataSnapshot) {

                e("dataChange")
                val key = snapshot.key.toString()
//                    val name = snapshot.child(key).child(Firebase.Users.USER_PASSWORD.Key).value.toString()
                tag("$tag key=").d(key)
                if(snapshot.childrenCount==0L){
                    Toast.makeText(context,"no items to display",Toast.LENGTH_SHORT).show()
                    activity?.onBackPressed()
                }
                for(snap in snapshot.children.iterator()){
                    if(cancelled)return
                    val item=snap.getValue(Item::class.java)
                    item?.id=snap.key.toString()
                    searchItemAdapter.addItem(item as Item)
                    layout_progressbar_isf.visibility=View.GONE
                    searchItemAdapter.notifyDataSetChanged()
                }

            }
            override fun onCancelled(error: DatabaseError) {
                e("$error")
            }
        }
        myRef.addListenerForSingleValueEvent(valueListener)
    }

    private fun setUI() {


        categoryList= Pair(context?.resources?.getStringArray(R.array.category_name),
                context?.resources?.obtainTypedArray(R.array.category_icon))
        txt_item_name_isf.text=categoryList.first?.get(index)
        img_item_isf.setImageResource(categoryList.second?.getResourceId(index,R.drawable.error)!!)

        layout_progressbar_isf.visibility-View.VISIBLE
        searchItemAdapter= SearchItemAdapter(context as Context)
        searchItemAdapter = SearchItemAdapter(context as Context)
        rv_items_isf.setHasFixedSize(false)
        rv_items_isf.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rv_items_isf.addItemDecoration(SpacesItemDecoration(0))

        rv_items_isf.adapter = searchItemAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelled=true
        myRef.removeEventListener(valueListener)

    }

}