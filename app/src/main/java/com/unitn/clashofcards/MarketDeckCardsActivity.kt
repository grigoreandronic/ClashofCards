package com.unitn.clashofcards


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unitn.clashofcards.adapters.DeckAdapter
import com.unitn.clashofcards.adapters.DeckCardsAdapter
import com.unitn.clashofcards.model.Deck
import com.unitn.clashofcards.model.Card


class MarketDeckCardsActivity : AppCompatActivity() {
    // [START declare_database_ref]

    private var recyclerView: RecyclerView? = null
    private var charItem: MutableList<Card>? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var alphaAdapters: DeckCardsAdapter? = null
    val db = Firebase.firestore




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marketdeckcards)
        recyclerView = findViewById(R.id.recycler_view_itemcards)
        gridLayoutManager =
            GridLayoutManager(applicationContext, 2, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        charItem = ArrayList()
        setAlphas()


    }

    private fun setAlphas() {
        var doc = intent.getStringExtra("idDeck")
        val docRef = db.collection("Decks").document(doc!!).collection("Cards")
        var docRef2 = db.collection("Decks")
        var card: Card
        docRef.addSnapshotListener{  snapshot , e ->
            if(snapshot!=null ){
                charItem!!.clear()
                val document = snapshot.documents
                document.forEach {
                    val alpha = it.get("alpha").toString()
                    val icons = it.get("icons").toString()
                    val id = it.id
                    card = Card(id, icons, alpha)
                        println("cacard"+card)
                    if(card!=null){
                        charItem!!.add(card)
                        alphaAdapters = DeckCardsAdapter(applicationContext, ArrayList(charItem!!))
                        recyclerView?.adapter = alphaAdapters
                        recyclerView?.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }

    }

}




