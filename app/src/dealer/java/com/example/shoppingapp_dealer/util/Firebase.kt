package com.example.shoppingapp_dealer.util

import com.example.shoppingapp.Category
import com.example.shoppingapp.Item
import com.example.shoppingapp.util.Firebase
import com.google.firebase.database.FirebaseDatabase

class Firebase {
    companion object {
        fun addItemToFirebase(item: Item) {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference(Firebase.ITEMS.Key).child(database.reference.push().key.toString())

            myRef.child(Firebase.ITEM_NAME.Key).setValue(item.name)
            myRef.child(Firebase.ITEM_PRICE.Key).setValue(item.price)
            myRef.child(Firebase.ITEM_STOCK.Key).setValue(item.stock)

            myRef.child(Firebase.CATEGORY.Key).setValue(item.categoryId)

            if (item.description != null) myRef.child(Firebase.ITEM_DESCRIPTION.Key).setValue(item.description)
            if (item.manufactureDetails != null) myRef.child(Firebase.ITEM_MANUFACTURE.Key).setValue(item.manufactureDetails)
            if (item.iconURL != null) myRef.child(Firebase.ITEM_IMG_URL.Key).setValue(item.iconURL)

        }

        fun addCategoryToFirebase(category: Category): String {
//        val database = FirebaseDatabase.getInstance().reference.child(getString(R.string.FIREBASE_CATEGORY))
            val database = FirebaseDatabase.getInstance()
            val key = database.reference.push().key.toString()
            val myRef = database.getReference(Firebase.CATEGORY.Key).child(key)

            myRef.child(Firebase.CATEGORY_NAME.Key).setValue(category.name)
            if(category.icon != null) myRef.child(Firebase.CATEGORY_IMG_URL.Key).setValue(category.icon)

            return key
        }

        fun addImageToStorage(uri: String):String{

            return ""
        }

        fun getCategories():List<Category>?{
            return null
        }
    }
}