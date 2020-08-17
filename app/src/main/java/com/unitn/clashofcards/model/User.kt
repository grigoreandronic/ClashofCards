package com.unitn.clashofcards.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var uid: String,
    var username: String?,
    var name: String?,
    var surname: String?,
    var email: String,
    var password: String?,
    var birthDate: String?

) : Parcelable {


    }
