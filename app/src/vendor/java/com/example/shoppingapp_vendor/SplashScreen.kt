package com.example.shoppingapp_vendor


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppingapp.Item
import com.example.shoppingapp.R
import com.example.shoppingapp.User
import com.example.shoppingapp.util.Firebase
import com.example.shoppingapp_vendor.register.RegisterActivity


import timber.log.Timber
import timber.log.Timber.e


class SplashScreen : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
//        var result= org.junit.runner.JUnitCore.runClasses(Item::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Timber.plant(Timber.DebugTree())



        Handler(Looper.getMainLooper()).postDelayed({

            e(User.getId(this))
            val intent = if (User.getId(this)==null)
                Intent(this, RegisterActivity::class.java)
            else
                (Intent(this, MainVendorActivity::class.java))
            startActivity(intent)
            finish()
        }, 1500)
    }
}