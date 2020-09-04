package com.unitn.clashofcards.model


data class Deck (
  var id:String ? = "",
  var icons:String ? = "",
  var alpha:String ? = "",
  var description: String ? = "",
  var price: String ?= "",
  var premium:Boolean? = false
)