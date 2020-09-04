package com.unitn.clashofcards.model

import com.google.firebase.firestore.DocumentReference

data class GameRoom(
    var uid1: String? = "",
    var uid2: String? = "",
    var Deck: String? = "",
    var status: String? = "",
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
    var turnwinner: String? = "",
    var setwinner: String? = ""

    )