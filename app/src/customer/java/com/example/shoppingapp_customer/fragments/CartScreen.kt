package com.example.shoppingapp_customer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingapp.Cart
import com.example.shoppingapp.R
import com.example.shoppingapp.util.UpdateUI
import com.example.shoppingapp_customer.adapters.CartAdapter
import kotlinx.android.synthetic.customer.fragment_cart_screen.*

class CartScreen : Fragment(),UpdateUI {

    private lateinit var cartAdapter: CartAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        txt_proceed_checkout_csf.setOnClickListener {
            it.findNavController().navigate(R.id.action_cartScreen_to_locationScreen)
        }
        img_back_csf.setOnClickListener {
            requireActivity().onBackPressed()
        }
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
        update(null)
    }

    override fun update(text: String?) {
        cartAdapter.notifyDataSetChanged()
        txt_item_count_csf.text=Cart.count.toString()
        txt_total_price_csf.text="${Cart.total} $"
    }

}