package com.example.shoppingapp.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shoppingapp.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import timber.log.Timber
import timber.log.Timber.e
import timber.log.Timber.tag
import kotlin.collections.HashMap
import kotlin.system.exitProcess


abstract class Firebase {

    companion object {

        private lateinit var type: String
        private lateinit var intent: Intent
        private var uri: Uri? = null
        private lateinit var fragment: Fragment

        private fun addImageToStorage(iconKey: String) {
            val mStorageRef = FirebaseStorage.getInstance().reference
            val ref: StorageReference = mStorageRef.child("$type/${User.getId(fragment.requireContext())}.jpg")

            ref.putFile(this.uri!!).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    addImageUrlToDatabase(iconKey = iconKey, url = task.result.toString())
                } else {
                    val it = task.exception
                    Timber.e(it)
                    val ui = this.fragment as UpdateUI
                    ui.update(it.toString())
                    Toast.makeText(this.fragment.context, "error occurred\n$it.message", Toast.LENGTH_SHORT).show()
                }
            }
        }
        private fun addImageUrlToDatabase(iconKey: String, url: String) {
            FirebaseDatabase.getInstance()
                    .getReference(type)
                    .child(User.getId(fragment.requireContext())!!)
                    .child(iconKey)
                    .setValue(url).addOnFailureListener {
                        Timber.e(it)
                        val ui = fragment.activity as UpdateUI
                        ui.update(it.toString())
                        Toast.makeText(fragment.context, "error occurred\n$it.message", Toast.LENGTH_SHORT).show()
                    }.addOnSuccessListener {
                        fragment.activity?.startActivity(intent)
                        fragment.activity?.finish()
                    }
        }
        fun logout(activity: Activity) {
            e("logOut")
            val ret=activity.getSharedPreferences(activity.getString(R.string.sharedPreference_KEY), Context.MODE_PRIVATE)
                    .edit()
                    .remove(activity.getString(R.string.sharedPreferenceUserID_KEY))
                    .commit()
            e("logOut $ret")
            activity.finishAffinity()
            exitProcess(0)
        }


        fun register(fragment: Fragment, userMap: HashMap<String, String>, uri: Uri?, type: String, intent: Intent) {
            this.type = type
            this.uri = uri
            this.intent = intent
            this.fragment = fragment


            val key=(userMap[Firebase.Users.USER_EMAIL.Key] as String).replace('.','-')
            userMap.remove(Firebase.Users.USER_EMAIL.Key)
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference(type)
                    .child(key)

            val ref = database.getReference(type)

            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child(Firebase.Users.USER_PASSWORD.Key).value == null) {
                        ref.child(key)
                                .setValue(userMap)
                                .addOnSuccessListener {
                                    User.setId(fragment.requireContext(), key)
                                    if (type == Firebase.Users.CUSTOMER.Key) {
                                        val cartId=ref.push().key.toString()
                                        Cart.setCartId(fragment.requireContext(), cartId)
                                        val cart = mapOf(cartId to Firebase.Carts.NOT_DELIVERED.Key)
                                        ref.child(key).child(Firebase.Users.USER_CART_ID.Key).setValue(cart)//add cart Id to user
                                    }
                                    if (uri != null)
                                        addImageToStorage(iconKey = Firebase.Users.USER_ICON.Key)
                                    else {
                                        fragment.startActivity(intent)
                                        fragment.activity?.finish()
                                    }

                                }
                                .addOnFailureListener {
                                    Timber.e(it)
                                    val ui = fragment as UpdateUI
                                    ui.update(it.toString())
                                    Toast.makeText(fragment.context, "log in failed.\n${it}", Toast.LENGTH_SHORT).show()
                                }
                    }
                    else {
                        val errorMsg = "email already exist"
                        Toast.makeText(fragment.context, errorMsg,
                                Toast.LENGTH_SHORT).show()
                        val ui = fragment as UpdateUI
                        ui.update(errorMsg)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.tag("login value listener cancelled").d("${error}")
                }
            })

        }

        fun login(fragment: Fragment, formattedEmail:String, password: String, type: String, intent: Intent){
            val email=formattedEmail.replace('.','-')
            val database = FirebaseDatabase.getInstance()

           val myRef = database.getReference(type).child(email)

            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    when (snapshot.child(Firebase.Users.USER_PASSWORD.Key).value) {
                        null -> {
                            val errorMsg = "email not found"
                            Toast.makeText(fragment.context, errorMsg,
                                    Toast.LENGTH_SHORT).show()
                            val ui = fragment as UpdateUI
                            ui.update(errorMsg)
                        }
                        password -> {
                            User.setId(context = fragment.requireContext(), value = email)
                            Cart.setCartId(fragment.requireContext(),snapshot.child(Firebase.Users.USER_CART_ID.Key).key.toString())

                            fragment.startActivity(intent)
                            fragment.activity?.finish()
                        }
                        else -> {
                            val errorMsg = "email/password is incorrect"
                            Toast.makeText(fragment.context, errorMsg,
                                    Toast.LENGTH_SHORT).show()
                            val ui = fragment as UpdateUI
                            ui.update(errorMsg)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.tag("login value listener cancelled").d("${error}")
                }
            })

        }

        fun addToCart(context: Context,cart: CartItem) {
            val tag="fb_addToCart"
            tag("$tag cartId").d("${Cart.getCartId(context)}")
            FirebaseDatabase.getInstance().getReference(Firebase.Carts.CART.Key)
                    .child(Cart.getCartId(context) as String)
                    .child(cart.id)
                    .setValue(cart.quantity).addOnCompleteListener {
                        if (it.isSuccessful) {
                           Cart.addToCart(cart)

                        } else
                            Toast.makeText(context,
                                    "something went wrong adding item to cart\nplease try again later",
                                    Toast.LENGTH_LONG).show()
                    }
        }

        fun loadCart(context: Context,intent: Intent){
            val tag="fb_cart"
            val key=Cart.getCartId(context) as String
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference(Firebase.Carts.CART.Key).child(key)
            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    tag("$tag snapshot").d("$snapshot")
                    snapshot.children.forEach {
                        tag("$tag snap key").d("${it.key}")
                        val snap = database.getReference(Firebase.Items.ITEMS.Key).child(it.key.toString())

                        snap.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(itemSnapShot: DataSnapshot) {
                                val cart=itemSnapShot.getValue(CartItem::class.java)
                                cart?.id=it.key.toString()
                                cart?.quantity=it.value.toString().toInt()
                                Cart.addToCart(cart!!)
                                tag("$tag sub item").d("${itemSnapShot.child("name").value}")
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })
                        tag("$tag snap value").d("${it.value}")
                    }
                    context.startActivity(intent)
                    (context as Activity).finish()
                }

                override fun onCancelled(error: DatabaseError) {
                    tag("$key error").e("${error}")
                }
            })

        }
    }

    enum class Items(val Key: String){
        ITEMS("Items"),
        ITEMS_CATEGORY("categoryId"),
        ITEM_NAME("name"),
        ITEM_IMG_URL("icon"),
        ITEM_PRICE("price"),
        ITEM_STOCK("stock"),
        ITEM_DESCRIPTION("description"),
        ITEM_MANUFACTURE("manufacture"),
        ITEM_VENDOR_ID("vendorId"),
    }
    enum class Users(val Key: String){
        CUSTOMER("customer"),
        VENDOR("vendor"),
        USER_PASSWORD("password"),
        USER_NAME("name"),
        USER_ICON("icon"),
        USER_EMAIL("email"),
        USER_PHONE("phone"),
        USER_CART_ID("cartId"),
    }
    enum class Carts(val Key: String){
        DELIVERED("delivered"),
        CART("cart"),
        NOT_DELIVERED("not delivered"),

    }
}