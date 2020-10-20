package com.unitn.clashofcards.matcher

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import bolts.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.unitn.clashofcards.LoginActivity
import com.unitn.clashofcards.MarketDeckActivity
import com.unitn.clashofcards.MyJSON
import com.unitn.clashofcards.R
import com.unitn.clashofcards.adapters.DeckAdapter
import com.unitn.clashofcards.adapters.DeckCardsAdapter
import com.unitn.clashofcards.adapters.DeckMarketAdapter
import com.unitn.clashofcards.model.Card
import com.unitn.clashofcards.model.Deck
import com.unitn.clashofcards.model.GameRoom
import org.json.JSONArray
import org.json.JSONObject

class WaitingActivity: AppCompatActivity() {

    val db = Firebase.firestore
    var deckcard : DeckGame = DeckGame()
    private var charItem: ArrayList<Card>? = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)
            setContentView(R.layout.waiting_activity)


        setAlphas()

        var deck:Deck = Deck()
        var docdeck = intent.getStringExtra("idDeck")
        createDoc()
                }


    private  fun createDoc(){
        var find=false
        var created =false
        var docdeck = intent.getStringExtra("idDeck")
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.progress_dialog,null)
        val message = dialogView.findViewById<TextView>(R.id.message)
        message.text = "Waiting for an opponent..."
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()

        var started=false
        var d: ListenerRegistration? = null
        d= db.collection("GameRoom").whereEqualTo("status", "online").whereEqualTo("deck", docdeck).limit(1)

            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {

                    snapshot.forEach {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e)
                            return@addSnapshotListener
                        }

                        if (snapshot != null && it.exists()) {
                            val intent = Intent(this, GameActivityFunction::class.java)
                            db.runTransaction { transaction ->
                                val snapshot = transaction.get(it.reference)
                                deckcard.setGameRoom(it.id)
                                deckcard.setuid("uid2")
                                deckcard.setuidopponet("uid1")
                                transaction.update(it.reference, "uid2", FirebaseAuth.getInstance().uid)
                                transaction.update(it.reference, "status", "ingame")

                                // Success
                                null
                            }.addOnSuccessListener {

                                startActivity(intent)
                                dialog.dismiss()
                                d?.remove()
                                finish()
                            }
                                .addOnFailureListener { e -> Log.w("TAG", "Transaction failure.", e) }

                        } else {
                            Log.d("TAG", "Current data: null")
                        }
                    }
                }

            }



            /*.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    if(!created) {
                        find = true
                        println("document ${document.id} => ${document.data}")
                        println("document esiste")
                        val data = hashMapOf(
                                "uid2" to FirebaseAuth.getInstance().uid,
                                "status" to "ingame"
                        )
                                document.reference.set(data, SetOptions.merge())

                        val intent = Intent(this, GameActivity::class.java)
                        intent.putExtra("Deck", docdeck)
                        intent.putExtra("idRoom", document.id)
                        intent.putExtra("uid", "uid1")
                        startActivity(intent)
                        dialog.dismiss()
                        finish()



                    }

                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
*/

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
                            charItem!!.add(card)
                            DeckGame.setArray(charItem!!)
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