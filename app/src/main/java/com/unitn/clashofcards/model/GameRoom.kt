package com.unitn.clashofcards.model

import com.google.firebase.firestore.DocumentReference

data class GameRoom(
    var uid1: String? = "",
    var uid2: String? = "",
    var Deck: String? = "",
    var status: String? = ""
)