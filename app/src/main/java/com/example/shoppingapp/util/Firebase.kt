package com.example.shoppingapp.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shoppingapp.R
import com.example.shoppingapp.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import timber.log.Timber
import timber.log.Timber.tag
import kotlin.collections.HashMap


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
                    User.icon =task.result.toString()
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
            activity.getSharedPreferences(activity.getString(R.string.sharedPreference_KEY), Context.MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply()
            activity.finish()
        }
        fun register(fragment: Fragment, userMap: HashMap<String, String>, uri: Uri?, type: String, intent: Intent){
            val tag="fb register"
            this.type=type
            this.uri=uri
            this.intent=intent
            this.fragment=fragment

            tag("$tag email").d(userMap[Firebase.Users.USER_EMAIL.Key])
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference(type)
                    .orderByChild(Firebase.Users.USER_EMAIL.Key)
                    .equalTo(userMap[Firebase.Users.USER_EMAIL.Key])

            val ref=database.getReference(type)
            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //check if email exist
                    val x = snapshot.children.firstOrNull()?.value
                    if (x == null) {
                        val key = ref.push().key.toString()
                        ref.child(key)
                                .setValue(userMap)
                                .addOnSuccessListener {
                                    User.setId(fragment.requireContext(),key)
                                    if(type==Firebase.Users.CUSTOMER.Key){
                                        User.cartId = ref.push().key.toString()
                                        val cart = mapOf(User.cartId to Firebase.Cart.NOT_DELIVERED.Key)
                                        ref.child(key).child(Firebase.Users.USER_CART_ID.Key).setValue(cart)
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
        fun login(fragment: Fragment, email:String, password: String, type: String, intent: Intent){
            val database = FirebaseDatabase.getInstance()

            val myRef = database.getReference(type)
                    .orderByChild(Firebase.Users.USER_EMAIL.Key)
                    .equalTo(email)

            myRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val snap = snapshot.children.firstOrNull()
                    tag("fb_snap").d("${snap}")
                    tag("fb_key").d("${snap?.key}")
                    tag("fb_value").d("${snap?.value}")
                    if (snap != null) {
                        val map = snap.value as HashMap<*, *>
                        if (map[Firebase.Users.USER_PASSWORD.Key] == password) {
                            User.setId(context = fragment.requireContext(), value = snap.key!!)
                            User.cartId=snap.child(Firebase.Users.USER_CART_ID.Key).key.toString()


                            fragment.startActivity(intent)
                            fragment.activity?.finish()
                        } else {
                            val errorMsg = "email/password is incorrect"
                            Toast.makeText(fragment.context, errorMsg,
                                    Toast.LENGTH_SHORT).show()
                            val ui = fragment as UpdateUI
                            ui.update(errorMsg)
                        }
                    } else {
                        val errorMsg = "email not found"
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

        fun loadUser(snapshot: DataSnapshot){

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
    enum class Cart(val Key: String){
        DELIVERED("delivered"),
        NOT_DELIVERED("not delivered"),

    }
}