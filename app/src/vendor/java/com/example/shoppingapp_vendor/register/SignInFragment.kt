package com.example.shoppingapp_vendor.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shoppingapp.R
import com.example.shoppingapp.util.OpenFragment
import com.example.shoppingapp_vendor.sign_up.SignUpFragment
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment(),OpenFragment {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_create_account_fsi.setOnClickListener{openFragment(context as RegisterActivity, SignUpFragment(),R.id.frame_register_ar)}
    }

}