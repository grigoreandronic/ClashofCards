package com.unitn.clashofcards

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.local.ReferenceSet
import com.google.firebase.ktx.Firebase
import com.unitn.clashofcards.feature.onboarding.OnBoardingActivity
import com.unitn.clashofcards.model.User
import java.util.*


class RegistrationActivity : AppCompatActivity() {

    val db = Firebase.firestore
    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

         val registration_birth_date     = findViewById<TextView>(R.id.birth_date_edit)
         val registration_name     = findViewById<TextView>(R.id.register_name)
         val registration_surname     = findViewById<TextView>(R.id.register_surname)
         val registration_email     = findViewById<TextView>(R.id.register_email)
         val registration_username     = findViewById<TextView>(R.id.register_username)
         val registration_password     = findViewById<TextView>(R.id.register_password)
         val registration_button     = findViewById<Button>(R.id.register_button_register)
        //date picker

        registration_birth_date.setOnClickListener {

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    // Display Selected date in TextView
                    registration_birth_date.setText("" + dayOfMonth + "/" + month + "/" + year)
                },
                year,
                month,
                day
            )
            dpd.show()

        }







        registration_button.setOnClickListener {
            performRegister(
                registration_name.text.toString(),
                registration_surname.text.toString(),
                registration_email.text.toString(),
                registration_username.text.toString(),
                registration_password.text.toString(),
                registration_birth_date.text.toString()
            )
        }

        findViewById<TextView>(R.id.signintologin).setOnClickListener {
            finish()
        }





    }


    private fun performRegister(
        name: String,
        surname: String,
        email: String,
        username: String,
        password: String,
        birthDate: String
    ) {


        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter a name /pw", Toast.LENGTH_SHORT).show()
            return
        }
        if (surname.isEmpty()) {
            Toast.makeText(this, "Please enter a surname/pw", Toast.LENGTH_SHORT).show()
            return
        }
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
            return
        }
        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter a username/pw", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter a password/pw", Toast.LENGTH_SHORT).show()
            return
        }
        if (birthDate.isEmpty()) {
            Toast.makeText(this, "Please enter a birth date /pw", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Attempting to create user with email: $email")

        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.d(TAG, "Successfully created user with uid: ${it.result?.user?.uid}")

                saveUserToFirebaseDatabase(username, name, surname, email, password, birthDate)
            }
            .addOnFailureListener{
                Log.d(TAG, "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserToFirebaseDatabase(username: String, name: String, surname: String, email: String, password: String, birthDate: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        var primo = "com.google.firebase.firestore.DocumentReference@bc73cc9c"
        var secondo = "com.google.firebase.firestore.DocumentReference@ee03a3c3"
        var terzo = "com.google.firebase.firestore.DocumentReference@78d1298b"
        var toRef: DocumentReference
        var toRef2: DocumentReference
        var toRef3: DocumentReference

        var Decks: ArrayList<DocumentReference> = ArrayList<DocumentReference>()
        toRef=(db.collection("Decks").document("U15wO66e7lTXJ5W0jHDD"))
        toRef2=(db.collection("Decks").document("5pB8d5qgUpVkAGBi7nt0"))
        toRef3=(db.collection("Decks").document("mejeej8pDuQ70ePwTCH6"))

        Decks.add(toRef)
        Decks.add(toRef2)
        Decks.add(toRef3)


        val user = User(uid, username, name, surname, email, password, birthDate, Decks)
        db.collection("Users").document("$uid")
            .set(user)
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!")
                val intent = Intent(this, OnBoardingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener { e -> Log.w(
                "TAG",
                "Failed to set value to database: ${e.message}",
                e
            )
            }

    }

    companion object {
        val TAG = "RegistrationActivity"

    }

}



