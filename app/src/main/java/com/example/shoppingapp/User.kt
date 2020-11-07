package com.example.shoppingapp

import android.os.Parcel
import android.os.Parcelable

class User() : Parcelable {

    lateinit var id: String
    lateinit var name: String
    lateinit var type:String
    lateinit var email: String
    var icon: String?=null
    var phone: String?=null
    var cartId:String?=null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString() as String
        name = parcel.readString() as String
        type = parcel.readString() as String
        email = parcel.readString() as String
        icon = parcel.readString()
        phone = parcel.readString()
        cartId = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(type)
        parcel.writeString(email)
        parcel.writeString(icon)
        parcel.writeString(phone)
        parcel.writeString(cartId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }


}
