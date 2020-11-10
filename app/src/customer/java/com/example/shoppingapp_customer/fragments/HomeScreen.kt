package com.example.shoppingapp_customer.fragments

import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp_customer.adapters.CategoryAdapter
import com.example.shoppingapp_customer.adapters.PreviousOrderAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.shoppingapp.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingapp.util.Firebase
import kotlinx.android.synthetic.customer.fragment_home_screen.*

class HomeScreen : Fragment() {

    private lateinit var categoryAdapter:CategoryAdapter
    private lateinit var previousOrderAdapter:PreviousOrderAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        img_logout_hsf.setOnClickListener{
            Firebase.logout(activity = requireActivity())
        }
        setUI()
        txt_see_all_previous_order_hsf.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeScreen_to_cartScreen2)
        }
    }

    private fun setUI() {
        categoryAdapter = CategoryAdapter(requireContext())
        rv_category_hsf!!.setHasFixedSize(true)
        rv_category_hsf.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_category_hsf.adapter = categoryAdapter

        previousOrderAdapter = PreviousOrderAdapter(context)
        rv_previous_order_hsf.setHasFixedSize(true)
        rv_previous_order_hsf.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_previous_order_hsf.adapter = previousOrderAdapter
    }

}