package com.example.shoppingapp.util

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.widget.Toast
import androidx.core.content.contentValuesOf
import androidx.fragment.app.Fragment
import com.example.shoppingapp.R
import com.example.shoppingapp.User
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import timber.log.Timber


abstract class Firebase {

    companion object {

        private lateinit var type: String
        private lateinit var intent: Intent
        private var uri: Uri? = null
        private lateinit var fragment: Fragment

        private fun addUserToDatabase(userMap: HashMap<String, String>) {
            val database = FirebaseDatabase.getInstance()
            database
                    .getReference(type)
                    .child(FirebaseAuth.getInstance().uid!!).setValue(userMap)
                    .addOnSuccessListener {
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
        fun addImageToStorage(iconKey: String) {
            val mStorageRef = FirebaseStorage.getInstance().reference
            val ref: StorageReference = mStorageRef.child("$type/${FirebaseAuth.getInstance().uid!!}.jpg")

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
                    val ui = this.fragment.activity as UpdateUI
                    ui.update(it.toString())
                    Toast.makeText(this.fragment.context, "error occurred\n$it.message", Toast.LENGTH_SHORT).show()
                }
            }

        }
        private fun addImageUrlToDatabase(iconKey: String, url: String) {
            FirebaseDatabase.getInstance()
                    .getReference(type)
                    .child(FirebaseAuth.getInstance().uid!!)
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

        fun auth(fragment: Fragment, user: HashMap<String, String>, password: String,
                 uri: Uri?, type: String, intent: Intent) {
            val mAuth = FirebaseAuth.getInstance()
            mAuth.createUserWithEmailAndPassword(user[Firebase.Users.USER_EMAIL.Key] as String, password)
                    .addOnSuccessListener {
                        this.type = type
                        this.intent = intent
                        this.uri = uri
                        this.fragment = fragment
                        addUserToDatabase(userMap = user)
                    }
                    .addOnFailureListener {
                        Timber.e(it)
                        val ui = fragment as UpdateUI
                        ui.update(it.toString())
                        Toast.makeText(fragment.context, "log in failed.\n${it}", Toast.LENGTH_SHORT).show()
                    }
        }
        fun logInWithEmailPassword(fragment: Fragment, email: String, password: String, intent: Intent) {
            val auth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        this.intent = intent
                        fragment.startActivity(intent)
                        fragment.activity?.finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(fragment.context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        val ui = fragment as UpdateUI
                        ui.update(it.toString())
                    }
        }
        fun logInWithGoogle(fragment: Fragment, token: String, type: String, intent: Intent) {
            val auth = FirebaseAuth.getInstance()
            val credential = GoogleAuthProvider.getCredential(token, null)
            auth.signInWithCredential(credential)
                    .addOnSuccessListener {
                        this.type = type
                        this.intent = intent
                        this.fragment = fragment
                        // Sign in success, update UI with the signed-in user's information
                        val user = HashMap<String, String>()
                        user[Firebase.Users.USER_NAME.Key] = auth.currentUser?.displayName
                                ?: auth.currentUser?.email as String
                        user[Firebase.Users.USER_EMAIL.Key] = auth.currentUser?.email as String
                        if (auth.currentUser?.phoneNumber != null)
                            user[Firebase.Users.USER_PHONE.Key] = auth.currentUser?.phoneNumber as String
                        Timber.tag("googleImg").d("${auth.currentUser?.photoUrl}")
                        addUserToDatabase(userMap = user)

                        if (auth.currentUser?.photoUrl != null)
                            addImageUrlToDatabase(iconKey = Firebase.Users.USER_ICON.Key,
                                    url = auth.currentUser?.photoUrl.toString())
                        else {
                            fragment.startActivity(intent)
                            fragment.activity?.finish()
                        }

                    }
                    .addOnFailureListener {
                        Toast.makeText(fragment.context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        val ui = fragment as UpdateUI
                        ui.update(it.toString())
                    }
        }

        fun logout(activity: Activity) {
            FirebaseAuth.getInstance().signOut()
            if (GoogleSignIn.getLastSignedInAccount(activity) != null)
                googleLogout(activity)
            activity.finish()
        }
        private fun googleLogout(context: Context) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.OAuthClient))
                    .requestEmail()
                    .build()

            val mGoogleSignInClient = GoogleSignIn.getClient(context, gso)
            mGoogleSignInClient.signOut()
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