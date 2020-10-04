package com.example.shoppingapp

import android.content.Context
import android.os.Parcel
import android.os.Parcelable

class Item : Parcelable {

    val id: String = ""
    val categoryId: String = ""
    val name: String = "name"
    val iconURL: String? = null
    val description: String = "Description"
    val price = 0.0
    val manufactureDetails: String? = null
    val stock = 0

    constructor(source: Parcel) : this()
    constructor()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Item> = object : Parcelable.Creator<Item> {
            override fun createFromParcel(source: Parcel): Item = Item(source)
            override fun newArray(size: Int): Array<Item?> = arrayOfNulls(size)
        }
    }
}