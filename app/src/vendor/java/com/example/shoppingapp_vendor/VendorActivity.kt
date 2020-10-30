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
import com.example.shoppingapp.util.UpdateUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.vendor.activity_vendor.*
import timber.log.Timber
import timber.log.Timber.DebugTree
import timber.log.Timber.plant
import java.lang.Exception

class VendorActivity : AppCompatActivity(),UpdateUI {
    private lateinit var adapter: ArrayAdapter<String>
    private var itemUri:Uri?=null
    private lateinit var categoryList:Pair<Array<String>,TypedArray>
    private lateinit var database:FirebaseDatabase
    private lateinit var type:String
    private lateinit var key:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor)
        initialiseData()



        spinner_layout_va.setOnClickListener {spinner_category_va.callOnClick()}
        btn_add_item.setOnClickListener { btnAdd() }
        img_item_icon_va.setOnClickListener {
          getImageFromGallery()
        }
        img2_item_icon_va.setOnClickListener {
          getImageFromGallery()
        }

        spinner_category_va?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Glide.with(this@VendorActivity)
                        .load(categoryList.second.getResourceId(position,R.drawable.error))
                        .apply(RequestOptions.circleCropTransform())
                        .into(img_selected_category_va)

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initialiseData() {
         database = FirebaseDatabase.getInstance()
         type=Firebase.Items.ITEMS.Key
         key=database.reference.push().key.toString()

        plant(DebugTree())
        categoryList= Pair(resources.getStringArray(R.array.category_name),  resources.obtainTypedArray(R.array.category_icon))
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoryList.first)
        spinner_category_va.adapter = adapter

    }
    private fun validateViews(): Boolean {
        var isValid = true
        if (txt_item_description_va.text.isNullOrEmpty()) {
            layout_item_description_txt_va.error = getString(R.string.empty_field_error_msg)
            isValid = false
        }
        if (txt_item_name_va.text.isNullOrEmpty()) {
            layout_item_name_txt_va.error = getString(R.string.empty_field_error_msg)
            isValid = false
        }
        if (txt_item_price_va.text.isNullOrEmpty()) {
            layout_item_price_txt_va.error = getString(R.string.empty_field_error_msg)
            isValid = false
        }
        if (txt_item_stock_va.text.isNullOrEmpty() || txt_item_stock_va.text!!.equals("0")) {
            layout_item_stock_txt_va.error = getString(R.string.empty_field_error_msg)
            isValid = false
        }

        return isValid
    }
    private fun btnAdd() {
        txt_error_va.visibility=View.GONE
        if (!validateViews()) return
        progress_bar_va.visibility=View.VISIBLE
        val itemMap = HashMap<String, String>()
        itemMap[Firebase.Items.ITEM_NAME.Key] = txt_item_name_va.text.toString()
        itemMap[Firebase.Items.ITEM_PRICE.Key] = txt_item_price_va.text.toString()
//        if (itemUri != null) itemMap[Firebase.Items.ITEM_IMG_URL.Key] = itemUri.toString()
        itemMap[Firebase.Items.ITEM_STOCK.Key] = txt_item_stock_va.text.toString()
        itemMap[Firebase.Items.ITEMS_CATEGORY.Key] = spinner_category_va.selectedItemPosition.toString()
        itemMap[Firebase.Items.ITEM_DESCRIPTION.Key] = txt_item_description_va.text.toString()
        itemMap[Firebase.Items.ITEM_MANUFACTURE.Key] = txt_manufacture_va.text.toString()

        database.getReference(type)
                .child(key)
                .setValue(itemMap)
                .addOnSuccessListener {
                    //item added
                    if (itemUri != null) {
                        //adding img to storage
                        val mStorageRef = FirebaseStorage.getInstance().reference
                        val ref: StorageReference = mStorageRef.child("${type}/${key}.jpg")

                        ref.putFile(itemUri!!).continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    throw it
                                }
                            }
                            ref.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                //adding img url to database
                                FirebaseDatabase.getInstance()
                                        .getReference(type)
                                        .child(key)
                                        .child(Firebase.Items.ITEM_IMG_URL.Key)
                                        .setValue(task.result.toString()).addOnFailureListener { error(it) }.addOnSuccessListener {
                                            super.onBackPressed()
                                        }
                            } else error(task.exception!!)
                        }
                    }
                }
                .addOnFailureListener {error(it)}
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
                    .into(img_item_icon_va)
            Glide.with(this)
                    .load(itemUri)
                    .error(R.drawable.default_img)
                    .into(img2_item_icon_va)
        }
    }

    override fun update(text: String?) {
        progress_bar_va.visibility=View.GONE
        txt_error_va.text = text
        txt_error_va.visibility = View.VISIBLE
    }

    private fun error(it: Exception){
        Timber.e(it)
        update(it.toString())
        Toast.makeText(this, "error occurred\n$it.message", Toast.LENGTH_SHORT).show()
    }
}

