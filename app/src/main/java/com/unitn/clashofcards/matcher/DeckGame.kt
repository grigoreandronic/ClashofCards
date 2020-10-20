package com.unitn.clashofcards.matcher

import com.unitn.clashofcards.model.Card
import org.json.JSONArray

class DeckGame {
    private companion object {
        var deck: ArrayList<Card> = ArrayList()
        var gameRoom:String =""
        var uid:String=""
        var uidopponent:String=""
        var size=0

    }




    fun getDeckSize():Int{
        return size
    }


    fun setGameRoom(gameroom:String){
        gameRoom=gameroom
    }

    fun getGameRoom():String{
    return gameRoom
    }


    fun setuid(uidn:String){
    uid=uidn

    }

    fun setuidopponet(uid2:String){
        uidopponent=uid2
    }

    fun getuid():String{
        return uid
    }

    fun getuidopponet():String{
        return uidopponent
    }

    fun setArray( array: ArrayList<Card>) {
        deck = array
        size = array.size
    }

    fun getArray(): ArrayList<Card> {
        return deck
    }

}