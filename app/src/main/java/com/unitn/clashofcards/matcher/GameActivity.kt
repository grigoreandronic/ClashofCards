package com.unitn.clashofcards.matcher

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bolts.Task
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.Tasks
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.unitn.clashofcards.DeckActivity
import com.unitn.clashofcards.LoginActivity
import com.unitn.clashofcards.R
import com.unitn.clashofcards.adapters.DeckCardsAdapter
import com.unitn.clashofcards.model.Card
import java.util.concurrent.atomic.AtomicBoolean

class GameActivity  : AppCompatActivity() {
    // [START declare_database_ref]
    var deckcard : DeckGame = DeckGame()
    private val charItem: MutableList<Card>? = deckcard.getArray()
    val db = Firebase.firestore
    var card: Card = Card()
    var turnplay=""
    var setup=true
    var valueSelected : String = ""
    var firstsetup1=true
    var firstsetup2=true
    var rnds :Int =0
    var numberofturn=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingame)
        setStart()
        setPlayingCard()
        val view_container = findViewById<ImageView>(R.id.view_container)
        val view_container2 = findViewById<ImageView>(R.id.view_container2)
        val view_container3 = findViewById<ImageView>(R.id.view_container3)
        val view_container4 = findViewById<ImageView>(R.id.view_container4)
        val view_container5 = findViewById<ImageView>(R.id.view_container5)

        view_container.setOnClickListener {
            valueSelected= "value1"
            view_container.setBackgroundColor(Color.parseColor("#F15A22"))
            view_container3.setBackgroundResource(android.R.color.transparent)
            view_container4.setBackgroundResource(android.R.color.transparent)
            view_container5.setBackgroundResource(android.R.color.transparent)
            view_container2.setBackgroundResource(android.R.color.transparent)
            }
        view_container2.setOnClickListener {
            valueSelected= "value2"
            view_container2.setBackgroundColor(Color.parseColor("#F15A22"))
            view_container3.setBackgroundResource(android.R.color.transparent)
            view_container4.setBackgroundResource(android.R.color.transparent)
            view_container5.setBackgroundResource(android.R.color.transparent)
            view_container.setBackgroundResource(android.R.color.transparent)

        }
        view_container3.setOnClickListener {
            valueSelected= "value3"
            view_container3.setBackgroundColor(Color.parseColor("#F15A22"))
            view_container.setBackgroundResource(android.R.color.transparent)
            view_container4.setBackgroundResource(android.R.color.transparent)
            view_container5.setBackgroundResource(android.R.color.transparent)
            view_container2.setBackgroundResource(android.R.color.transparent)

        }
        view_container4.setOnClickListener {
            valueSelected= "value4"
            view_container4.setBackgroundColor(Color.parseColor("#F15A22"))
            view_container3.setBackgroundResource(android.R.color.transparent)
            view_container.setBackgroundResource(android.R.color.transparent)
            view_container5.setBackgroundResource(android.R.color.transparent)
            view_container2.setBackgroundResource(android.R.color.transparent)

        }
        view_container5.setOnClickListener {
            valueSelected= "value5"
            view_container5.setBackgroundColor(Color.parseColor("#F15A22"))
            view_container3.setBackgroundResource(android.R.color.transparent)
            view_container4.setBackgroundResource(android.R.color.transparent)
            view_container.setBackgroundResource(android.R.color.transparent)
            view_container2.setBackgroundResource(android.R.color.transparent)
        }

    }




    private fun setPlayingCard() {
        val idRoom: String? = intent.getStringExtra("idRoom")
        val uid= intent.getStringExtra("uid")
            if((charItem!!.size)-1>=0){
                rnds = (0..((charItem.size)-1)).random()

            }else{
                rnds = (0..((charItem.size))).random()
            }

        if(charItem.size>0) {
            card = charItem.get(rnds)
            val cardimage = findViewById<ImageView>(R.id.cardImage)
            val cardtext = findViewById<TextView>(R.id.cardTitle)
            var attributename1 = findViewById<TextView>(R.id.attributename1)
            var attributevalue1 = findViewById<TextView>(R.id.attributevalue1)
            var attributename2 = findViewById<TextView>(R.id.attributename2)
            var attributevalue2 = findViewById<TextView>(R.id.attributevalue2)
            var attributename3 = findViewById<TextView>(R.id.attributename3)
            var attributevalue3 = findViewById<TextView>(R.id.attributevalue3)
            var attributename4 = findViewById<TextView>(R.id.attributename4)
            var attributevalue4 = findViewById<TextView>(R.id.attributevalue4)
            var attributename5 = findViewById<TextView>(R.id.attributename5)
            var attributevalue5 = findViewById<TextView>(R.id.attributevalue5)

            cardtext.text = card.alpha
            attributename1.text = card.attributename1!!
            attributevalue1.text = card.attributevalue1!!
            attributename2.text = card.attributename2!!
            attributevalue2.text = card.attributevalue2!!
            attributename3.text = card.attributename3!!
            attributevalue3.text = card.attributevalue3!!
            attributename4.text = card.attributename4!!
            attributevalue4.text = card.attributevalue4!!
            attributename5.text = card.attributename5!!
            attributevalue5.text = card.attributevalue5!!
            val options: RequestOptions = RequestOptions()
                .skipMemoryCache(true)
                .centerInside()
            Glide.with(this)
                .load(card.icons)
                .apply(options)
                .into(cardimage)

            if (idRoom != null) {
                val sfDocRef = db.collection("GameRoom").document(idRoom)

                db.runTransaction { transaction ->
                    val snapshot = transaction.get(sfDocRef)
                    turnplay = snapshot.getString("turn")!!
                    if (uid == "uid1") {
                        transaction.update(sfDocRef, "uid1value1", card.attributevalue1!!)
                        transaction.update(sfDocRef, "uid1value2", card.attributevalue2!!)
                        transaction.update(sfDocRef, "uid1value3", card.attributevalue3!!)
                        transaction.update(sfDocRef, "uid1value4", card.attributevalue4!!)
                        transaction.update(sfDocRef, "uid1value5", card.attributevalue5!!)

                    } else {
                        transaction.update(sfDocRef, "uid2value1", card.attributevalue1!!)
                        transaction.update(sfDocRef, "uid2value2", card.attributevalue2!!)
                        transaction.update(sfDocRef, "uid2value3", card.attributevalue3!!)
                        transaction.update(sfDocRef, "uid2value4", card.attributevalue4!!)
                        transaction.update(sfDocRef, "uid2value5", card.attributevalue5!!)
                    }


                    // Success
                    null
                }.addOnSuccessListener { setPlayerOne() }
                    .addOnFailureListener { e -> Log.w("TAG", "Transaction  card failure.", e) }
            }
        }
    }
    private fun setStart() {
        val idRoom: String? = intent.getStringExtra("idRoom")

        val rnds = (1..2).random()
        val turn: String
        if(rnds==1){
            turn="uid1"
        }else{
            turn="uid2"

        }
        if(idRoom!=null) {
            var room = db.collection("GameRoom").document(idRoom)
            var transaction= db.runTransaction { transaction ->
                val snapshot = transaction.get(room)

                val start = snapshot.getString("turn")!!
                if(start==""){
                    transaction.update(room, "turn", turn)
                }
                // Success
                null
            }.addOnSuccessListener { Log.d("TAG", "Transaction success start!") }
                .addOnFailureListener { e -> Log.w("TAG", "Transaction failure start.", e) }


        }
    }



    private fun setPlayerOne() {
        val idRoom: String? = intent.getStringExtra("idRoom")
        val uid= intent.getStringExtra("uid")
        val greater = findViewById<MaterialButton>(R.id.greater)
        val lower = findViewById<MaterialButton>(R.id.lower)
        if(idRoom!=null) {
            val docRef = db.collection("GameRoom").document(idRoom)
            docRef.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val turntext = findViewById<TextView>(R.id.turntext)
                    if (uid == turnplay) {
                        turntext.text="YOUR TURN"
                        greater.visibility = MaterialButton.VISIBLE
                        lower.visibility = MaterialButton.VISIBLE

                        greater.setOnClickListener {
                            val value1 = uid + valueSelected
                            var value2 = ""
                            if (uid == "uid1") {
                                value2 = "uid2" + valueSelected
                            } else {
                                value2 = "uid1" + valueSelected
                            }
                            var conf1: String? = snapshot.get(value1).toString()
                            var conf2: String? = snapshot.get(value2).toString()
                            if (conf1 != null && conf2 != null) {
                                if (conf1.toDouble() > conf2.toDouble()) {
                                    Toast.makeText(this, "you win", Toast.LENGTH_LONG).show()
                                    numberofturn++
                                    if(numberofturn==4){
                                        var transaction = db.runTransaction { transaction ->
                                            val snapshot = transaction.get(snapshot.reference)
                                            if(uid=="uid1"){
                                                transaction.update(snapshot.reference, "turn", "uid2")
                                            }else{
                                                transaction.update(snapshot.reference, "turn", "uid1")
                                            }
                                            // Success
                                            null
                                        }.addOnSuccessListener {
                                            Log.d(
                                                "TAG",
                                                "Transaction success start!"
                                            )
                                        }
                                            .addOnFailureListener { e ->
                                                Log.w(
                                                    "TAG",
                                                    "Transaction failure start.",
                                                    e
                                                )
                                            }
                                    }else{
                                        var transaction = db.runTransaction { transaction ->
                                            val snapshot = transaction.get(snapshot.reference)
                                            transaction.update(snapshot.reference, "turnwinner", uid)
                                            // Success
                                            null
                                        }.addOnSuccessListener {
                                            Log.d(
                                                "TAG",
                                                "Transaction success start!"
                                            )
                                        }
                                            .addOnFailureListener { e ->
                                                Log.w(
                                                    "TAG",
                                                    "Transaction failure start.",
                                                    e
                                                )
                                            }
                                    }


                                } else if (conf1.toDouble() == conf2.toDouble()) {
                                    Toast.makeText(this, "pareggio", Toast.LENGTH_LONG).show()
                                    var transaction = db.runTransaction { transaction ->
                                        val snapshot = transaction.get(snapshot.reference)
                                        transaction.update(snapshot.reference, "turnwinner", "all")
                                        // Success
                                        null
                                    }.addOnSuccessListener {
                                        Log.d(
                                            "TAG",
                                            "Transaction success start!"
                                        )
                                    }
                                        .addOnFailureListener { e ->
                                            Log.w(
                                                "TAG",
                                                "Transaction failure start.",
                                                e
                                            )
                                        }


                                } else if (conf1.toDouble() < conf2.toDouble()) {
                                    Toast.makeText(this, "you lost", Toast.LENGTH_LONG).show()
                                    var uidwinner = ""
                                    if (uid == "uid1") {
                                        uidwinner = "uid2"
                                    } else {
                                        uidwinner = "uid1"
                                    }
                                    var transaction = db.runTransaction { transaction ->
                                        val snapshot = transaction.get(snapshot.reference)
                                        transaction.update(
                                            snapshot.reference,
                                            "turnwinner",
                                            uidwinner
                                        )
                                        // Success
                                        null
                                    }.addOnSuccessListener {
                                        Log.d(
                                            "TAG",
                                            "Transaction success start!"
                                        )
                                    }
                                        .addOnFailureListener { e ->
                                            Log.w(
                                                "TAG",
                                                "Transaction failure start.",
                                                e
                                            )
                                        }
                                }


                            }
                        }



                        lower.setOnClickListener {
                            val value1 = uid + valueSelected
                            var value2 = ""
                            if (uid == "uid1") {
                                value2 = "uid2" + valueSelected
                            } else {
                                value2 = "uid1" + valueSelected
                            }
                            var conf1: String? = snapshot.get(value1).toString()
                            var conf2: String? = snapshot.get(value2).toString()
                            if (conf1 != null && conf2 != null) {
                                if (conf1.toDouble() < conf2.toDouble()) {
                                    Toast.makeText(this, "you win", Toast.LENGTH_LONG).show()
                                    setPlayingCard()
                                    var transaction = db.runTransaction { transaction ->
                                        val snapshot = transaction.get(snapshot.reference)
                                        transaction.update(snapshot.reference, "turnwinner", uid)
                                        // Success
                                        null
                                    }.addOnSuccessListener {
                                        Log.d(
                                            "TAG",
                                            "Transaction success start!"
                                        )
                                    }
                                        .addOnFailureListener { e ->
                                            Log.w(
                                                "TAG",
                                                "Transaction failure start.",
                                                e
                                            )
                                        }

                                } else if (conf1.toDouble() == conf2.toDouble()) {
                                    Toast.makeText(this, "pareggio", Toast.LENGTH_LONG).show()

                                    var transaction = db.runTransaction { transaction ->
                                        val snapshot = transaction.get(snapshot.reference)
                                        transaction.update(snapshot.reference, "turnwinner", "all")
                                        // Success
                                        null
                                    }.addOnSuccessListener {
                                        Log.d(
                                            "TAG",
                                            "Transaction success start!"
                                        )
                                    }
                                        .addOnFailureListener { e ->
                                            Log.w(
                                                "TAG",
                                                "Transaction failure start.",
                                                e
                                            )
                                        }


                                } else if (conf1.toDouble() > conf2.toDouble()) {
                                    Toast.makeText(this, "you lost", Toast.LENGTH_LONG).show()
                                    var uidwinner = ""
                                    if (uid == "uid1") {
                                        uidwinner = "uid2"
                                    } else {
                                        uidwinner = "uid1"
                                    }
                                    var transaction = db.runTransaction { transaction ->
                                        val snapshot = transaction.get(snapshot.reference)
                                        transaction.update(
                                            snapshot.reference,
                                            "turnwinner",
                                            uidwinner
                                        )
                                        // Success
                                        null
                                    }.addOnSuccessListener {
                                        Log.d(
                                            "TAG",
                                            "Transaction success start!"
                                        )
                                    }
                                        .addOnFailureListener { e ->
                                            Log.w(
                                                "TAG",
                                                "Transaction failure start.",
                                                e
                                            )
                                        }
                                }


                            }


                        }
                    }else {
                        turntext.text="OPPONENT TURN"
                        greater.visibility = MaterialButton.INVISIBLE
                        lower.visibility = MaterialButton.INVISIBLE
                    }


                    var turnwinner: String? = snapshot.get("turnwinner").toString()

                    if ( turnwinner != uid && turnwinner != "") {
                        var transaction = db.runTransaction { transaction ->
                            val snapshot = transaction.get(snapshot.reference)
                            transaction.update(
                                snapshot.reference,
                                "turnwinner",
                                ""
                            )
                            // Success
                            null
                        }.addOnSuccessListener {
                            Log.d(
                                "TAG",
                                "Transaction success start!"
                            )
                        }
                            .addOnFailureListener { e ->
                                Log.w(
                                    "TAG",
                                    "Transaction failure start.",
                                    e
                                )
                            }
                        setPlayingCard()
                        charItem?.removeAt(rnds)
                    }

                    var setwinner: String? = snapshot.get("setwinner").toString()

                    if ( setwinner != uid && setwinner != "") {
                        var transaction = db.runTransaction { transaction ->
                            val snapshot = transaction.get(snapshot.reference)
                            transaction.update(
                                snapshot.reference,
                                "setwinner",
                                ""
                            )
                            // Success
                            null
                        }.addOnSuccessListener {
                            Log.d(
                                "TAG",
                                "Transaction success start!"
                            )
                        }
                            .addOnFailureListener { e ->
                                Log.w(
                                    "TAG",
                                    "Transaction failure start.",
                                    e
                                )
                            }
                        setPlayingCard()
                        charItem?.removeAt(rnds)
                    }















                }


            }

                } else {
                    Log.d("TAG", "Current data: null")
                }
            }
        }





