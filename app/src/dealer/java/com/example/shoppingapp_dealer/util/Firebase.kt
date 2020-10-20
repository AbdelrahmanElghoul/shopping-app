package com.example.shoppingapp_dealer.util

import com.example.shoppingapp.Category
import com.example.shoppingapp.Item
import com.example.shoppingapp.util.FirebaseKey
import com.google.firebase.database.FirebaseDatabase

class Firebase {
    companion object {
        fun addItemToFirebase(item: Item) {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference(FirebaseKey.ITEMS.Key).child(database.reference.push().key.toString())

            myRef.child(FirebaseKey.ITEM_NAME.Key).setValue(item.name)
            myRef.child(FirebaseKey.ITEM_PRICE.Key).setValue(item.price)
            myRef.child(FirebaseKey.ITEM_STOCK.Key).setValue(item.stock)

            myRef.child(FirebaseKey.ITEMS_CATEGORY.Key).setValue(item.categoryId)

            if (item.description != null) myRef.child(FirebaseKey.ITEM_DESCRIPTION.Key).setValue(item.description)
            if (item.manufactureDetails != null) myRef.child(FirebaseKey.ITEM_MANUFACTURE.Key).setValue(item.manufactureDetails)
            if (item.iconURL != null) myRef.child(FirebaseKey.ITEM_IMG_URL.Key).setValue(item.iconURL)

        }

        fun addCategoryToFirebase(category: Category): String {
//        val database = FirebaseDatabase.getInstance().reference.child(getString(R.string.FIREBASE_CATEGORY))
            val database = FirebaseDatabase.getInstance()
            val key = database.reference.push().key.toString()
            val myRef = database.getReference(FirebaseKey.CATEGORY.Key).child(key)

            myRef.child(FirebaseKey.CATEGORY_NAME.Key).setValue(category.name)
            if(category.icon != null) myRef.child(FirebaseKey.CATEGORY_IMG_URL.Key).setValue(category.icon)

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