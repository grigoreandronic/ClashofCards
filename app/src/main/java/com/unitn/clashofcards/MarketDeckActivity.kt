package com.unitn.clashofcards


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unitn.clashofcards.adapters.DeckAdapter
import com.unitn.clashofcards.model.Deck
import com.unitn.clashofcards.model.Card
import com.unitn.clashofcards.model.User
import kotlin.reflect.typeOf


class MarketDeckActivity : AppCompatActivity() {
    // [START declare_database_ref]

    private var recyclerView: RecyclerView? = null
    private var charItem: MutableList<Deck>? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var alphaAdapters: DeckAdapter? = null
    val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marketdeck)
        recyclerView = findViewById(R.id.recycler_view_item)
        gridLayoutManager =
            GridLayoutManager(applicationContext, 2, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        charItem = ArrayList()
        setAlphas()

    }

    private fun setAlphas() {
        val docRef = db.collection("Decks")
        var deck: Deck
        charItem!!.clear()

        db.collection("Decks").whereEqualTo("premium", true)
            .get()
            .addOnSuccessListener { document ->
                document.forEach {
                    if (document != null) {
                        val alpha = it.get("alpha").toString()
                        val icons = it.get("icons").toString()
                        val id =it.id
                        val premium : Boolean = it.get("premium") as Boolean
                        deck = Deck(id,icons, alpha,premium)
                        if (deck != null) {
                            charItem!!.add(deck)
                            alphaAdapters = DeckAdapter(applicationContext, ArrayList(charItem!!))
                            recyclerView?.adapter = alphaAdapters
                            recyclerView?.adapter?.notifyDataSetChanged()
                        }
                    } else {
                        Log.d("TAG", "No such document")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
                }
    }



