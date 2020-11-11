package com.example.shoppingapp_customer.register

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
import com.example.shoppingapp_customer.navigation.NavigationActivity
import kotlinx.android.synthetic.main.fragment_sign_up.*
import timber.log.Timber


class SignUpFragment : Fragment(),OpenFragment,UpdateUI {
    private var imgUri: Uri?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_sign_up_fsu.setOnClickListener{signUp()}
        img_avatar_fsu.setOnClickListener { getImageFromGallery() }
        btn_have_account_fsu.setOnClickListener{ activity?.onBackPressed()}
    }

    private fun getImageFromGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, RequestCode.GET_IMAGE_RESULT.getValue)
    }
    private fun  signUp() {
        txt_error_fsu.visibility = View.GONE
        if (!areAllViewsValid()) return
        progress_bar_fsu.visibility = View.VISIBLE

        val userMap=HashMap<String,String>()
        userMap[Firebase.Users.USER_NAME.Key]= txt_sign_up_name_fsu.text.toString()
        userMap[Firebase.Users.USER_EMAIL.Key]= txt_sign_up_email_fsu.text.toString()
        userMap[Firebase.Users.USER_PHONE.Key]= txt_sign_up_phone_fsu.text.toString()
        userMap[Firebase.Users.USER_PASSWORD.Key] = txt_sign_up_password_fsu.text.toString()

       val intent = Intent(context, NavigationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_HISTORY
        Firebase.register(
                fragment = this,
                userMap = userMap,
                type = Firebase.Users.CUSTOMER.Key,
                intent = intent,
                uri = imgUri
                )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.d("$requestCode / $resultCode ${data?.data.toString()}")
        if (requestCode == RequestCode.GET_IMAGE_RESULT.getValue && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            imgUri = data.data
            Timber.tag("imgUri=${imgUri}")
            Glide.with(this)
                    .load(data.data)
                    .apply(RequestOptions.circleCropTransform())
                    .error(R.drawable.error)
                    .into(img_avatar_fsu)

//            Firebase.addImageToStorage(context=context as Context,parentKey = "testing",
//                    iconKey = "testing",uri=imgUri as Uri,type = "testing")
        }

    }
    private fun areAllViewsValid():Boolean{
        resetViews()
        var isValid=true
        if(txt_sign_up_name_fsu.text.isNullOrEmpty()){
            isValid=false
            layout_sign_up_name_txt_fsu.error=getString(R.string.empty_field_error_msg)
        }
        if(txt_sign_up_password_fsu.text.isNullOrEmpty()){
            isValid=false
            layout_sign_up_password_txt_fsu.error=getString(R.string.empty_field_error_msg)
        }else if(txt_sign_up_password_fsu.text!!.length < 6){
            isValid=false
            layout_sign_up_password_txt_fsu.error="password mst be more than 6 digits"
        }

        if(txt_sign_up_email_fsu.text.isNullOrEmpty()){
            isValid=false
            layout_sign_up_email_txt_fsu.error=getString(R.string.empty_field_error_msg)
        } else if(!Util.isEmailValid(txt_sign_up_email_fsu.text.toString())) {
            layout_sign_up_email_txt_fsu.error = "invalid email"
            isValid = false

        }
        return isValid
    }
    private fun resetViews(){
            layout_sign_up_name_txt_fsu.isErrorEnabled=false
            layout_sign_up_password_txt_fsu.isErrorEnabled=false
            layout_sign_up_email_txt_fsu.isErrorEnabled=false

    }

    override fun update(text: String?) {
        progress_bar_fsu.visibility=View.GONE
        txt_error_fsu.text=text
        txt_error_fsu.visibility=View.VISIBLE
    }


}
