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
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

class RegistrationActivity : AppCompatActivity() {



    private val c = Calendar.getInstance()
    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH)
    private val day = c.get(Calendar.DAY_OF_MONTH)

    companion object {
        val TAG = "RegistrationActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

         val birth_date     = findViewById<TextView>(R.id.birth_date_edit)
         val register_name     = findViewById<TextView>(R.id.register_name)
         val register_surname     = findViewById<TextView>(R.id.register_surname)
         val register_email     = findViewById<TextView>(R.id.register_email)
         val register_username     = findViewById<TextView>(R.id.register_username)
         val register_password     = findViewById<TextView>(R.id.register_password)
         val registration_button     = findViewById<Button>(R.id.register_button_register)
        //date picker
        birth_date.setOnClickListener {

            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                birth_date.setText("" + dayOfMonth + "/" + month + "/" + year)
            }, year, month, day)
            dpd.show()

        }


        registration_button.setOnClickListener {
            performRegister()
        }




        findViewById<TextView>(R.id.register_sign_in).setOnClickListener {
            finish()
        }
    }



    private fun performRegister() {
        val birthD  = birth_date_edit.text.toString()
        val name     = register_name.text.toString()
        val surname     = register_surname.text.toString()
        val email     = register_email.text.toString()
        val username     = register_username.text.toString()
        val password     = register_password.text.toString()

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
        if (birthD.isEmpty()) {
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

                saveUserToFirebaseDatabase(username,name,surname,email,password,birthD)
            }
            .addOnFailureListener{
                Log.d(TAG, "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun saveUserToFirebaseDatabase(username: String,name: String,surname: String,email: String,password: String,birthDate: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, username,name,surname, email,password, birthDate)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "Finally we saved the user to Firebase Database")

                val intent = Intent(this, MenuActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to set value to database: ${it.message}")
            }
    }

}



class User(val uid: String, val username: String, val name: String , val surname: String , val email: String, val password: String, val birthDate : String)

