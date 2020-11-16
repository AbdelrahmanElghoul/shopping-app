package com.example.shoppingapp_customer.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shoppingapp.Cart
import com.example.shoppingapp.CartItem
import com.example.shoppingapp.R
import com.example.shoppingapp.util.Firebase

import com.google.firebase.database.*
import timber.log.Timber.tag

class NavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        val cartID=Cart.getCartId(this) as String
        tag("key").e(cartID)
        val tag="fb_Navigation"
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(Firebase.Carts.CART.Key)
                .child(cartID)

        myRef.addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                tag("$tag add").d("$snapshot")
                database.getReference(Firebase.Items.ITEMS.Key)
                        .child(snapshot.key!!)
                        .addListenerForSingleValueEvent(object:ValueEventListener{
                            override fun onDataChange(snap: DataSnapshot) {
                                tag("$tag snap").d("$snap")
                                val cart=snap.getValue(CartItem::class.java)
                                cart?.id=snap.key!!
                                cart?.quantity=snapshot.value.toString().toInt()
                                Cart.addToCart(cart!!)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(applicationContext,error.message,Toast.LENGTH_LONG).show()
                            }
                        })

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                tag("$tag change").d("$snapshot")
                database.getReference(Firebase.Items.ITEMS.Key)
                        .child(snapshot.key!!)
                        .addListenerForSingleValueEvent(object:ValueEventListener{
                            override fun onDataChange(snap: DataSnapshot) {
                                val cart=snap.getValue(CartItem::class.java)
                                cart?.id=snap.key!!
                                cart?.quantity=snapshot.value.toString().toInt()
                                Cart.updateCart(cart!!)
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(applicationContext,error.message,Toast.LENGTH_LONG).show()
                            }
                        })
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                tag("$tag  remove").d("$snapshot")
                Cart.removeFromCart(snapshot.key!!)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                tag("$tag  move").d("$snapshot")
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_LONG).show()
            }
        })

    }


}
