package com.unitn.clashofcards.model

data class Card (
  var id :String ? = "",
  var icons:String ? = "",
  var alpha:String ? = "",
  var attributename1:String ? = "",
  var attributevalue1:String ? = "",
  var attributename2:String ? = "",
  var attributevalue2:String ? = "",
  var attributename3:String ? = "",
  var attributevalue3:String ? = "",
  var attributename4:String ? = "",
  var attributevalue4:String ? = "",
  var attributename5:String ? = "",
  var attributevalue5:String ? = "",
  var special:Boolean? = false
)