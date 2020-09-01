package com.unitn.clashofcards.matcher

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unitn.clashofcards.LoginActivity
import com.unitn.clashofcards.MarketDeckActivity
import com.unitn.clashofcards.R
import com.unitn.clashofcards.adapters.DeckAdapter
import com.unitn.clashofcards.model.Deck
import com.unitn.clashofcards.model.GameRoom

class WaitingActivity: AppCompatActivity() {
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.waiting_activity)
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog,null)
        val message = dialogView.findViewById<TextView>(R.id.message)
        message.text = "Waiting for an opponent..."
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
        var deck:Deck = Deck()
        var doc = intent.getStringExtra("idDeck")
        if (doc != null) {
            db.collection("Decks").document(doc)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val alpha = document.get("alpha").toString()
                        val icons = document.get("icons").toString()
                        val id =document.id
                        var description = document.get("description").toString()
                        var price= document.get("price").toString()
                        var premium= document.get("premium") as Boolean
                        deck = Deck(id,icons,alpha,description,price,premium)
                    } else {
                        Log.d("TAG", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "get failed with ", exception)
                }
        }

            var find=false
            var created =false
        db.collection("GameRoom").whereEqualTo("status", "online").whereEqualTo("deck", doc)
            .get()
            .addOnSuccessListener { document ->
                println("ci passo 0")
                println(document)
                //join a game room
                if (document != null) {
                    println("ci passo 1")
                    document.forEach{
                        println("ci passo 2")
                        find=true
                        if (it != null && !created) {
                            println("ci passo 3")

                            db.runTransaction { transaction ->
                                        val snapshot = transaction.get(it.reference )
                                        //val uid2 = snapshot.getString("uid2")!!
                                        transaction.update(it.reference , "uid2", FirebaseAuth.getInstance().uid)
                                        transaction.update(it.reference , "status", "ingame")
                                val intent = Intent(this, GameActivity::class.java)
                                startActivity(intent)
                                dialog.dismiss()
                                finish()

                                // Success
                                        null
                                    }.addOnSuccessListener { Log.d("TAG", "Transaction success!") }
                                        .addOnFailureListener { e -> Log.w("TAG", "Transaction failure.", e) }


                                } else {

                                }

                    }
                    if(!find) {
                        created=true
                        var game: GameRoom = GameRoom(FirebaseAuth.getInstance().uid, "", doc, "online")
                        db.collection("GameRoom")
                            .add(game)
                            .addOnSuccessListener { doc ->
////////////////////////////////////////start

                                doc.addSnapshotListener { snapshot, e ->
                                    if (e != null) {
                                        Log.w("TAG", "Listen failed.", e)
                                        return@addSnapshotListener
                                    }

                                    if (snapshot != null && snapshot.exists()) {
                                            if(snapshot.get("status").toString()=="ingame"){
                                                val intent = Intent(this, GameActivity::class.java)
                                                startActivity(intent)
                                                dialog.dismiss()
                                                finish()
                                            }
                                    } else {
                                        Log.d("TAG", "Current data: null")
                                    }
                                }





                                ////////end
                            }
                            .addOnFailureListener { e ->
                                Log.w("TAG", "Error adding document", e)
                            }

                    }
                } else {
                    Log.d("TAG", "No such document")
                }
              //end
            }
            .addOnFailureListener { e ->
                //create a game room

                Log.w("TAG", "Error adding document", e)

                //end creation

            }

        }

    }