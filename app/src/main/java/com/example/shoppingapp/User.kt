package com.example.shoppingapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Parcel
import android.os.Parcelable
import com.example.shoppingapp.util.Firebase
import timber.log.Timber

class User(){


    companion object{
        fun toMap():HashMap<String,String>{
            val map=HashMap<String,String>()

            map[Firebase.Users.USER_NAME.Key]=name
            map[Firebase.Users.USER_EMAIL.Key]=email
            map[Firebase.Users.USER_PHONE.Key]=phone as String
            map[Firebase.Users.USER_CART_ID.Key]=cartId

            if (icon != null)
                map[Firebase.Users.USER_ICON.Key]= icon!!

            return map
        }
        fun addToCart(cart:Cart){
            cartList.add(cart)
        }
        lateinit var id: String
        fun setId(context: Context, value: String){
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreference_KEY), Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor =  sharedPreferences.edit()
            editor.putString(context.getString(R.string.sharedPreferenceUserID_KEY), value)
            editor.apply()
        }
        fun getId(context: Context):String?{
            if(this::id.isInitialized) return id
            val sharedPref: SharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreference_KEY), Context.MODE_PRIVATE)
            return sharedPref.getString(context.getString(R.string.sharedPreferenceUserID_KEY),null)

        }
        lateinit var name: String
        lateinit var email: String
        var icon: String?=null
        var phone: String?=null
            get(){
                return field ?: ""
            }
        var cartList=mutableListOf<Cart>()
            private set
        lateinit var cartId:String

    }
   class Cart {
        lateinit var itemId:String
        var quantity:Int=0
   }


}
