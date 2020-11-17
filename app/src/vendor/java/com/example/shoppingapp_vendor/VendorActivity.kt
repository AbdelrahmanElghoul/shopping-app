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
import com.example.shoppingapp.Item
import com.example.shoppingapp.R
import com.example.shoppingapp.User
import com.example.shoppingapp.util.Firebase
import com.example.shoppingapp.util.RequestCode
import com.example.shoppingapp.util.UpdateUI

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import kotlinx.android.synthetic.vendor.activity_vendor.*
import timber.log.Timber
import timber.log.Timber.*
import java.lang.Exception

class VendorActivity : AppCompatActivity(),UpdateUI {
    private lateinit var adapter: ArrayAdapter<String>
    private var itemUri:Uri?=null
    private lateinit var categoryList:Pair<Array<String>,TypedArray>
    private lateinit var database:FirebaseDatabase
    private lateinit var type:String
    private lateinit var key:String
    var item:Item?=null
    val tag="fb_va"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor)
        item=intent.getParcelableExtra(getString(R.string.class_id))

        initialiseData()

        btn_add_item.setOnClickListener { btnAdd() }
        spinner_layout_va.setOnClickListener {spinner_category_va.callOnClick()}
        img2_item_icon_va.setOnClickListener {
          getImageFromGallery()
        }
        img_back_va.setOnClickListener {
            onBackPressed()
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

        if(item!=null) bindItem()

    }
    private fun bindItem() {
        key=item?.id!!
        txt_item_name_va.setText(item?.name)
        txt_item_price_va.setText(item?.price)
        txt_item_stock_va.setText(item?.stock)
        spinner_category_va.setSelection(item?.categoryId?.toInt()!!)
        txt_item_description_va.setText(item?.description)
        txt_manufacture_va.setText(item?.manufacture)

        Glide.with(this@VendorActivity)
                .load(item?.icon)
                .error(R.drawable.default_img)
                .into(img2_item_icon_va)

        btn_add_item.text="update"
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

        itemMap[Firebase.Items.ITEM_NAME.Key] = txt_item_name_va.text?.trim().toString()
        itemMap[Firebase.Items.ITEM_PRICE.Key] = txt_item_price_va.text?.trim().toString()

        itemMap[Firebase.Items.ITEM_STOCK.Key] = txt_item_stock_va.text?.trim().toString()
        itemMap[Firebase.Items.ITEMS_CATEGORY.Key] = spinner_category_va.selectedItemPosition.toString()
        itemMap[Firebase.Items.ITEM_DESCRIPTION.Key] = txt_item_description_va.text?.trim().toString()
        itemMap[Firebase.Items.ITEM_MANUFACTURE.Key] = txt_manufacture_va.text?.trim().toString()
        itemMap[Firebase.Items.ITEM_VENDOR_ID.Key] = User.getId(this@VendorActivity).toString()

        if (item?.icon  != null) itemMap[Firebase.Items.ITEM_IMG_URL.Key] = item?.icon!!
        database.getReference(type)
                .child(key)
                .setValue(itemMap)
                .addOnSuccessListener {
                    //item added
                    if (itemUri != null){
                        //adding img to storage
                        val mStorageRef = FirebaseStorage.getInstance().reference
                        val ref: StorageReference = mStorageRef.child("${type}/${key}.jpg")
                        ref.putFile(itemUri!!).continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let {
                                    tag("$tag throw").e(it)
                                    throw it
                                }
                            }
                            ref.downloadUrl
                        }
                                .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                tag("$tag img uploaded").e("successful")
                                //adding img url to database
                                FirebaseDatabase.getInstance()
                                        .getReference(type)
                                        .child(key)
                                        .child(Firebase.Items.ITEM_IMG_URL.Key)
                                        .setValue(task.result.toString())
                                        .addOnSuccessListener {
                                            tag("$tag img url").e("task.result.toString()")
                                            onBackPressed()
                                        }
                            }
                            else error("task.exception")
                        }
                    }
                    else {
                        e("else")
                        onBackPressed()
                    }
                }
                .addOnFailureListener {error(it)}
    }
    private fun getImageFromGallery() {
        tag("activity").e("${callingActivity}")
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, RequestCode.GET_IMAGE_RESULT.getValue)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        tag("activity").e("${callingActivity}")
        if (requestCode == RequestCode.GET_IMAGE_RESULT.getValue && resultCode == RESULT_OK && data != null) {
            itemUri = data.data
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
        e(it)
        update(it.toString())
        Toast.makeText(this, "error occurred\n$it.message", Toast.LENGTH_SHORT).show()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,MainVendorActivity::class.java))
        finish()
    }
}

