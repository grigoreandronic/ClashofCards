package com.unitn.clashofcards

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myIntent = Intent(this, LoginActivity::class.java)
        startActivity(myIntent)
    }



  /*  fun generateFBKey(){
        try {
            val info = packageManager.getPackageInfo(
                "com.unitn.clashofcards",
               PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
               val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    } */
}