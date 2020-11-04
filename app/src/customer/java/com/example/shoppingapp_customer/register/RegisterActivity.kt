package com.example.shoppingapp_customer.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shoppingapp.R
import com.example.shoppingapp.util.OpenFragment
import com.example.shoppingapp_customer.register.SignInFragment

class RegisterActivity : AppCompatActivity(),OpenFragment {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
//        Toast.makeText(this,"new run",Toast.LENGTH_SHORT).show()

        openFragment(this, SignInFragment(),R.id.frame_register_ar)

    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if(count==1)
            finish()
        else
            super.onBackPressed()
    }

}