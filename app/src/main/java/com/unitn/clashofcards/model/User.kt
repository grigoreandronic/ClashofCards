package com.unitn.clashofcards.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val username: String, val name: String , val surname: String , val email: String, val password: String, val birthDate : String): Parcelable {
    constructor() : this("", "", "", "", "", "", "")
}