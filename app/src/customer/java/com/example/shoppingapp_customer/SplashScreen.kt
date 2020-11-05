package com.example.shoppingapp_customer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppingapp.R
import com.example.shoppingapp.util.Firebase
import com.example.shoppingapp_customer.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber
import timber.log.Timber.tag


class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Timber.plant(Timber.DebugTree())

//        Firebase.logout(this)
         val mAuth= FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = if (Firebase.getUid(this)==null)
                Intent(this, RegisterActivity::class.java)
            else
                (Intent(this, MainShopScreenActivity::class.java))
            startActivity(intent)
            finish()
        }, 1500)
    }
}