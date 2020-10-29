package com.example.shoppingapp.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shoppingapp.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import timber.log.Timber


abstract class Firebase {

    companion object {
        //User
        private fun addUserToDatabase(fragment: Fragment, userMap: HashMap<String,String>, type: String) {
            val database = FirebaseDatabase.getInstance()
            val key = database.reference.push().key.toString()
            if (userMap.containsKey(Firebase.Users.USER_ICON.Key))
                userMap[Firebase.Users.USER_ICON.Key]=
                        addImageToStorage(fragment.context as Context, key, userMap[Firebase.Users.USER_ICON.Key] as String, type)

            val myRef = database
                    .getReference(type)
                    .child(key).
                    setValue(userMap)
                    .addOnSuccessListener {
                        //Todo Continue
                    }
                    .addOnFailureListener{
                        Timber.e(it)
                        val ui = fragment as UpdateUI
                        ui.update(it.toString())
                        Toast.makeText(fragment.context, "log in failed.\n${it}", Toast.LENGTH_SHORT).show()
                    }

        }
        fun addImageToStorage(context: Context, iconName: String, uri: String, type: String): String {
            val mStorageRef = FirebaseStorage.getInstance().reference
            val riversRef: StorageReference = mStorageRef.child("$type/$iconName.jpg")
            riversRef.putFile(Uri.parse(uri))
                    .addOnFailureListener {
                        Timber.e(it)
                        val ui = context as UpdateUI
                        ui.update(it.toString())
                        Toast.makeText(context, "error occurred\n$it.message", Toast.LENGTH_SHORT).show()
                    }.addOnCompleteListener {
                        if (!it.isSuccessful) {
                            Timber.e(it.exception)
                            val ui = context as UpdateUI
                            ui.update(it.toString())
                            Toast.makeText(context, "error2 occurred\n$it.message", Toast.LENGTH_SHORT).show()
                        }

                    }
            return riversRef.downloadUrl.result.toString()
        }
        fun auth(fragment: Fragment, user: HashMap<String,String>, password: String, type: String, intent: Intent) {
            val mAuth = FirebaseAuth.getInstance()
            mAuth.createUserWithEmailAndPassword(user[Firebase.Users.USER_EMAIL.Key] as String, password)
                    .addOnSuccessListener {
                        addUserToDatabase(fragment, user, type)
                        fragment.startActivity(intent)
                    }
                    .addOnFailureListener {
                        Timber.e(it)
                        val ui = fragment as UpdateUI
                        ui.update(it.toString())
                        Toast.makeText(fragment.context, "log in failed.\n${it}", Toast.LENGTH_SHORT).show()
                    }
        }
        fun logout(activity: Activity) {
            FirebaseAuth.getInstance().signOut()
            activity.finish()

        }
        fun logInWithEmailPassword(fragment: Fragment, email: String, password: String, intent: Intent) {
            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        fragment.startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(fragment.context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        val ui = fragment as UpdateUI
                        ui.update(it.toString())
                    }
        }
        fun logInWithGoogle(fragment: Fragment,token:String,type:String,intent: Intent) {
            val auth = FirebaseAuth.getInstance()
            val credential = GoogleAuthProvider.getCredential(token, null)
            auth.signInWithCredential(credential)
                    .addOnSuccessListener {
                        // Sign in success, update UI with the signed-in user's information
                        val user = HashMap<String, String>()
                        user[Firebase.Users.USER_NAME.Key] = auth.currentUser?.displayName
                                ?: auth.currentUser?.email as String
                        user[Firebase.Users.USER_EMAIL.Key] = auth.currentUser?.email as String
                        if (auth.currentUser?.photoUrl != null)
                            user[Firebase.Users.USER_ICON.Key] = auth.currentUser?.photoUrl.toString()
                        if (auth.currentUser?.phoneNumber != null)
                            user[Firebase.Users.USER_PHONE.Key] = auth.currentUser?.phoneNumber as String
                        addUserToDatabase(fragment, user, type)
                        fragment.startActivity(intent)
                    }
                    .addOnFailureListener {
                        Toast.makeText(fragment.context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        val ui = fragment as UpdateUI
                        ui.update(it.toString())
                    }
        }

        fun logInWithFacebook(context: Context, user: User, password: String, type: String, intent: Intent) {

        }
        fun updateUserProfile() {
//            val user = Firebase.auth.currentUser
//
//            val profileUpdates = userProfileChangeRequest {
//                displayName = "Jane Q. User"
//                photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
//            }
//
//            user!!.updateProfile(profileUpdates)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Log.d(TAG, "User profile updated.")
//                        }
//                    }
        }
        fun getItems(key: String, value: String): List<Items>? {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference(Firebase.Items.ITEMS.Key).child(key)

//            myRef.addValueEventListener(postListener)

            return null
        }
        fun verifyMail() {
//            val user = Firebase.auth.currentUser
//
//            user!!.sendEmailVerification()
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Log.d(TAG, "Email sent.")
//                        }
//                    }
        }
        fun updatePassword() {
//            val user = Firebase.auth.currentUser
//            val newPassword = "SOME-SECURE-PASSWORD"
//
//            user!!.updatePassword(newPassword)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Log.d(TAG, "User password updated.")
//                        }
//                    }

        }
        fun passwordResetEmail() {
//            val emailAddress = "user@example.com"
//
//            Firebase.auth.sendPasswordResetEmail(emailAddress)
//                    .addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            Log.d(TAG, "Email sent.")
//                        }
//                    }

        }
        fun reAuthUser() {
//            val user = Firebase.auth.currentUser!!

//// Get auth credentials from the user for re-authentication. The example below shows
//// email and password credentials but there are multiple possible providers,
//// such as GoogleAuthProvider or FacebookAuthProvider.
//            val credential = EmailAuthProvider
//                    .getCredential("user@example.com", "password1234")
//
//// Prompt the user to re-provide their sign-in credentials
//            user.reauthenticate(credential)
//                    .addOnCompleteListener { Log.d(TAG, "User re-authenticated.") }
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
    }
    enum class Users(val Key: String){
        CUSTOMER("customer"),
        VENDOR("vendor"),
        USER_NAME("name"),
        USER_ICON("icon"),
        USER_EMAIL("email"),
        USER_PHONE("phone"),
        USER_CART_ID("cartId"),
    }
}