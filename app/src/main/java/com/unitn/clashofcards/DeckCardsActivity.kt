package com.unitn.clashofcards


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.unitn.clashofcards.adapters.DeckAdapter
import com.unitn.clashofcards.adapters.DeckCardsAdapter
import com.unitn.clashofcards.model.Deck


class DeckCardsActivity : AppCompatActivity() {
    // [START declare_database_ref]

    private var recyclerView: RecyclerView? = null
    private var charItem: ArrayList<Deck>? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var alphaAdapters: DeckCardsAdapter? = null



    val dbReference: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deckcards)
        recyclerView = findViewById(R.id.recycler_view_itemcards)
        gridLayoutManager =
            GridLayoutManager(applicationContext, 2, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)

        charItem = ArrayList()
        charItem = setAlphas()
        alphaAdapters = DeckCardsAdapter(applicationContext, charItem!!)
        recyclerView?.adapter = alphaAdapters

    }

    private fun setAlphas(): ArrayList<Deck> {

        val products: ArrayList<Deck> = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference()
        val commandsRef = ref.child("Cards")
        commandsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                products.clear()
                for (productSnapshot in dataSnapshot.children) {
                    val product = productSnapshot.getValue(Deck::class.java)
                    products.add(product!!)
                }
                println(products)
                recyclerView?.adapter?.notifyDataSetChanged();

            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })
        return products
    }
}




