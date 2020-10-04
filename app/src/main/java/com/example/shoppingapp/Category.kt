package com.example.shoppingapp

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Category : Parcelable {

    var id: String = ""
    var name: String? = null
    var icon = 0
    var itemList: List<Item> = ArrayList()

    constructor(source: Parcel) : this()
    constructor()

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {}

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Category> = object : Parcelable.Creator<Category> {
            override fun createFromParcel(source: Parcel): Category = Category(source)
            override fun newArray(size: Int): Array<Category?> = arrayOfNulls(size)
        }
    }
}