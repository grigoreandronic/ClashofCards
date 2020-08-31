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


class DeckActivity : AppCompatActivity() {
    // [START declare_database_ref]

    private var recyclerView: RecyclerView? = null
    private var charItem: MutableList<Deck>? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var alphaAdapters: DeckAdapter? = null
    val db = Firebase.firestore




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck)
        recyclerView = findViewById(R.id.recycler_view_item)
        gridLayoutManager =
            GridLayoutManager(applicationContext, 2, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        charItem = ArrayList()
            setAlphas()

    }

    private fun setAlphas() {
        var deck: Deck
        charItem!!.clear()
        db.collection("Users").document("${FirebaseAuth.getInstance().uid}")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val reference: ArrayList<DocumentReference> = document.get("decks") as ArrayList<DocumentReference>
                    reference.forEach{
                    it.get()
                        .addOnSuccessListener { document ->
                            if (document != null) {
                                val alpha = document.get("alpha").toString()
                                val icons = document.get("icons").toString()
                                val id =document.id
                                var description = document.get("description").toString()
                                var price= document.get("price").toString()
                                var premium= document.get("premium") as Boolean
                                deck = Deck(id,icons,alpha,description,price,premium)
                                if(deck!=null){
                                    charItem!!.add(deck)
                                    alphaAdapters = DeckAdapter(applicationContext, ArrayList(charItem!!))
                                    recyclerView?.adapter = alphaAdapters
                                    recyclerView?.adapter?.notifyDataSetChanged()
                                }
                            } else {
                                Log.d("TAG", "No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d("TAG", "get failed with ", exception)
                        }
                    }
                } else {
                    Log.d("TAG", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }

    }




