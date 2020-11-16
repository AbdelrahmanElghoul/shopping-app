package com.example.shoppingapp_customer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppingapp.R
import com.example.shoppingapp.User
import com.example.shoppingapp.util.Firebase
import com.example.shoppingapp_customer.navigation.NavigationActivity
import com.example.shoppingapp_customer.register.RegisterActivity
import timber.log.Timber
import timber.log.Timber.e
import timber.log.Timber.tag


class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Timber.plant(Timber.DebugTree())

//        Firebase.logout(this)
        Handler(Looper.getMainLooper()).postDelayed({
            tag("UserID").e("${User.getId(this)}")
            if (User.getId(this) == null) {
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            } else {
//                Firebase.loadCart(this, intent = (Intent(this, NavigationActivity::class.java)))
                startActivity(Intent(this, NavigationActivity::class.java))
                finish()
            }

        }, 1500)

    }
}