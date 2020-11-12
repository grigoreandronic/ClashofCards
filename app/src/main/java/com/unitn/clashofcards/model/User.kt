package com.unitn.clashofcards.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.parcel.Parcelize


data class User(
  var uid: String? = "",
  var username: String? = "",
  var name: String?= "",
  var surname: String?= "",
  var email: String? = "",
  var password: String?= "",
  var birthDate: String= "",
  var Decks: ArrayList<DocumentReference> ? = null,
  var wins: String?= "0",
  var defeats : String?= "0",
  var tier : String?= "Iron"
)