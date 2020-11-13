package com.example.shoppingapp

import android.content.Context
import android.content.SharedPreferences
import com.example.shoppingapp.util.Firebase
import timber.log.Timber.tag

class Cart {
    companion object {
        private const val cartTag = "cart id"
        var cartList = mutableListOf<CartItem>()
            private set
        var total = 0.0f
        var count = 0


        fun addToCart(item: CartItem) {
            val index=alreadyExist(item.id)
            if (item.quantity > 1 && index>=0) {
                cartList[index].quantity=item.quantity
            } else
                cartList.add(item)
        }

        fun alreadyExist(id: String): Int {
            cartList.forEachIndexed { index, it ->
                if (it.id == id) return index
            }
            return -1
        }
        private lateinit var cartId: String
        fun setCartId(context: Context, value: String) {
            tag("cartId").d(value)
            cartId = value
            val sharedPreferences: SharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreference_KEY), Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(cartTag, value)
            editor.apply()
        }
        fun getCartId(context: Context): String? {
            if (this::cartId.isInitialized) return cartId
            val sharedPref: SharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPreference_KEY), Context.MODE_PRIVATE)
            return sharedPref.getString(cartTag, null)

        }
        fun updateStock(context: Context, index: Int, remove: Boolean): Boolean {
                if(remove) {
                    if (cartList[index].stock.toInt() == 0) return false
                    cartList[index].stock = (cartList[index].stock.toInt() - 1).toString()
                    total+= cartList[index].price.toFloat()
                    count++
                }
                else {
                    cartList[index].stock = (cartList[index].stock.toInt() + 1).toString()
                    total-= cartList[index].price.toFloat()
                    count--
                }
            Firebase.updateStock(context, cartList[index])
            return true
        }
    }
}
