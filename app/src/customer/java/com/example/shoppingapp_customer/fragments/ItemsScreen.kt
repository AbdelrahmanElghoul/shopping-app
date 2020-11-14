package com.example.shoppingapp_customer.fragments

import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.shoppingapp.Item
import com.example.shoppingapp.R
import com.example.shoppingapp.util.Firebase
import com.example.shoppingapp_customer.adapters.SearchItemAdapter
import com.example.shoppingapp_customer.util.SpacesItemDecoration
import com.google.firebase.database.*
import kotlinx.android.synthetic.customer.fragment_items_screen.*
import kotlinx.android.synthetic.customer.fragment_items_screen.img_back_isf
import timber.log.Timber.*

class ItemsScreen : Fragment() {

    private lateinit var searchItemAdapter: SearchItemAdapter
    private lateinit var categoryList: Pair<Array<String>, TypedArray>
    private var index:Int=0
    lateinit var listener:ChildEventListener
    lateinit var myRef:Query
    var cancelled=false
    var itemList= mutableListOf<Item>()
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

        listener=object :ChildEventListener{
            val tag="fb_is_XXX"
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                tag("$tag added").d("${snapshot.exists()}")
                val item=snapshot.getValue(Item::class.java)
                item?.id=snapshot.key.toString()
                layout_progressbar_isf.visibility=View.GONE
                itemList.forEachIndexed{index,mItem->
                    if(mItem.id==item?.id) {
                       return
                    }
                }
                itemList.add(item!!)
                searchItemAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                tag("$tag changed").d("$snapshot")
                val item=snapshot.getValue(Item::class.java)
                item?.id=snapshot.key.toString()
                itemList.forEachIndexed{index,mItem->
                    if(mItem.id==item?.id) {
                        itemList[index] = item
                        if(layout_progressbar_isf!=null)
                        layout_progressbar_isf.visibility = View.GONE
                        searchItemAdapter.notifyDataSetChanged()
                    }

                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                tag("$tag removed").d("$snapshot")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                tag(tag).d("moved")
            }

            override fun onCancelled(error: DatabaseError) {
                tag(tag).d("cancel")
            }
        }

        val tmp=object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.childrenCount==0L){
                    Toast.makeText(context,"no items to display",Toast.LENGTH_SHORT).show()
                    activity?.onBackPressed()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        myRef.addListenerForSingleValueEvent(tmp)
        myRef.addChildEventListener(listener)

    }

    private fun setUI() {
        categoryList= Pair(context?.resources?.getStringArray(R.array.category_name),
                context?.resources?.obtainTypedArray(R.array.category_icon))
        txt_item_name_isf.text=categoryList.first?.get(index)
        img_item_isf.setImageResource(categoryList.second?.getResourceId(index,R.drawable.error)!!)


        layout_progressbar_isf.visibility=View.VISIBLE
        searchItemAdapter= SearchItemAdapter(requireContext(),itemList)

        rv_items_isf.setHasFixedSize(false)
        rv_items_isf.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rv_items_isf.addItemDecoration(SpacesItemDecoration(0))

        rv_items_isf.adapter = searchItemAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelled=true
        myRef.removeEventListener(listener)
    }

}