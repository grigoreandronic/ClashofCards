package com.unitn.clashofcards


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.unitn.clashofcards.adapters.DeckAdapter
import com.unitn.clashofcards.model.Deck


class DeckActivity : AppCompatActivity() {
    // [START declare_database_ref]

    private var recyclerView: RecyclerView? = null
    private var charItem: ArrayList<Deck>? = null
    private var gridLayoutManager: GridLayoutManager? = null
    private var alphaAdapters: DeckAdapter? = null




    val dbReference: DatabaseReference = FirebaseDatabase.getInstance().reference
    val imgRef = dbReference.child("img")
    val imglink = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck)
        recyclerView = findViewById(R.id.recycler_view_item)
        gridLayoutManager =
            GridLayoutManager(applicationContext, 2, LinearLayoutManager.VERTICAL, false)
        recyclerView?.layoutManager = gridLayoutManager
        recyclerView?.setHasFixedSize(true)
        charItem = ArrayList()
        charItem = setAlphas()
        alphaAdapters = DeckAdapter(applicationContext, charItem!!)
        recyclerView?.adapter = alphaAdapters

    }

    private fun setAlphas(): ArrayList<Deck> {

        val products: ArrayList<Deck> = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference()
        val commandsRef = ref.child("Deck")
        commandsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                products.clear()
                for (productSnapshot in dataSnapshot.children) {
                    val product = productSnapshot.getValue(Deck::class.java)
                    products.add(product!!)
                }
                recyclerView?.adapter?.notifyDataSetChanged();

            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })
        return products
    }
    }




