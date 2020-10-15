package com.example.shoppingapp_dealer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.shoppingapp.R

class DealerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dealer)

        Toast.makeText(this@DealerActivity,"done",Toast.LENGTH_LONG).show()
    }
}