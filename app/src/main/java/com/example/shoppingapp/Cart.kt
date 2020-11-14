package com.example.shoppingapp

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.shoppingapp.util.CartListener
import com.example.shoppingapp.util.OpenFragment
import timber.log.Timber.tag

class Cart {
    companion object {
        private const val tag="Cart"
        private const val cartTag = "cart id"
        var cartList = mutableListOf<CartItem>()
            private set
        var total = 0.0f
            get() {
            var counter=0f
            cartList.forEach {
                counter += (it.quantity * it.price.toFloat())
            }
            return counter
        }
        var count = 0
            get() {
                var counter=0
                cartList.forEach {
                    counter += (it.quantity)
                }
                return counter
            }

        private var listenerContext:Fragment?=null

        fun notifyUpdate(){
            tag("$tag notifier1").d("check")
            if(listenerContext!=null){
                val notifier=(listenerContext) as CartListener
                notifier.notifyChange()
            }
            tag("$tag notifier2").d("check")
        }

        fun listen(fragment: Fragment){
            listenerContext=fragment
        }
        fun removeListener(){
            listenerContext=null
        }


        fun addToCart(item: CartItem) {
            cartList.add(item)
            notifyUpdate()
        }
        fun updateCart(item:CartItem){
            val index=getIndex(item.id)
            tag("$tag updateCart").d("$index")
            if (item.quantity > 1 && index>=0) {
                cartList[index].quantity=item.quantity
                notifyUpdate()
            }
        }
        fun removeFromCart(id: String) {
            val index=getIndex(id)
            cartList.removeAt(index)
            notifyUpdate()
        }

        fun getIndex(id: String): Int {
            cartList.forEachIndexed { index, it ->
                if (it.id == id) return index
            }
            return -1
        }

        private lateinit var cartId: String
        fun setCartId(context: Context, value: String) {
            tag("$tag cartId").d(value)
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

//        fun updateStock(context: Context, index: Int, remove: Boolean): Boolean {
//                if(remove) {
//                    if (cartList[index].stock.toInt() == 0) return false
//                    cartList[index].stock = (cartList[index].stock.toInt() - 1).toString()
//                    total+= cartList[index].price.toFloat()
//                    count++
//                }
//                else {
//                    cartList[index].stock = (cartList[index].stock.toInt() + 1).toString()
//                    total-= cartList[index].price.toFloat()
//                    count--
//                }
//            Firebase.updateStock(context, cartList[index])
//            return true
//        }
    }
}
