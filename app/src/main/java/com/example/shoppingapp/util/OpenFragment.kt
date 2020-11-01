package com.example.shoppingapp.util

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.shoppingapp.R

interface OpenFragment {
    fun openFragment(context: FragmentActivity,fragment: Fragment,view:Int) {
       context.supportFragmentManager
                .beginTransaction()
                .add(view, fragment)
                .addToBackStack(null)
                .commit()
    }
}