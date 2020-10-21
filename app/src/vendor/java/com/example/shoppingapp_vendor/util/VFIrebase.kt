package com.example.shoppingapp_vendor.util


import com.example.shoppingapp.Item
import com.example.shoppingapp.util.Firebase

import com.google.firebase.database.FirebaseDatabase

class VFirebase {
    companion object {
        fun addItemToFirebase(item: Item) {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference(Firebase.Items.ITEMS.Key).child(database.reference.push().key.toString())

            myRef.child(Firebase.Items.ITEM_NAME.Key).setValue(item.name)
            myRef.child(Firebase.Items.ITEM_PRICE.Key).setValue(item.price)
            myRef.child(Firebase.Items.ITEM_STOCK.Key).setValue(item.stock)

            myRef.child(Firebase.Items.ITEMS_CATEGORY.Key).setValue(item.categoryId)

            if (item.description != null) myRef.child(Firebase.Items.ITEM_DESCRIPTION.Key).setValue(item.description)
            if (item.manufactureDetails != null) myRef.child(Firebase.Items.ITEM_MANUFACTURE.Key).setValue(item.manufactureDetails)
            if (item.iconURL != null) myRef.child(Firebase.Items.ITEM_IMG_URL.Key).setValue(item.iconURL)

        }
    }
}