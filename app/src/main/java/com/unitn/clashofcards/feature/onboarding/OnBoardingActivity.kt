package com.unitn.clashofcards.feature.onboarding
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unitn.clashofcards.DeckActivity
import com.unitn.clashofcards.LoginActivity
import com.unitn.clashofcards.MarketDeckActivity
import com.unitn.clashofcards.R
import com.unitn.clashofcards.marketplace.CheckoutActivity
import com.unitn.clashofcards.matcher.DeckSelection
import com.unitn.clashofcards.model.GameRoom
import kotlinx.android.synthetic.main.activity_turnwin.*
import kotlinx.android.synthetic.main.menu_bar.*

class OnBoardingActivity : AppCompatActivity() {
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding_activity)

        val deck_button = findViewById<MaterialButton>(R.id.DeckBtn)
        val marketplace_button = findViewById<MaterialButton>(R.id.MarketplaceBtn)
        val play_btn = findViewById<MaterialButton>(R.id.PlayBtn)
        val profile_button = findViewById<Chip>(R.id.profile_button)
        deck_button.setOnClickListener {
            val intent = Intent(this, DeckActivity::class.java)
            startActivity(intent)
        }
        marketplace_button.setOnClickListener {
            val intent = Intent(this, MarketDeckActivity::class.java)
            startActivity(intent)
        }

        profile_button.setOnClickListener {
            var dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.fragment_profile)

            dialog.show()

        }

        play_btn.setOnClickListener {
            val intent = Intent(this, DeckSelection::class.java)
            startActivity(intent)
            /* var dialog = Dialog(this, R.style.AppTheme)
             dialog.setCancelable(false)
             dialog.setContentView(R.layout.activity_main)
             //dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
             dialog.show()
             Handler(Looper.getMainLooper()).postDelayed({
                 //finish this activity
                // finish()
                 dialog.dismiss()
             },5000)  */
        }

    }


}
