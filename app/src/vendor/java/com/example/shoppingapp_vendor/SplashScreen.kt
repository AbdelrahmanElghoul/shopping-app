package com.example.shoppingapp_vendor


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppingapp.Item
import com.example.shoppingapp.R
import com.example.shoppingapp_vendor.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

import timber.log.Timber


class SplashScreen : AppCompatActivity() {

    private var mAuth: FirebaseAuth?= null

    override fun onCreate(savedInstanceState: Bundle?) {
//        var result= org.junit.runner.JUnitCore.runClasses(Item::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Timber.plant(Timber.DebugTree())

        mAuth = FirebaseAuth.getInstance()

        Handler(Looper.getMainLooper()).postDelayed({

            val intent = if (mAuth!!.currentUser?.uid == null)
                Intent(this, RegisterActivity::class.java)
            else
                (Intent(this, MainVendorActivity::class.java))
            Timber.d("${mAuth!!.currentUser?.uid == null}")
            startActivity(intent)
            finish()
        }, 1500)
    }
}