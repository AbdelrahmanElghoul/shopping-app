package com.example.shoppingapp_vendor.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shoppingapp.R
import com.example.shoppingapp.util.OpenFragment
import com.example.shoppingapp_vendor.sign_up.SignUpFragment

class RegisterActivity : AppCompatActivity(),OpenFragment {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
//        Toast.makeText(this,"new run",Toast.LENGTH_SHORT).show()

        openFragment(this, SignInFragment(),R.id.frame_register_ar)

    }


}