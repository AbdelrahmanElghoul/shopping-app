package com.example.shoppingapp.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri
import com.example.shoppingapp.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import timber.log.Timber


abstract class Firebase {

    companion object {

        fun addUserToFirebase(context: Context, user: User, type: String) {
            val database = FirebaseDatabase.getInstance()
            val key = database.reference.push().key.toString()
            val myRef = database.getReference(type).child(key)

            myRef.child(Firebase.Users.USER_NAME.Key).setValue(user.name)
            if (user.icon != null) myRef.child(Firebase.Users.USER_ICON.Key).setValue(
                    addImageToStorage(context, key, user.icon as String, type)
            )
            myRef.child(Firebase.Users.USER_NAME.Key).setValue(user.name)
            myRef.child(Firebase.Users.USER_EMAIL.Key).setValue(user.email)
            myRef.child(Firebase.Users.USER_PHONE.Key).setValue(user.phone)
        }
        fun addImageToStorage(context: Context, iconName: String, uri: String, type: String): String {
            val mStorageRef = FirebaseStorage.getInstance().reference
            val riversRef: StorageReference = mStorageRef.child("$type/$iconName.jpg")
            riversRef.putFile(Uri.parse(uri))
                    .addOnFailureListener {
                        Timber.e(it)
                        val ui=context as UpdateUI
                        ui.update(it.toString())
                        Toast.makeText(context, "error occurred\n$it.message", Toast.LENGTH_SHORT).show()
                    }
            return riversRef.downloadUrl.toString()
        }
        fun auth(activity: Activity, user: User, password: String, type: String, intent: Intent) {
            val mAuth = FirebaseAuth.getInstance()
            mAuth.createUserWithEmailAndPassword(user.email, password)
                    .addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            addUserToFirebase(activity, user, type)
                            // Sign in success, update UI with the signed-in user's information
                            activity.startActivity(intent)
                        } else {
                            task.addOnFailureListener {
                                Timber.e(it)
                                Toast.makeText(activity, "log in failed.\n${it}", Toast.LENGTH_SHORT).show()

                            }
                        }

                    }

        }

        fun login(email:String,password:String){

        }
        fun getUserProfile():User{

            return User()
        }
        fun getItems(key:String,value:String):List<Items>?{
            return null
        }

        fun logout(){
            FirebaseAuth.getInstance().signOut()
        }
        fun validateEmail(){}
        fun validatePhone(){}
    }

    enum class Items(val Key: String){
        ITEMS("Items"),
        ITEMS_CATEGORY("category id"),
        ITEM_NAME("name"),
        ITEM_IMG_URL("icon"),
        ITEM_PRICE("price"),
        ITEM_STOCK("stock"),
        ITEM_DESCRIPTION("description"),
        ITEM_MANUFACTURE("manufacture"),
    }
    enum class Users(val Key: String){
        CUSTOMER("customer"),
        VENDOR("vendor"),
        USER_NAME("name"),
        USER_ICON("icon"),
        USER_EMAIL("email"),
        USER_PHONE("phone"),
        USER_CART_ID("cartID"),
    }
}