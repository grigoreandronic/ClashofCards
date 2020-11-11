package com.unitn.clashofcards.matcher

import android.app.Dialog
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
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
    private var charItem: MutableList<Card>? = deckcard.getArray().toMutableList()
    val db = Firebase.firestore
    var card: Card = Card()
    var turnplay = ""
    var valueSelected: String = ""
    var useruid=""
    var idRoom: String = ""
    var uid: String = ""
    var uidopponent: String = ""
    var d: ListenerRegistration? = null
    var mLastClickTime: Long = 0
    var progres: Int = 0
    var turn = 0
    var firstime = true
    var move = false
    var decksize=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngameBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)
        charItem!!.addAll(deckcard.getArray().toMutableList())
        setAnimation()
        var data =
            hashMapOf(
                "decksize" to (deckcard.getDeckSize()).toString(),
                "uid1decksize" to (deckcard.getDeckSize()).toString(),
                "uid2decksize" to (deckcard.getDeckSize()).toString()
            )
        setFieldValueStart(data)
        setGameBase()
        setPlayingCard()
        listenDB()
        binding.greater.setOnClickListener {
            binding.greater.visibility = View.INVISIBLE
            binding.lower.visibility = View.INVISIBLE
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            var data =
                hashMapOf(
                    "operation" to "greater",
                    "valueselected" to uid + valueSelected
                )

            setFieldValue(data)

        }

        binding.lower.setOnClickListener {
            binding.greater.visibility = View.INVISIBLE
            binding.lower.visibility = View.INVISIBLE
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            var data =
                hashMapOf(
                    "operation" to "lower",
                    "valueselected" to uid + valueSelected
                )
            setFieldValue(data)

        }

    }

    private fun listenDB() {
        val docRef = db.collection("GameRoom").document(idRoom)
        d = docRef.addSnapshotListener { snapshot, e ->
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
                    useruid=snapshot.get("uid1").toString()

                    decksize= snapshot.get("uid1decksize").toString()
                    if (snapshot.get("uid1move") == "gamewin" && !move) {
                        move = true
                        var data =
                            hashMapOf(
                                "uid1move" to ""
                            )
                        setFieldValue(data)
                        createDialogGame(true)

                    } else if ((snapshot.get("uid1move") == "setwin") && !move) {
                        move = true
                        decksize= snapshot.get("uid1decksize").toString()
                        var data =
                            hashMapOf(
                                "uid1move" to ""
                            )
                        setFieldValue(data)
                        createDialogSet(true)

                    } else if ((snapshot.get("uid1move") == "turnwinswitch") && !move) {
                        move = true
                        decksize= snapshot.get("uid1decksize").toString()
                        var data =
                            hashMapOf(
                                "uid1move" to ""
                            )
                        setFieldValue(data)
                        createDialogTurnSwitch(true)

                    } else if ((snapshot.get("uid1move") == "turnwin") && !move) {
                        move = true
                        decksize= snapshot.get("uid1decksize").toString()
                        var data =
                            hashMapOf(
                                "uid1move" to ""
                            )
                        setFieldValue(data)
                        createDialogTurn(true)
                    } else if ((snapshot.get("uid1move") == "turnlost") && !move) {
                        move = true
                        decksize= snapshot.get("uid1decksize").toString()
                        var data =
                            hashMapOf(
                                "uid1move" to ""
                            )
                        setFieldValue(data)
                        createDialogTurn(false)
                    } else if (snapshot.get("uid1move") == "gamelost" && !move) {
                        decksize= snapshot.get("uid1decksize").toString()
                        move = true

                        var data =
                            hashMapOf(
                                "uid1move" to ""
                            )
                        setFieldValue(data)
                        createDialogGame(false)

                    } else if ((snapshot.get("uid1move") == "setlost") && !move) {
                        decksize= snapshot.get("uid1decksize").toString()
                        move = true

                        var data =
                            hashMapOf(
                                "uid1move" to ""
                            )
                        setFieldValue(data)
                        createDialogSet(false)

                    } else if ((snapshot.get("uid1move") == "turnlostswitch") && !move) {
                        move = true
                        decksize= snapshot.get("uid1decksize").toString()

                        var data =
                            hashMapOf(
                                "uid1move" to ""
                            )
                        setFieldValue(data)
                        createDialogTurnSwitch(false)

                    }

                } else if (uid == "uid2") {
                    useruid=snapshot.get("uid1").toString()
                    decksize= snapshot.get("uid1decksize").toString()
                    if (snapshot.get("uid2move") == "gamewin" && !move) {
                        move = true

                        var data =
                            hashMapOf(
                                "uid2move" to ""
                            )
                        setFieldValue(data)
                        createDialogGame(true)

                    } else if ((snapshot.get("uid2move") == "setwin") && !move) {
                        move = true
                        decksize= snapshot.get("uid2decksize").toString()
                        var data =
                            hashMapOf(
                                "uid2move" to ""
                            )
                        setFieldValue(data)
                        createDialogSet(true)

                    } else if ((snapshot.get("uid2move") == "turnwinswitch") && !move) {
                        move = true
                        decksize= snapshot.get("uid2decksize").toString()
                        var data =
                            hashMapOf(
                                "uid2move" to ""
                            )
                        setFieldValue(data)
                        createDialogTurnSwitch(true)

                    } else if ((snapshot.get("uid2move") == "turnwin") && !move) {
                        move = true
                        decksize= snapshot.get("uid2decksize").toString()

                        var data =
                            hashMapOf(
                                "uid2move" to ""
                            )
                        setFieldValue(data)
                        createDialogTurn(true)

                    } else if ((snapshot.get("uid2move") == "turnlost") && !move) {
                        decksize= snapshot.get("uid2decksize").toString()

                        move = true
                        var data =
                            hashMapOf(
                                "uid2move" to ""
                            )
                        setFieldValue(data)
                        createDialogTurn(false)

                    } else if (snapshot.get("uid2move") == "gamelost" && !move) {
                        move = true

                        var data =
                            hashMapOf(
                                "uid2move" to ""
                            )
                        setFieldValue(data)
                        createDialogGame(false)

                    } else if ((snapshot.get("uid2move") == "setlost") && !move) {
                        decksize= snapshot.get("uid2decksize").toString()

                        move = true
                        var data =
                            hashMapOf(
                                "uid2move" to ""
                            )
                        setFieldValue(data)
                        createDialogSet(false)

                    } else if ((snapshot.get("uid2move") == "turnlostswitch") && !move) {
                        decksize= snapshot.get("uid2decksize").toString()
                        move = true
                        var data =
                            hashMapOf(
                                "uid2move" to ""
                            )
                        setFieldValue(data)
                        createDialogTurnSwitch(false)

                    }

                }
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

    private fun setTurn(_turn: Boolean) {
        if (_turn) {
            turn = 1
            if (!firstime) {
                binding.turntext.text = "YOUR TURN"
                binding.turntext.visibility = View.VISIBLE
                binding.greater.visibility = View.VISIBLE
                binding.lower.visibility = View.VISIBLE
            }
        } else {
            turn = 2
            if (!firstime) {
                binding.turntext.text = "OPPONENT TURN"
                binding.turntext.visibility = View.VISIBLE
                binding.greater.visibility = View.INVISIBLE
                binding.lower.visibility = View.INVISIBLE
            }
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
            binding.attributevalue1.setTextColor(Color.parseColor("#000000"))
            binding.imageView1.setImageResource(R.drawable.arrowblack)
            viewContainer3.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue3.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView3.setImageResource(R.drawable.arrow)
            viewContainer4.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue4.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView4.setImageResource(R.drawable.arrow)
            viewContainer5.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue5.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView5.setImageResource(R.drawable.arrow)
            viewContainer2.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue2.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView2.setImageResource(R.drawable.arrow)
        }

        viewContainer2.setOnClickListener {
            valueSelected = "value2"
            println("valsel" + valueSelected)
            viewContainer2.setBackgroundColor(Color.parseColor("#F15A22"))
            binding.attributevalue2.setBackgroundColor(Color.parseColor("#000000"))
            binding.imageView2.setImageResource(R.drawable.arrowblack)
            viewContainer3.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue3.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView3.setImageResource(R.drawable.arrow)
            viewContainer4.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue4.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView4.setImageResource(R.drawable.arrow)
            viewContainer5.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue5.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView5.setImageResource(R.drawable.arrow)
            viewContainer1.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue1.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView1.setImageResource(R.drawable.arrow)

        }
        viewContainer3.setOnClickListener {
            valueSelected = "value3"
            println("valsel" + valueSelected)

            viewContainer3.setBackgroundColor(Color.parseColor("#F15A22"))
            binding.attributevalue3.setTextColor(Color.parseColor("#000000"))
            binding.imageView3.setImageResource(R.drawable.arrowblack)
            viewContainer1.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue1.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView1.setImageResource(R.drawable.arrow)
            viewContainer4.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue4.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView4.setImageResource(R.drawable.arrow)
            viewContainer5.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue5.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView5.setImageResource(R.drawable.arrow)
            viewContainer2.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue2.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView2.setImageResource(R.drawable.arrow)

        }
        viewContainer4.setOnClickListener {
            valueSelected = "value4"
            println("valsel" + valueSelected)

            viewContainer4.setBackgroundColor(Color.parseColor("#F15A22"))
            binding.attributevalue4.setTextColor(Color.parseColor("#000000"))
            binding.imageView4.setImageResource(R.drawable.arrowblack)
            viewContainer3.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue3.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView3.setImageResource(R.drawable.arrow)
            viewContainer1.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue1.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView1.setImageResource(R.drawable.arrow)
            viewContainer5.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue5.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView5.setImageResource(R.drawable.arrow)
            viewContainer2.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue2.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView2.setImageResource(R.drawable.arrow)

        }
        viewContainer5.setOnClickListener {
            valueSelected = "value5"
            println("valsel" + valueSelected)
            viewContainer5.setBackgroundColor(Color.parseColor("#F15A22"))
            binding.attributevalue5.setTextColor(Color.parseColor("#000000"))
            binding.imageView5.setImageResource(R.drawable.arrowblack)
            viewContainer3.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue3.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView3.setImageResource(R.drawable.arrow)
            viewContainer4.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue4.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView4.setImageResource(R.drawable.arrow)
            viewContainer1.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue1.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView1.setImageResource(R.drawable.arrow)
            viewContainer2.setBackgroundResource(android.R.color.transparent)
            binding.attributevalue2.setTextColor(Color.parseColor("#F15A22"))
            binding.imageView2.setImageResource(R.drawable.arrow)
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
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.fragment_set_result)
        if (win) {
            dialog.dialoginfo.text = "YOU WIN THE SET"
            charItem!!.addAll(deckcard.getArray().toMutableList())
        } else {
            dialog.dialoginfo.text = "YOU LOST THE SET"
            charItem!!.addAll(deckcard.getArray().toMutableList())
        }
        dialog.show()
        //dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            move = false
            setPlayingCard()
        }, 4000)


    }


    private fun createDialogTurn(win: Boolean) {
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.fragment_turn_result)
        if (win) {
            dialog.turnwinner.text = "YOU WIN THE TURN"
        } else {

            dialog.turnwinner.text = "YOU LOST THE TURN"
            charItem!!.remove(card)
            Handler(Looper.getMainLooper()).postDelayed({
                setPlayingCard()

            }, 1000)

        }
        dialog.show()

        //dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            move = false
            setAnimationDeck(decksize)

        }, 4000)


    }

    private fun createDialogTurnSwitch(win: Boolean) {
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.fragment_turn_result)
        if (win) {
            dialog.turnwinner.text = "YOU WIN THE TURN "
            setPlayingCard()
        } else {

            dialog.turnwinner.text = "YOU LOST THE TURN"
            charItem!!.remove(card)
            Handler(Looper.getMainLooper()).postDelayed({
                setPlayingCard()
            }, 1000)
        }
        dialog.show()

        //dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            move = false
            setAnimationDeck(decksize)

        }, 4000)


    }

    private fun createDialogGame(win: Boolean) {
        Thread.MAX_PRIORITY
        var dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.fragment_game_result)
        var score=""
        if (win) {
            dialog.gamewinner.text = "YOU WIN THE GAME"
            val docRef = db.collection("Users").document(useruid)
            val data = firestore.runTransaction {
                val snapshot = it.get(docRef)
                var wins = snapshot.getString("wins")!!.toInt() +1
                it.update(docRef, "wins", wins)
            }
        } else {
            dialog.gamewinner.text = "YOU LOST THE GAME"
            val docRef = db.collection("Users").document(useruid)
            val data = firestore.runTransaction {
                val snapshot = it.get(docRef)
                var defeats = snapshot.getString("defeats")!!.toInt() +1
                it.update(docRef, "defeats", defeats)
            }
        }
        dialog.show()
        //dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        Handler(Looper.getMainLooper()).postDelayed({
            dialog.dismiss()
            finish()
        }, 4000)


    }


    private fun setFieldValueStart(hashMap: HashMap<String, String>) {

        val docRef = db.collection("GameRoom").document(deckcard.getGameRoom())
        val data = firestore.runTransaction {
            val snapshot = it.get(docRef)
            turnplay = snapshot.getString("turn")!!
            it.update(docRef, hashMap as Map<String, Any>)
        }


    }

    private fun setFieldValue(hashMap: HashMap<String, String>) {

        val docRef = db.collection("GameRoom").document(deckcard.getGameRoom())
        val data = firestore.runTransaction {
            val snapshot = it.get(docRef)
            turnplay = snapshot.getString("turn")!!
            it.update(docRef, hashMap as Map<String, Any>)
        }


    }

    private fun setAnimation() {
        binding.watchProgress.playAnimation()
        binding.watchProgress.addAnimatorUpdateListener { valueAnimator ->
            // Set animation progress
            progres = (valueAnimator.animatedValue as Float * 100).toInt()
            if (progres == 99 && turn == 1) {
                binding.watchProgress.cancelAnimation()
                binding.watchProgress.visibility = View.GONE
                binding.waitingtext3.text = "YOUR TURN"

                binding.waitingtext3.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.waitingtext3.visibility = View.INVISIBLE
                    binding.turntext.text = "YOUR TURN"
                    binding.turntext.visibility = View.VISIBLE
                    binding.greater.visibility = View.VISIBLE
                    binding.lower.visibility = View.VISIBLE
                    binding.cardLayout.visibility = View.VISIBLE
                    firstime = false
                    setAnimationDeck(deckcard.getDeckSize().toString())

                }, 2000)
            } else if (progres == 99 && turn == 2) {
                binding.watchProgress.cancelAnimation()
                binding.watchProgress.visibility = View.INVISIBLE
                binding.waitingtext3.text = "OPPONENT TURN"
                binding.waitingtext3.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.waitingtext3.visibility = View.GONE
                    binding.turntext.text = "OPPONENT TURN"
                    binding.turntext.visibility = View.VISIBLE
                    binding.greater.visibility = View.INVISIBLE
                    binding.lower.visibility = View.INVISIBLE
                    binding.cardLayout.visibility = View.VISIBLE
                    firstime = false
                    setAnimationDeck("")

                }, 2000)

            }

        }
    }

    private fun setAnimationDeck(cardNumber : String) {
        binding.deckShuffle.setAnimation("card_shuffle.json")
        binding.deckShuffle.loop(false)
        binding.deckShuffle.speed = 1f
        binding.deckShuffle.playAnimation()
        binding.deckShuffle.addAnimatorUpdateListener { valueAnimator ->
            // Set animation progress
            var progres = (valueAnimator.animatedValue as Float * 100).toInt()
            if (progres == 98 ) {
                binding.deckShuffle.pauseAnimation()
                binding.decksize.text=cardNumber

            }
        }

    }
}





