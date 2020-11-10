package com.example.shoppingapp

import android.os.Parcel
import android.os.Parcelable

class Item() : Parcelable {

    lateinit var id:String
    lateinit var name: String
    var icon: String? = null
    var description: String? = null
    lateinit var price: String
    var manufacture: String? = null
    get(){
        return if(field=="") "no details added by vendor" else field
    }
    lateinit var stock: String
    lateinit var categoryId:String
    lateinit var vendorId: String

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() as String
        name = parcel.readString() as String
        icon = parcel.readString()
        description = parcel.readString()
        price = parcel.readString() as String
        manufacture = parcel.readString()
        stock = parcel.readString() as String
        categoryId = parcel.readString() as String
        vendorId = parcel.readString() as String
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(icon)
        parcel.writeString(description)
        parcel.writeString(price)
        parcel.writeString(manufacture)
        parcel.writeString(stock)
        parcel.writeString(categoryId)
        parcel.writeString(vendorId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }

}