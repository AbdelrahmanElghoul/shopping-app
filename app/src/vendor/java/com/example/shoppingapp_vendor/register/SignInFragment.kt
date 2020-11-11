package com.example.shoppingapp_vendor.register

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.Fade
import androidx.transition.Slide
import com.example.shoppingapp.R
import com.example.shoppingapp.util.*
import com.example.shoppingapp_vendor.MainVendorActivity
import com.example.shoppingapp_vendor.VendorActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.fragment_sign_in.*
import timber.log.Timber


class SignInFragment : Fragment(),OpenFragment,UpdateUI {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }
private lateinit var mGoogleSignInClient:GoogleSignInClient
private lateinit  var gso:GoogleSignInOptions

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.OAuthClient))
                .requestEmail()
                .build()

        mGoogleSignInClient= GoogleSignIn.getClient(context as Context, gso)

        btn_create_account_fsi.setOnClickListener {
            val frag=SignUpFragment()

            frag.apply{
                //Slide(Gravity.TOP)
                enterTransition =  Fade().setDuration(500)
                exitTransition = Fade().setDuration(300)
            }
            openFragment(context as RegisterActivity, frag, R.id.frame_register_ar)
        }
        btn_log_in_fsi.setOnClickListener {
            logInEmailPassword()
        }

    }


    private fun areAllViewsValid(): Boolean {
        resetViews()
        var isValid = true
        if (txt_sign_in_email_fsi.text.isNullOrEmpty()) {
            layout_sign_in_email_txt_fsi.error = getString(R.string.empty_field_error_msg)
            isValid = false
        } else if (!Util.isEmailValid(txt_sign_in_email_fsi.text.toString())) {
            layout_sign_in_email_txt_fsi.error = "invalid email"
            isValid = false

        }
        if (txt_sign_in_password_fsi.text.isNullOrEmpty()) {
            isValid = false
            layout_sign_in_password_txt_fsi.error = getString(R.string.empty_field_error_msg)
        } else if (txt_sign_in_password_fsi.text!!.length < 6) {
            isValid = false
            layout_sign_in_password_txt_fsi.error = "password mst be more than 6 digits"
        }
        return isValid
    }
    private fun resetViews() {
        layout_sign_in_email_txt_fsi.isErrorEnabled=false
        layout_sign_in_password_txt_fsi.isErrorEnabled=false
    }

    private fun logInEmailPassword() {
        txt_error_fsi.visibility=View.GONE
        if (!(areAllViewsValid())) return
        progress_bar_fsi.visibility=View.VISIBLE


        Firebase.login(
                fragment=this,
                formattedEmail= txt_sign_in_email_fsi.text.toString(),
                password=txt_sign_in_password_fsi.text.toString(),
                type=Firebase.Users.VENDOR.Key,
                intent=Intent(context, MainVendorActivity::class.java))
    }
    private fun logInGoogle() {

        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RequestCode.GOOGLE_SIGN_IN.getValue)
    }

    override fun update(text: String?) {
        progress_bar_fsi.visibility = View.GONE
        txt_error_fsi.text = text
        txt_error_fsi.visibility = View.VISIBLE
    }



}