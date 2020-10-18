package com.example.shoppingapp_customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.shoppingapp.MainShopScreenActivity
import com.example.shoppingapp.R


class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Timber.plant(DebugTree())
//

//        Log.e("SDK123",String.valueOf(android.os.Build.VERSION.SDK_INT))
        Handler().postDelayed({

//            val intent = if(packageName=="com.example.shoppingapp_dealer")
//                Intent(this, DealerActivity::class.java)
//            else
            val intent = Intent(this, MainShopScreenActivity::class.java)

            startActivity(intent)
            finish()
        }, 1000)

// Float.POSITIVE_INFINITY.toLong()
//        var dotCount:Int=0
//        var timer = object: CountDownTimer(10000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                when {
//                    dotCount % 4== 0 -> loading_txt.text = "loading..."
//                    dotCount % 3== 0 -> loading_txt.text = "loading.."
//                    dotCount % 2== 0 -> loading_txt.text = "loading."
//                    else -> loading_txt.text = "loading"
//                }
//                dotCount++
//            }
//            override fun onFinish() {
//                var intent = Intent(this@SplashScreen, MainShopScreenActivity::class.java)
//                startActivity(intent)
//            }
//        }
//        timer.start()
    }
}