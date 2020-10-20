package com.unitn.clashofcards.model

import com.google.firebase.firestore.DocumentReference

data class GameRoom(
    var uid1: String? = "",
    var uid2: String? = "",
    var Deck: String? = "",
    var status: String? = "",
    var decksize:String? = "",
    var uid1decksize:String? = "",
    var uid2decksize:String? = "",
    var turn: String? = "",
    var uid1value1: String? = "",
    var uid1value2: String? = "",
    var uid1value3: String? = "",
    var uid1value4: String? = "",
    var uid1value5: String? = "",
    var uid2value1: String? = "",
    var uid2value2: String? = "",
    var uid2value3: String? = "",
    var uid2value4: String? = "",
    var uid2value5: String? = "",
    var valueselected: String? = "",
    var operation: String? = "",
    var uid1move: String? = "",
    var uid2move: String? = "",
    var uid1nturn: String? = "",
    var uid2nturn: String? = "",
    var uid1nset: String? = "",
    var uid2nset: String? = ""
)