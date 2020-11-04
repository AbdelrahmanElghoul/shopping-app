package com.example.shoppingapp

import android.os.Parcel
import android.os.Parcelable

class User(var id: String, var name: String, var type: String, var email: String) : Parcelable {
    var icon: String? = null

    var phone: String? = null

    var cartId: String? = null

    constructor():this(
            "x","x","x","x"
    )
    constructor(source: Parcel) : this(
            source.readString() as String,
            source.readString() as String,
            source.readString() as String,
            source.readString() as String
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeString(type)
        writeString(email)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}
