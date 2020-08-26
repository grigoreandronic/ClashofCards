package com.unitn.clashofcards

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.unitn.clashofcards.feature.onboarding.OnBoardingActivity
import com.unitn.clashofcards.model.User
import java.util.ArrayList


class LoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var callbackManager: CallbackManager
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login_button_login    = findViewById<Button>(R.id.login_button_login)
        val email     = findViewById<TextView>(R.id.edit_login_email)
        val password = findViewById<TextView>(R.id.edit_login_password)
        val google_button = findViewById<ImageView>(R.id.google_button_login)
        val facebook_button = findViewById<ImageView>(R.id.facebook_button_login)
        firebaseAuth = FirebaseAuth.getInstance()


        configureGoogleSignIn()
        google_button.setOnClickListener {
            signIn()
        }
        facebook_button.setOnClickListener {
            signInFacebook()
        }





        login_button_login.setOnClickListener {
            performLogin(email.text.toString(), password.text.toString())
        }



         findViewById<TextView>(R.id.login_sign_in).setOnClickListener {
             val intent = Intent(this, RegistrationActivity::class.java)
             startActivity(intent)

         }

    }

    private fun signInFacebook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance()
            .logInWithReadPermissions(this, listOf("email"))
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {

                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    Log.d(RegistrationActivity.TAG, "facebook:onCancel")

                }

                override fun onError(error: FacebookException) {
                    Log.d(RegistrationActivity.TAG, "facebook:onError", error)
                }
            })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d(RegistrationActivity.TAG, "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        val email = task.result?.user?.email.toString()
                        val isNew: Boolean = task.result!!.additionalUserInfo!!.isNewUser
                        if(isNew){
                            saveSocialUserToFirebaseDatabase(email)
                        }else{
                            val intent = Intent(this, OnBoardingActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                } else {
                    Toast.makeText(this, "Facebook sign in failed:(", Toast.LENGTH_LONG).show()
                }

            }
    }





    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun configureGoogleSignIn() {
        val myScope =
            Scope("https://www.googleapis.com/auth/user.birthday.read")
        val myScope2 =
            Scope(Scopes.PLUS_ME)
        val myScope3 =
            Scope(Scopes.PROFILE) //get name and id

        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestScopes(myScope, myScope)
            .requestProfile()
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account : GoogleSignInAccount? =task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        } else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {

            if (it.isSuccessful) {
                val user = firebaseAuth.currentUser

                if (user != null) {
                    val email = user.email.toString()
                    val isNew: Boolean = it.result!!.additionalUserInfo!!.isNewUser
                    if(isNew){
                        saveSocialUserToFirebaseDatabase(email)
                    }else{
                        val intent = Intent(this, OnBoardingActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }
            } else {
                Toast.makeText(this, "Google sign in failed:(", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun saveSocialUserToFirebaseDatabase(email: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        var toRef: DocumentReference
        var toRef2: DocumentReference
        var toRef3: DocumentReference

        var decks: ArrayList<DocumentReference> = ArrayList<DocumentReference>()
        toRef=(db.collection("Decks").document("U15wO66e7lTXJ5W0jHDD"))
        toRef2=(db.collection("Decks").document("5pB8d5qgUpVkAGBi7nt0"))
        toRef3=(db.collection("Decks").document("mejeej8pDuQ70ePwTCH6"))

        decks.add(toRef)
        decks.add(toRef2)
        decks.add(toRef3)


        var user : User = User()
        user.uid=uid
        user.email=email
        user.Decks=decks

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


    private fun performLogin(email: String, password: String) {
        if (email.isEmpty()) {
            Toast.makeText(this, "Please fill out email/pw.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Please fill out password /pw.", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Login", "Successfully logged in: ${it.result?.user?.uid}")
                val intent = Intent(this, OnBoardingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }













    companion object {

        private const val RC_SIGN_IN = 123
    }

    }