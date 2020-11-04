package com.example.shoppingapp

import android.os.Parcel
import android.os.Parcelable

class Item(var id: String, var name: String, var categoryId: String, var price: String, var stock: String) : Parcelable {

    var vendorId: String? = null
    var icon: String? = null
    var description: String? = null
    var manufacture: String? = null

    constructor(source: Parcel) : this(
            source.readString() as String,
            source.readString() as String,
            source.readString() as String,
            source.readString() as String,
            source.readString() as String
    )


    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeString(categoryId)
        writeString(price)
        writeString(stock)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Item> = object : Parcelable.Creator<Item> {
            override fun createFromParcel(source: Parcel): Item = Item(source)
            override fun newArray(size: Int): Array<Item?> = arrayOfNulls(size)
        }
    }
}