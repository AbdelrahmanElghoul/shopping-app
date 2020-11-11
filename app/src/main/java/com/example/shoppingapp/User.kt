package com.example.shoppingapp

import android.content.Context
import android.content.SharedPreferences
import timber.log.Timber.e

class User{

    companion object{

        private lateinit var id: String
        fun setId(context: Context, value: String){
            id=value
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreference_KEY), Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor =  sharedPreferences.edit()
            editor.putString(context.getString(R.string.sharedPreferenceUserID_KEY), value)
            editor.apply()
        }
        fun getId(context: Context):String?{
            if(this::id.isInitialized) return id
            val sharedPref: SharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreference_KEY), Context.MODE_PRIVATE)
            val value=sharedPref.getString(context.getString(R.string.sharedPreferenceUserID_KEY),null)
            e(value)
            return value
        }

    }

}

