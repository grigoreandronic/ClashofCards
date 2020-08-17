package com.unitn.clashofcards.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserSocial(
    var uid: String,
    var email: String

) : Parcelable {


}
