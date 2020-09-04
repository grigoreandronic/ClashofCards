package com.unitn.clashofcards.matcher

import com.unitn.clashofcards.model.Card
import org.json.JSONArray

class DeckGame {
    private companion object {
        var deck: ArrayList<Card> = ArrayList()

    }

    fun setArray( array: ArrayList<Card>) {
        deck = array
    }

    fun getArray(): ArrayList<Card> {
        return deck
    }

}