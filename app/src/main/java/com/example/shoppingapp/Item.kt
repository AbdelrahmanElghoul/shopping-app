package com.example.shoppingapp

import android.os.Parcel
import android.os.Parcelable

class Item : Parcelable {

    var id: String = ""
    var categoryId: String = ""
    var name: String = "name"
    var iconURL: String? = null
    var description: String? = null
    var price :String="0.0"
    var manufactureDetails: String? = null
    var stock :String="0"

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