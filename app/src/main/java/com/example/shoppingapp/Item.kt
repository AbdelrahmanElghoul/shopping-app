package com.example.shoppingapp

import android.os.Parcel
import android.os.Parcelable

class Item : Parcelable {

    lateinit var id: String
    lateinit var categoryId: String
    lateinit var name: String
    var iconURL: String? = null
    var description: String? = null
    lateinit var price: String
    var manufactureDetails: String? = null
    lateinit var stock: String
    lateinit var vendorId: String

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