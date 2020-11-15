package com.example.shoppingapp_customer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingapp.Cart
import com.example.shoppingapp.CartItem
import com.example.shoppingapp.R
import com.example.shoppingapp.util.CartListener
import com.example.shoppingapp.util.Firebase
import com.example.shoppingapp.util.UpdateUI
import com.example.shoppingapp_customer.adapters.CartAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.customer.activity_navigation.*
import kotlinx.android.synthetic.customer.fragment_cart_screen.*
import timber.log.Timber.e
import timber.log.Timber.tag

class CartScreen : Fragment(),UpdateUI,CartListener {

    lateinit var ref:DatabaseReference
    private lateinit var cartAdapter: CartAdapter
    private lateinit var childEventListener: ChildEventListener
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        e("onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        setUI()
        txt_proceed_checkout_csf.setOnClickListener {
            if(Cart.count==0) return@setOnClickListener
            it.findNavController().navigate(R.id.action_cartScreen_to_locationScreen)
        }
        img_back_csf.setOnClickListener {
            requireActivity().onBackPressed()
        }

        ref=FirebaseDatabase.getInstance()
                .getReference(Firebase.Items.ITEMS.Key)
        Cart.listen(this)
         childEventListener=object :ChildEventListener{
             val tag="fb_cart"
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                tag("$tag add").d("${snapshot.value}")
                val cart=snapshot.getValue(CartItem::class.java)
                cart?.id=snapshot.key!!
                val index=Cart.getIndex(cart?.id!!)
                if(index>=0){
                    Cart.updateCart(cart)
                    update("onChange")
                    tag("$tag update").d("notified ${cart.stock}")
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                tag("$tag change").d("${snapshot.value}")
                val cart=snapshot.getValue(CartItem::class.java)
                cart?.id=snapshot.key!!

                val index=Cart.getIndex(cart?.id!!)
                if(index>=0){
                    Cart.updateCart(cart)
                    update("onChange")
                    tag("$tag update").d("notified ${cart.stock}")
                }

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                tag("$tag remove").d("${snapshot.value}")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                tag("$tag move").d("${snapshot.value}")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),error.message,Toast.LENGTH_LONG).show()
            }
        }
         ref.addChildEventListener(childEventListener)
    }

    private fun setUI() {

        cartAdapter = CartAdapter(this)

        rv_cart_csf.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_cart_csf.setHasFixedSize(false)
        rv_cart_csf.adapter = cartAdapter
        Cart.total=0f
        Cart.count=0
        Cart.cartList.forEach{
            Cart.total=(it.price.toFloat()*it.quantity)+Cart.total
            Cart.count=Cart.count+(it.quantity)
        }
        update("setUI")
    }

    override fun update(text: String?) {
        tag("update").d("$text")
        cartAdapter.notifyDataSetChanged()
        txt_item_count_csf.text=Cart.count.toString()
        txt_total_price_csf.text="${Cart.total} $"
    }

    override fun notifyChange() {
       update("notifyChange")
    }

    override fun onDestroyView() {
        e("onDestroy")

        Cart.removeListener()
        ref.removeEventListener(childEventListener)
        super.onDestroyView()
    }

}