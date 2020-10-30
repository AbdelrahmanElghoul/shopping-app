package com.example.shoppingapp_vendor

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.shoppingapp.R
import com.example.shoppingapp.util.Firebase
import com.example.shoppingapp_vendor.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber
import timber.log.Timber.d


class SplashScreen : AppCompatActivity() {
    private var mAuth: FirebaseAuth?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Timber.plant(Timber.DebugTree())

        mAuth = FirebaseAuth.getInstance()

        Handler().postDelayed({

            val intent = if (mAuth!!.currentUser?.uid == null)
                Intent(this, RegisterActivity::class.java)
            else
                (Intent(this, MainVendorActivity::class.java))
            Timber.d("${mAuth!!.currentUser?.uid == null}")
            startActivity(intent)
            finish()
        }, 1000)
    }
}