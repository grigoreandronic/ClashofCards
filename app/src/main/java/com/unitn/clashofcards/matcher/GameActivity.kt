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

class GameActivity  : AppCompatActivity(), CoroutineScope {
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
    var decksize= deckcard.getDeckSize()
    var decksizeopponent=deckcard.getDeckSize()
    val db = Firebase.firestore
    var card: Card = Card()
    var turnplay = ""
    var valueSelected: String = "value1"
    var rnds: Int = 0
    var numberofturn = 0
    var idRoom: String = ""
    var uid: String = ""
    var uidopponent: String = ""
    var d: ListenerRegistration? = null
    var currentTurn =""
    var previousTurn=""
    var lastsetwinner=""
    var lastturnwinner=""
    var matchwinner=""
    var setwin = 0
    var setlost = 0
    var mLastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding=ActivityIngameBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)
        job = launch {
            setGameBase()
        }


    }
    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }

    private suspend fun setGameBase(){
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
            println("valsel"+valueSelected)
            viewContainer1.setBackgroundColor(Color.parseColor("#F15A22"))
            viewContainer3.setBackgroundResource(android.R.color.transparent)
            viewContainer4.setBackgroundResource(android.R.color.transparent)
            viewContainer5.setBackgroundResource(android.R.color.transparent)
            viewContainer2.setBackgroundResource(android.R.color.transparent)
        }

        viewContainer2.setOnClickListener {
            valueSelected = "value2"
            println("valsel"+valueSelected)
            viewContainer2.setBackgroundColor(Color.parseColor("#F15A22"))
            viewContainer3.setBackgroundResource(android.R.color.transparent)
            viewContainer4.setBackgroundResource(android.R.color.transparent)
            viewContainer5.setBackgroundResource(android.R.color.transparent)
            viewContainer1.setBackgroundResource(android.R.color.transparent)

        }
        viewContainer3.setOnClickListener {
            valueSelected = "value3"
            println("valsel"+valueSelected)

            viewContainer3.setBackgroundColor(Color.parseColor("#F15A22"))
            viewContainer1.setBackgroundResource(android.R.color.transparent)
            viewContainer4.setBackgroundResource(android.R.color.transparent)
            viewContainer5.setBackgroundResource(android.R.color.transparent)
            viewContainer2.setBackgroundResource(android.R.color.transparent)

        }
        viewContainer4.setOnClickListener {
            valueSelected = "value4"
            println("valsel"+valueSelected)

            viewContainer4.setBackgroundColor(Color.parseColor("#F15A22"))
            viewContainer3.setBackgroundResource(android.R.color.transparent)
            viewContainer1.setBackgroundResource(android.R.color.transparent)
            viewContainer5.setBackgroundResource(android.R.color.transparent)
            viewContainer2.setBackgroundResource(android.R.color.transparent)

        }
        viewContainer5.setOnClickListener {
            valueSelected = "value5"
            println("valsel"+valueSelected)
            viewContainer5.setBackgroundColor(Color.parseColor("#F15A22"))
            viewContainer3.setBackgroundResource(android.R.color.transparent)
            viewContainer4.setBackgroundResource(android.R.color.transparent)
            viewContainer1.setBackgroundResource(android.R.color.transparent)
            viewContainer2.setBackgroundResource(android.R.color.transparent)
        }
        setStart()
        setPlayingCard()
    }


    private suspend fun setStart() {
        val rnds = (1..2).random()
        val turn: String
        if (rnds == 1) {
            turn = uid
        } else {
            turn = uidopponent
        }
        setTurn(turn)
    }


    private suspend fun setPlayingCard() {
        val uid = deckcard.getuid()
        var size = 0
        if (decksize - 1 >= 0) {
            size = (charItem!!.size)
            rnds = (0 until (size)).random()

        } else {
            rnds = 0
        }

        if (decksize > 0) {

            card = charItem!![rnds]
            val cardimage =binding.cardImage
            val cardtext = binding.cardTitleGame
            val attributename1 = binding.attributename1
            val attributevalue1 = binding.attributevalue1
            val attributename2 = binding.attributename2
            val attributevalue2 =binding.attributevalue2
            val attributename3 = binding.attributename3
            val attributevalue3 = binding.attributevalue3
            val attributename4 = binding.attributename4
            val attributevalue4 = binding.attributevalue4
            val attributename5 = binding.attributename5
            val attributevalue5 = binding.attributevalue5
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


            this@GameActivity.runOnUiThread(java.lang.Runnable {
                val options: RequestOptions = RequestOptions()
                    .skipMemoryCache(true)
                    .centerInside()
                Glide.with(this)
                    .load(card.icons)
                    .apply(options)
                    .into(cardimage)
            })
            if (uid == "uid1") {
                var cardvalue=
                    hashMapOf(
                        "uid1value1" to card.attributevalue1!!,
                        "uid1value2" to card.attributevalue2!!,
                        "uid1value3" to card.attributevalue3!!,
                        "uid1value4" to card.attributevalue4!!,
                        "uid1value5" to card.attributevalue5!!
                    )
                setFieldValue(cardvalue)

            } else {
                var cardvalue=
                    hashMapOf(
                        "uid2value1" to card.attributevalue1!!,
                        "uid2value2" to card.attributevalue2!!,
                        "uid2value3" to card.attributevalue3!!,
                        "uid2value4" to card.attributevalue4!!,
                        "uid2value5" to card.attributevalue5!!
                    )
                setFieldValue(cardvalue)
            }
            currentTurn=getField("turn")
            if(currentTurn!=uid){
                waitTurn(uid)
            }else{
                listenToButton()
            }
        }
    }




    private suspend fun waitTurn(uid: String)  {
        val greater = findViewById<MaterialButton>(R.id.greater)
        val lower = findViewById<MaterialButton>(R.id.lower)
        val turnText = findViewById<TextView>(R.id.turntext)
        turnText.text = "OPPONENT TURN"
        greater.visibility = MaterialButton.INVISIBLE
        lower.visibility = MaterialButton.INVISIBLE
        var myturn=false
        println("ci passo")
        d = db.collection("GameRoom").document(idRoom)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    previousTurn = currentTurn
                    currentTurn= snapshot.get("turn") as String
                    if (currentTurn == uid && snapshot.get("setwinner") == uidopponent) {
                        d?.remove()
                        job = launch {
                            reset()
                            charItem = deckcard.getArray()
                            decksize = deckcard.getDeckSize()
                            decksizeopponent = deckcard.getDeckSize()
                            createDialogSet(false)
                            listenToButton()
                        }
                    }else if (snapshot.get("matchwinner") == uidopponent){
                        createDialogGame(false)
                    }else if (snapshot.get("turnwinner") ==uidopponent && currentTurn==uidopponent){
                        if(decksize>=2) {
                            d?.remove()
                            job = launch {
                                charItem!!.removeAt(rnds)
                                println("1")
                                decksize--
                                createDialogTurn(false)
                                setPlayingCard()
                            }
                        }else if(decksize==1) {
                            d?.remove()
                            job = launch {
                                decksize--
                                reset()
                                println("2")
                                createDialogTurn(false)
                                setPlayingCard()
                            }
                        }

                    } else if (snapshot.get("turnwinner")==uidopponent && currentTurn==uid){
                        if(decksize>=2) {
                            d?.remove()
                            charItem!!.removeAt(rnds)
                            job = launch {
                                println("3")
                                decksize--
                                createDialogTurn(false)
                                setPlayingCard()
                                listenToButton()
                            }
                        }else if(decksize==1) {
                            d?.remove()
                            job = launch {
                                reset()
                                println("4")

                                createDialogTurn(false)
                                setPlayingCard()
                                listenToButton()
                            }
                        }

                    }else if (snapshot.get("matchwinner")==uid){
                        createDialogGame(true)
                    }else if(snapshot.get("setwinner")== uid && currentTurn ==uid){
                        d?.remove()
                        job = launch {
                            reset()
                            charItem = deckcard.getArray()
                            decksize = deckcard.getDeckSize()
                            decksizeopponent = deckcard.getDeckSize()
                            println("9")

                            createDialogSet(true)
                            listenToButton()
                        }
                    }


                } else {
                    Log.d(TAG, "Current data: null")
                }



            }
    }

    private suspend fun createDialogSet(win: Boolean)
    {
        this@GameActivity.runOnUiThread(java.lang.Runnable {
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
        })

    }



    private  suspend fun createDialogTurn(win: Boolean) {
        this@GameActivity.runOnUiThread(java.lang.Runnable {
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
        })

    }

    private  fun createDialogGame(win: Boolean) {
        this@GameActivity.runOnUiThread(java.lang.Runnable {
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
        })

    }







    private suspend fun setTurn(uid: String){
        return try{
            val data = firestore
                .collection("GameRoom")
                .document(idRoom)
                .update("turn", uid)
                .await()
            currentTurn=uid
        }catch (e: Exception){
            println(e)
        }
    }

    private suspend fun getField(field: String)
            : String{
        return try{
            val data = firestore
                .collection("GameRoom")
                .document(idRoom)
                .get()
                .await()
            return data.get(field) as String
        }catch (e: Exception){
            e.toString()
        }
    }

    private suspend fun getFieldConfr(field: String)
            : String{
        return try{
            val value1 = uid + valueSelected
            var value2 = ""
            value2 = if (uid == "uid1") {
                "uid2"+valueSelected
            } else {
                "uid1"+valueSelected
            }
            val data = firestore
                .collection("GameRoom")
                .document(idRoom)
                .get()
                .await()
            if(field=="1"){
                val value =(data.get(value1)as String)
                return value
            }else {
                val value =(data.get(value2)as String)
                return value
            }
        }catch (e: Exception){
            e.toString()
        }
    }


    private suspend fun setFieldValue(hashMap: HashMap<String, String>){
        return try{
            val sfDocRef = db.collection("GameRoom").document(deckcard.getGameRoom())
            val data = firestore.runTransaction {
                val snapshot = it.get(sfDocRef)
                turnplay = snapshot.getString("turn")!!
                it.update(sfDocRef, hashMap as Map<String, Any>)

            }.await()
        }catch (e: Exception){
            println(e)
        }
    }

    private fun View.onClickAsync(action: suspend () -> Unit) {
        setOnClickListener {
            launch {
                action()
            }
        }
    }
    private suspend fun listenToButton() {

        val greater = findViewById<MaterialButton>(R.id.greater)
        val lower = findViewById<MaterialButton>(R.id.lower)
        val turnText = findViewById<TextView>(R.id.turntext)
        runOnUiThread {
            turnText.text = "YOUR TURN"

            greater.visibility = MaterialButton.VISIBLE
            lower.visibility = MaterialButton.VISIBLE
        }



        var confrValue1 =getFieldConfr("1")
        var confrValue2 =getFieldConfr("2")

        greater.onClickAsync {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@onClickAsync
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            if(confrValue1>=confrValue2 && numberofturn<3){
                println(confrValue1 + ">= < "+confrValue2)
                turnWin()
            }else if(confrValue1>=confrValue2 && numberofturn==3){
                println(confrValue1+">=  == "+confrValue2)
                numberofturn=0
                turnWinSwitch()

            }else{
                println(confrValue1+" !> "+confrValue2)

                numberofturn=0
                turnLost()
            }
        }

        lower.onClickAsync {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return@onClickAsync
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            if(confrValue1<=confrValue2 && numberofturn<3){
                turnWin()
            }else if(confrValue1<=confrValue2 && numberofturn==3){
                numberofturn=0
                turnWinSwitch()
            }else{
                numberofturn=0
                turnLost()
            }
        }
    }



    private suspend fun turnWin(){
        decksizeopponent--
        if(decksizeopponent==0 && setwin<2 ){
            numberofturn=0
            setwin++
            var data=
                hashMapOf(
                    "setwinner" to uid,
                    "turn" to uidopponent
                )
            charItem=deckcard.getArray()
            decksize=deckcard.getDeckSize()
            decksizeopponent=deckcard.getDeckSize()
            setFieldValue(data)
            setPlayingCard()
            println("10")

            createDialogSet(true)
        }else if (decksizeopponent==0 && setwin==2) {
            var data=
                hashMapOf(
                    "matchwinner" to uid
                )
            setFieldValue(data)
            createDialogGame(true)
        }
        else{
            numberofturn++
            var data=
                hashMapOf(
                    "turnwinner" to uid
                )
            println("6")
            createDialogTurn(true)
            setFieldValue(data)
        }

    }

    private suspend fun turnWinSwitch(){
        decksizeopponent--
        if(decksizeopponent==0 && setwin<2 ){
            setwin++
            numberofturn=0
            var data=
                hashMapOf(
                    "setwinner" to uid,
                    "turn" to uidopponent
                )
            charItem=deckcard.getArray()
            decksize=deckcard.getDeckSize()
            decksizeopponent=deckcard.getDeckSize()
            setFieldValue(data)
            println("11")

            createDialogSet(true)
        }else if (decksizeopponent==0 && setwin==2) {
            setwin=0
            var data=
                hashMapOf(
                    "matchwinner" to uid
                )
            setFieldValue(data)
            createDialogGame(true)
        }
        else{
            numberofturn=0
            var data=
                hashMapOf(
                    "turnwinner" to uid,
                    "turn" to uidopponent
                )
            setFieldValue(data)
            setPlayingCard()
            println("7")

            createDialogTurn(true)
        }


    }


    private suspend fun turnLost(){
        numberofturn=0
        if(decksize>=1 && setlost<2){
            var data=
                hashMapOf(
                    "turnwinner" to uidopponent,
                    "turn" to uidopponent
                )
            setFieldValue(data)
        }else if (setlost==2){
            var data=
                hashMapOf(
                    "matchwinner" to uidopponent,
                )
            setFieldValue(data)
        }else{
            var data=
                hashMapOf(
                    "setwinner" to uidopponent,
                    "turn" to uidopponent
                )
            setFieldValue(data)

        }

    }



    private suspend fun reset(){
        var data=
            hashMapOf(
                "turnwinner" to "",
                "setwinner"  to ""
            )
        println("8")
        setFieldValue(data)
    }
}





