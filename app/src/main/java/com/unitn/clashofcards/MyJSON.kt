package com.unitn.clashofcards

import android.content.Context
import android.util.Log
import org.json.JSONArray
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException


class MyJSON {
   private companion object {
        var arrayShop : JSONArray=  JSONArray()
         var buyPos : Int = 0

    }

    fun setArray( array: JSONArray) {
         arrayShop = array
    }

    fun getArray(): JSONArray {
        return arrayShop
    }
    fun setBuyItem(integer: Int) {
        buyPos = integer
    }
    fun getBuyItem(): Int {
       return buyPos
    }

}