package com.unitn.clashofcards


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unitn.clashofcards.adapters.DeckAdapter
import com.unitn.clashofcards.model.Deck
import com.unitn.clashofcards.model.Card


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
        val docRef = db.collection("Decks")
        var deck: Deck
        docRef.addSnapshotListener{  snapshot , e ->
            if(snapshot!=null ){
                charItem!!.clear()
                val document = snapshot.documents
                document.forEach{
                  val alpha = it.get("alpha").toString()
                    val icons = it.get("icons").toString()
                    val id =it.id
                     deck = Deck(id,icons,alpha)
                    if(deck!=null){
                         charItem!!.add(deck)
                        alphaAdapters = DeckAdapter(applicationContext, ArrayList(charItem!!))
                        recyclerView?.adapter = alphaAdapters
                        recyclerView?.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }

    }

    }




