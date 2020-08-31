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
import com.google.gson.JsonObject
import com.unitn.clashofcards.adapters.DeckAdapter
import com.unitn.clashofcards.adapters.DeckMarketAdapter
import com.unitn.clashofcards.model.Deck
import com.unitn.clashofcards.model.Card
import com.unitn.clashofcards.model.User
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.typeOf


class MarketDeckActivity : AppCompatActivity() {
    // [START declare_database_ref]

    private var recyclerView: RecyclerView? = null
    private var charItem: MutableList<Deck>? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var alphaAdapters: DeckMarketAdapter? = null
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
        var json : MyJSON = MyJSON()
        var item : String = String()
        var rootObject= JSONObject()
        var rootArray= JSONArray()
        item = "["

        var deck: Deck = Deck()
        charItem!!.clear()
        db.collection("Decks").whereEqualTo("premium", true)
            .get()
            .addOnSuccessListener { document ->
                document.forEach {
                    if (document != null) {
                        var alpha = it.get("alpha").toString()
                        var icons = it.get("icons").toString()
                        var id =it.id
                        var description = it.get("description").toString()
                        var price = it.get("price").toString()
                        var premium : Boolean = it.get("premium") as Boolean
                        rootObject.put("id",id)
                        rootObject.put("icons",icons)
                        rootObject.put("description",description)
                        rootObject.put("alpha",alpha)
                        rootObject.put("price",price)
                        rootArray.put(rootObject)
                        rootObject = JSONObject()
                        deck = Deck(id,icons,alpha,description,price,premium)
                        if (deck != null) {
                            charItem!!.add(deck)
                            alphaAdapters = DeckMarketAdapter(applicationContext, ArrayList(charItem!!))
                            recyclerView?.adapter = alphaAdapters
                            recyclerView?.adapter?.notifyDataSetChanged()
                        }
                    } else {
                        Log.d("TAG", "No such document")
                    }
                }
                json.setArray(rootArray)
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
                }
    }



