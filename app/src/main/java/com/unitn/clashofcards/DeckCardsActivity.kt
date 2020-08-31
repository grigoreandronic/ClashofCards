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


class DeckCardsActivity : AppCompatActivity() {
    // [START declare_database_ref]

    private var recyclerView: RecyclerView? = null
    private var charItem: MutableList<Card>? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var alphaAdapters: DeckCardsAdapter? = null
    val db = Firebase.firestore




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deckcards)
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
                        alphaAdapters = DeckCardsAdapter(applicationContext, ArrayList(charItem!!))
                        recyclerView?.adapter = alphaAdapters
                        recyclerView?.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }

    }

}




