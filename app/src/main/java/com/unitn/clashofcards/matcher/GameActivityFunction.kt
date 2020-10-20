package com.unitn.clashofcards.matcher

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unitn.clashofcards.R
import com.unitn.clashofcards.databinding.ActivityIngameBinding
import com.unitn.clashofcards.model.Card
import kotlinx.android.synthetic.main.activity_setwin.*
import kotlinx.android.synthetic.main.activity_turnwin.*
import kotlinx.android.synthetic.main.fragment_game_result.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.CoroutineContext

class GameActivityFunction : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext = newSingleThreadContext("game")
    var job: Job? = null
    private lateinit var binding: ActivityIngameBinding

    companion object {

        private val TAG = "GameActivityFunction"
    }

    // [START declare_database_ref]
    val firestore = Firebase.firestore
    var deckcard: DeckGame = DeckGame()
    private var charItem: MutableList<Card>? = deckcard.getArray()
    var decksize = deckcard.getDeckSize()
    var decksizeopponent = deckcard.getDeckSize()
    val db = Firebase.firestore
    var card: Card = Card()
    var turnplay = ""
    var valueSelected: String = ""
    var rnds: Int = 0
    var numberofturn = 0
    var idRoom: String = ""
    var uid: String = ""
    var uidopponent: String = ""
    var d: ListenerRegistration? = null
    var currentTurn = ""
    var currentMove = ""
    var previousTurn = ""
    var setwin = 0
    var setlost = 0
    var mLastClickTime: Long = 0
    var case = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityIngameBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)
        setGameBase()
        setPlayingCard()
        listenDB()
        binding.greater.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            var data =
                hashMapOf(
                    "operation" to "greater",
                    "valueselected" to uid+valueSelected
                )

            setFieldValue(data)

        }

        binding.lower.setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            var data =
                hashMapOf(
                    "operation" to "lower",
                    "valueselected" to uid+valueSelected
                )
            setFieldValue(data)

        }

    }

    private fun listenDB() {
        val docRef = db.collection("GameRoom").document(idRoom)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                if (snapshot.get("turn") == uid) {
                    setTurn(true)

                } else if (snapshot.get("turn") == uidopponent) {
                    setTurn(false)
                }
                if (uid == "uid1") {
                    if (snapshot.get("uid1move") == "gamewin") {
                        createDialogGame(true)
                        var data =
                            hashMapOf(
                                "uid1move" to ""
                            )
                        setFieldValue(data)
                    } else if ((snapshot.get("uid1move") == "setwin")) {
                        createDialogSet(true)
                        var data =
                            hashMapOf(
                                "uid1move" to ""
                            )
                        setFieldValue(data)
                    } else if ((snapshot.get("uid1move") == "turnwinswitch")) {
                        createDialogTurn(true)
                        var data =
                            hashMapOf(
                                "uid1move" to ""
                            )
                        setFieldValue(data)
                    } else if ((snapshot.get("uid1move") == "turnwin")) {
                        createDialogTurn(true)
                        var data =
                            hashMapOf(
                                "uid1move" to ""
                            )
                        setFieldValue(data)
                    } else if ((snapshot.get("uid1move") == "turnlost")) {
                        createDialogTurn(false)
                        var data =
                            hashMapOf(
                                "uid1move" to ""
                            )
                        setFieldValue(data)
                    } else if (snapshot.get("uid1move") == "gamelost") {
                        createDialogGame(false)
                        var data =
                            hashMapOf(
                                "uid1move" to ""
                            )
                        setFieldValue(data)
                    } else if ((snapshot.get("uid1move") == "setlost")) {
                        createDialogSet(false)
                        var data =
                            hashMapOf(
                                "uid1move" to ""
                            )
                        setFieldValue(data)
                    } else if ((snapshot.get("uid1move") == "turnlostswitch")) {
                        createDialogTurn(false)
                        var data =
                            hashMapOf(
                                "uid1move" to ""
                            )
                        setFieldValue(data)
                    } else if ((snapshot.get("uid1move") == "turnlost")) {
                        createDialogTurn(false)
                        var data =
                            hashMapOf(
                                "uid1move" to ""
                            )
                        setFieldValue(data)
                    }

                } else if (uid == "uid2") {
                    if (snapshot.get("uid2move") == "gamewin") {
                        createDialogGame(true)
                        var data =
                            hashMapOf(
                                "uid2move" to ""
                            )
                        setFieldValue(data)
                    } else if ((snapshot.get("uid2move") == "setwin")) {
                        createDialogSet(true)
                        var data =
                            hashMapOf(
                                "uid2move" to ""
                            )
                        setFieldValue(data)
                    } else if ((snapshot.get("uid2move") == "turnwinswitch")) {
                        createDialogTurn(true)
                        var data =
                            hashMapOf(
                                "uid2move" to ""
                            )
                        setFieldValue(data)
                    } else if ((snapshot.get("uid2move") == "turnwin")) {
                        createDialogTurn(true)
                        var data =
                            hashMapOf(
                                "uid2move" to ""
                            )
                        setFieldValue(data)
                    } else if ((snapshot.get("uid2move") == "turnlost")) {
                        createDialogTurn(false)
                        var data =
                            hashMapOf(
                                "uid2move" to ""
                            )
                        setFieldValue(data)
                    } else if (snapshot.get("uid2move") == "gamelost") {
                        createDialogGame(false)
                        var data =
                            hashMapOf(
                                "uid2move" to ""
                            )
                        setFieldValue(data)
                    } else if ((snapshot.get("uid2move") == "setlost")) {
                        createDialogSet(false)
                        var data =
                            hashMapOf(
                                "uid2move" to ""
                            )
                        setFieldValue(data)
                    } else if ((snapshot.get("uid2move") == "turnlostswitch")) {
                        createDialogTurn(false)
                        var data =
                            hashMapOf(
                                "uid2move" to ""
                            )
                        setFieldValue(data)
                    } else if ((snapshot.get("uid2move") == "turnlost")) {
                        createDialogTurn(false)
                        var data =
                            hashMapOf(
                                "uid2move" to ""
                            )
                        setFieldValue(data)
                    }

                }
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

    private fun setTurn(turn: Boolean) {
        if (turn) {
            binding.turntext.text = "YOUR TURN"
            binding.greater.visibility = View.VISIBLE
            binding.lower.visibility = View.VISIBLE
        } else {
            binding.turntext.text = "OPPONENT TURN"
            binding.greater.visibility = View.INVISIBLE
            binding.lower.visibility = View.INVISIBLE
        }
    }

    private fun setGameBase() {
        idRoom = deckcard.getGameRoom()
        uid = deckcard.getuid()
        uidopponent = deckcard.getuidopponet()

        val viewContainer1 = binding.viewContainer1
        val viewContainer2 = binding.viewContainer2
        val viewContainer3 = binding.viewContainer3
        val viewContainer4 = binding.viewContainer4
        val viewContainer5 = binding.viewContainer5

        viewContainer1.setOnClickListener {
            valueSelected = "value1"
            println("valsel" + valueSelected)
            viewContainer1.setBackgroundColor(Color.parseColor("#F15A22"))
            viewContainer3.setBackgroundResource(android.R.color.transparent)
            viewContainer4.setBackgroundResource(android.R.color.transparent)
            viewContainer5.setBackgroundResource(android.R.color.transparent)
            viewContainer2.setBackgroundResource(android.R.color.transparent)
        }

        viewContainer2.setOnClickListener {
            valueSelected = "value2"
            println("valsel" + valueSelected)
            viewContainer2.setBackgroundColor(Color.parseColor("#F15A22"))
            viewContainer3.setBackgroundResource(android.R.color.transparent)
            viewContainer4.setBackgroundResource(android.R.color.transparent)
            viewContainer5.setBackgroundResource(android.R.color.transparent)
            viewContainer1.setBackgroundResource(android.R.color.transparent)

        }
        viewContainer3.setOnClickListener {
            valueSelected = "value3"
            println("valsel" + valueSelected)

            viewContainer3.setBackgroundColor(Color.parseColor("#F15A22"))
            viewContainer1.setBackgroundResource(android.R.color.transparent)
            viewContainer4.setBackgroundResource(android.R.color.transparent)
            viewContainer5.setBackgroundResource(android.R.color.transparent)
            viewContainer2.setBackgroundResource(android.R.color.transparent)

        }
        viewContainer4.setOnClickListener {
            valueSelected = "value4"
            println("valsel" + valueSelected)

            viewContainer4.setBackgroundColor(Color.parseColor("#F15A22"))
            viewContainer3.setBackgroundResource(android.R.color.transparent)
            viewContainer1.setBackgroundResource(android.R.color.transparent)
            viewContainer5.setBackgroundResource(android.R.color.transparent)
            viewContainer2.setBackgroundResource(android.R.color.transparent)

        }
        viewContainer5.setOnClickListener {
            valueSelected = "value5"
            println("valsel" + valueSelected)
            viewContainer5.setBackgroundColor(Color.parseColor("#F15A22"))
            viewContainer3.setBackgroundResource(android.R.color.transparent)
            viewContainer4.setBackgroundResource(android.R.color.transparent)
            viewContainer1.setBackgroundResource(android.R.color.transparent)
            viewContainer2.setBackgroundResource(android.R.color.transparent)
        }

    }


    private fun setPlayingCard() {

        card = charItem!!.random()
        var cardimage = binding.cardImage
        var cardtext = binding.cardTitleGame
        var attributename1 = binding.attributename1
        var attributevalue1 = binding.attributevalue1
        var attributename2 = binding.attributename2
        var attributevalue2 = binding.attributevalue2
        var attributename3 = binding.attributename3
        var attributevalue3 = binding.attributevalue3
        var attributename4 = binding.attributename4
        var attributevalue4 = binding.attributevalue4
        var attributename5 = binding.attributename5
        var attributevalue5 = binding.attributevalue5
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

        if (uid == "uid1") {
            var cardvalue =
                hashMapOf(
                    "uid1value1" to card.attributevalue1!!,
                    "uid1value2" to card.attributevalue2!!,
                    "uid1value3" to card.attributevalue3!!,
                    "uid1value4" to card.attributevalue4!!,
                    "uid1value5" to card.attributevalue5!!
                )
            setFieldValue(cardvalue)

        } else {
            var cardvalue =
                hashMapOf(
                    "uid2value1" to card.attributevalue1!!,
                    "uid2value2" to card.attributevalue2!!,
                    "uid2value3" to card.attributevalue3!!,
                    "uid2value4" to card.attributevalue4!!,
                    "uid2value5" to card.attributevalue5!!
                )
            setFieldValue(cardvalue)
        }
    }


    private fun createDialogSet(win: Boolean) {
        Thread.MAX_PRIORITY
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.fragment_set_result)
        if (win) {
            dialog.dialoginfo.text = "YOU WIN THE SET"
        } else {
            dialog.dialoginfo.text = "YOU LOST THE SET"
        }
        dialog.show()
        //dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
        }, 3000)


    }


    private fun createDialogTurn(win: Boolean) {
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.fragment_turn_result)
        if (win) {
            dialog.turnwinner.text = "YOU WIN THE TURN"

        } else {
            dialog.turnwinner.text = "YOU LOST THE TURN"
        }
        dialog.show()

        //dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
        }, 3000)


    }

    private fun createDialogGame(win: Boolean) {
        Thread.MAX_PRIORITY
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.fragment_game_result)
        if (win) {
            dialog.gamewinner.text = "YOU WIN THE GAME"

        } else {
            dialog.gamewinner.text = "YOU LOST THE GAME"
        }
        dialog.show()
        //dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            finish()
        }, 3000)


    }


    private suspend fun getField(field: String)
            : String {
        return try {
            val data = firestore
                .collection("GameRoom")
                .document(idRoom)
                .get()
                .await()
            return data.get(field) as String
        } catch (e: Exception) {
            e.toString()
        }
    }

    private suspend fun getFieldConfr(field: String)
            : String {
        return try {
            val value1 = uid + valueSelected
            var value2 = ""
            value2 = if (uid == "uid1") {
                "uid2" + valueSelected
            } else {
                "uid1" + valueSelected
            }
            val data = firestore
                .collection("GameRoom")
                .document(idRoom)
                .get()
                .await()
            if (field == "1") {
                val value = (data.get(value1) as String)
                return value
            } else {
                val value = (data.get(value2) as String)
                return value
            }
        } catch (e: Exception) {
            e.toString()
        }
    }


    private fun setFieldValue(hashMap: HashMap<String, String>) {

        val sfDocRef = db.collection("GameRoom").document(deckcard.getGameRoom())
        val data = firestore.runTransaction {
            val snapshot = it.get(sfDocRef)
            turnplay = snapshot.getString("turn")!!
            it.update(sfDocRef, hashMap as Map<String, Any>)
        }

    }




}





