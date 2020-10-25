package com.example.shoppingapp_vendor.register

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shoppingapp.R
import com.example.shoppingapp.User
import com.example.shoppingapp.util.*
import com.example.shoppingapp_vendor.MainVendorActivity
import com.example.shoppingapp_vendor.VendorActivity
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import timber.log.Timber


class SignUpFragment : Fragment(),OpenFragment,UpdateUI {
    private var imgUri: Uri?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_sign_up_fsu.setOnClickListener{btnsign_upOnClick()}
        img_avatar_fsu.setOnClickListener { getImageFromGallery() }
    }

    private fun getImageFromGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, RequestCode.GET_IMAGE_RESULT.getValue)
    }
    private fun  btnsign_upOnClick(){
        txt_error_fsu.visibility=View.GONE
        if(!areAllViewsValid()) return
        progress_bar_fsu.visibility=View.VISIBLE
        val userMap=HashMap<String,String>()
        userMap[Firebase.Users.USER_NAME.Key]=txt_sign_up_name_fsu.text.toString()
        userMap[Firebase.Users.USER_EMAIL.Key]=txt_sign_up_email_fsu.text.toString()
        userMap[Firebase.Users.USER_PHONE.Key]=txt_sign_up_phone_fsu.text.toString()
        if(imgUri!=null) userMap[Firebase.Users.USER_ICON.Key]=imgUri.toString()

        Firebase.auth(this,
                userMap, password = txt_sign_up_password_fsu.text.toString(), Firebase.Users.VENDOR.Key, intent = Intent(context, MainVendorActivity::class.java))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.d("$requestCode / $resultCode ${data?.data.toString()}")
        if (requestCode == RequestCode.GET_IMAGE_RESULT.getValue && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            imgUri = data.data
            Glide.with(this)
                    .load(data.data)
                    .apply(RequestOptions.circleCropTransform())
                    .error(R.drawable.error)
                    .into(img_avatar_fsu)
        }

    }
    private fun areAllViewsValid():Boolean{
        var isValid=true
        if(txt_sign_up_name_fsu.text.isNullOrEmpty()){
            isValid=false
            txt_sign_up_name_fsu.error=getString(R.string.empty_field_error_msg)
        }
        if(txt_sign_up_password_fsu.text.isNullOrEmpty()){
            isValid=false
            txt_sign_up_password_fsu.error=getString(R.string.empty_field_error_msg)
        }else if(txt_sign_up_password_fsu.text!!.length < 6){
            isValid=false
            txt_sign_up_password_fsu.error="password mst be more than 6 digits"
        }

        if(txt_sign_up_email_fsu.text.isNullOrEmpty()){
            isValid=false
            txt_sign_up_email_fsu.error=getString(R.string.empty_field_error_msg)
        } else if(!Util.isEmailValid(txt_sign_up_email_fsu.text.toString())) {
            txt_sign_up_email_fsu.error = "invalid email"
            isValid = false

        }
        if(txt_sign_up_phone_fsu.text.isNullOrEmpty()){
            isValid=false
            txt_sign_up_phone_fsu.error=getString(R.string.empty_field_error_msg)
        }
        return isValid
    }

    override fun update(text: String?) {
        progress_bar_fsu.visibility=View.GONE
        txt_error_fsu.text=text
        txt_error_fsu.visibility=View.VISIBLE
    }


}
