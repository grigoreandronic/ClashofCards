package com.unitn.clashofcards.matcher

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unitn.clashofcards.R
import com.unitn.clashofcards.model.Card
import com.unitn.clashofcards.model.Deck
import com.unitn.clashofcards.model.GameRoom

class CreateGame  : AppCompatActivity() {

    val db = Firebase.firestore
    var deckcard : DeckGame = DeckGame()
    var size =0
    private var charItem: ArrayList<Card>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.waiting_activity)

        setAlphas()

        var deck: Deck = Deck()
        var docdeck = intent.getStringExtra("idDeck")
        createDoc()
    }










    private  fun createDoc(){
        var find=false
        var created =false
        var docdeck = intent.getStringExtra("idDeck")



            var game: GameRoom = GameRoom(FirebaseAuth.getInstance().uid, "", docdeck, "online",(deckcard.getDeckSize()).toString(),(deckcard.getDeckSize()).toString(),(deckcard.getDeckSize()).toString() ,"", "", "", "", "", "", "", "", "", "", "","","","","","0","0","0","0")
            db.collection("GameRoom")
                .add(game)
                .addOnSuccessListener { doc ->
////////////////////////////////////////start
                    var started=false
                    var d: ListenerRegistration? = null
                     d= doc.addSnapshotListener { snapshot, e ->
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e)
                            return@addSnapshotListener
                        }

                        if (snapshot != null && snapshot.exists()) {

                            if(snapshot.get("status")=="ingame"){
                                if(!started){
                                val intent = Intent(this, GameActivityFunction::class.java)
                                    deckcard.setGameRoom(doc.id)
                                    deckcard.setuid("uid1")
                                    deckcard.setuidopponet("uid2")
                                    startActivity(intent)
                                finish()
                                    d?.remove()
                                }
                            }
                        } else {
                            Log.d("TAG", "Current data: null")
                        }
                    }


                }



    }









    private fun setAlphas() {
        var doc = intent.getStringExtra("idDeck")
        val docRef = db.collection("Decks").document(doc!!).collection("Cards")
        var card: Card
        var DeckGame: DeckGame= DeckGame()

        docRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null) {
                    Log.d("TAG", "DocumentSnapshot data: " + task.result)
                    task.result!!.forEach {
                        val alpha = it.get("alpha").toString()
                        val icons = it.get("icons").toString()
                        val id = it.id
                        var attributename1 = it.get("attributename1").toString()
                        var attributevalue1 = it.get("attributevalue1").toString()
                        var attributename2 = it.get("attributename2").toString()
                        var attributevalue2 = it.get("attributevalue2").toString()
                        var attributename3 = it.get("attributename3").toString()
                        var attributevalue3 = it.get("attributevalue3").toString()
                        var attributename4 = it.get("attributename4").toString()
                        var attributevalue4 = it.get("attributevalue4").toString()
                        var attributename5 = it.get("attributename5").toString()
                        var attributevalue5 = it.get("attributevalue5").toString()
                        var special = it.get("special") as Boolean
                        card = Card(id, icons, alpha, attributename1,attributevalue1,attributename2,attributevalue2, attributename3,attributevalue3, attributename4, attributevalue4, attributename5, attributevalue5, special)

                        if(card!=null){
                            size++
                            charItem!!.add(card)

                            DeckGame.setArray(charItem!!,size)

                        }
                    }
                } else {
                    Log.d("TAG", "No such document")
                }
            } else {
                Log.d("TAG", "get failed with ", task.exception)
            }
        }

    }










}
