package com.example.shoppingapp_customer.fragments

import com.example.shoppingapp_customer.adapters.CategoryAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shoppingapp.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingapp.Item
import com.example.shoppingapp.User
import com.example.shoppingapp.util.Firebase
import kotlinx.android.synthetic.customer.fragment_home_screen.*

class HomeScreen : Fragment() {

    private lateinit var categoryAdapter:CategoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fab_logout.setOnClickListener{
            Firebase.logout(activity = requireActivity())
        }
        setUI()
        img_cart_hsf.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeScreen_to_cartScreen)
        }
    }

    private fun setUI() {
        categoryAdapter = CategoryAdapter(requireContext())
        rv_category_hsf!!.setHasFixedSize(true)
        rv_category_hsf.layoutManager = GridLayoutManager(requireContext(), 2)
        rv_category_hsf.adapter = categoryAdapter
    }

}