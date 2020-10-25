package com.example.shoppingapp_vendor

import android.content.Intent
import android.content.res.TypedArray
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shoppingapp.R
import com.example.shoppingapp.util.Firebase
import com.example.shoppingapp.util.RequestCode
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.vendor.activity_vendor.*
import timber.log.Timber.DebugTree
import timber.log.Timber.plant

class VendorActivity : AppCompatActivity() {
    private lateinit var adapter: ArrayAdapter<String>
    private var itemUri:Uri?=null
    private var selectedImg:Int=-1  // itemUri=0 / categeoryUri=1
    private lateinit var categoryList:Pair<Array<String>,TypedArray>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor)
        initialiseData()

//        val user=FirebaseAuth.getInstance().currentUser
//        Timber.tag("current user")
//                .d("name = ${user?.displayName}\n" +
//                        "email = ${user?.email}\n" +
//                        "photo = ${user?.photoUrl}\n" +
//                        "phone=${user?.phoneNumber}\nuser=$user")
//

        img_logout_av.setOnClickListener { Firebase.logout(this) }
        spinner_layout_av.setOnClickListener {spinner_category_av.callOnClick()}
        btn_add_item.setOnClickListener { btnAdd() }
        img_item_icon_av.setOnClickListener {
            selectedImg = 0
          getImageFromGallery()
        }

        spinner_category_av?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Glide.with(this@VendorActivity)
                        .load(categoryList.second.getResourceId(position,R.drawable.error))
                        .apply(RequestOptions.circleCropTransform())
                        .into(img_selected_category_av)

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initialiseData() {
        plant(DebugTree())
        categoryList= Pair(resources.getStringArray(R.array.category_name),  resources.obtainTypedArray(R.array.category_icon))
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoryList.first)
        spinner_category_av.adapter = adapter

    }
    private fun validateViews(): Boolean {
        var isValid = true
        if (txt_item_description_av.text.isNullOrEmpty()) {
            txt_item_description_av.error = getString(R.string.empty_field_error_msg)
            isValid = false
        }
        if (txt_item_price_av.text.isNullOrEmpty()) {
            txt_item_description_av.error = getString(R.string.empty_field_error_msg)
            isValid = false
        }
        if (txt_item_stock_av.text.isNullOrEmpty() || txt_item_stock_av.text!!.equals("0")) {
            txt_item_description_av.error = getString(R.string.empty_field_error_msg)
            isValid = false
        }

        return isValid
    }
    private fun btnAdd() {
        txt_error_av.visibility=View.GONE
        if (!validateViews()) return

        val itemMap = HashMap<String, String>()
        itemMap[Firebase.Items.ITEM_NAME.Key] = txt_item_name_av.text.toString()
        itemMap[Firebase.Items.ITEM_PRICE.Key] = txt_item_price_av.text.toString()
        if (itemUri != null) itemMap[Firebase.Items.ITEM_IMG_URL.Key] = itemUri.toString()
        itemMap[Firebase.Items.ITEM_STOCK.Key] = txt_item_stock_av.text.toString()
        itemMap[Firebase.Items.ITEMS_CATEGORY.Key] = spinner_category_av.selectedItemPosition.toString()
        itemMap[Firebase.Items.ITEM_DESCRIPTION.Key] = txt_item_description_av.text.toString()
        itemMap[Firebase.Items.ITEM_MANUFACTURE.Key] = txt_manufacture_av.text.toString()

        val database = FirebaseDatabase.getInstance()
        val myRef = database
                .getReference(Firebase.Items.ITEMS.Key)
                .child(database.reference.push().key.toString())
                .setValue(itemMap)

        myRef.addOnSuccessListener  {
            super.onBackPressed()
        }
                .addOnFailureListener {
            Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
            txt_error_av.text=it.message
        }

    }
    private fun getImageFromGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, RequestCode.GET_IMAGE_RESULT.getValue)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.GET_IMAGE_RESULT.getValue && resultCode == RESULT_OK && data != null) {
            itemUri = data.data
            Glide.with(this)
                    .load(itemUri)
                    .apply(RequestOptions.circleCropTransform())
                    .error(R.drawable.default_img)
                    .into(img_item_icon_av)
        }
    }

}

