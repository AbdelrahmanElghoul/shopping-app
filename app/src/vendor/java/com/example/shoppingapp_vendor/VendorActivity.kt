package com.example.shoppingapp_vendor


import android.content.Intent
import android.content.res.TypedArray
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shoppingapp.Item
import com.example.shoppingapp.R
import com.example.shoppingapp.util.RequestCode
import com.example.shoppingapp_vendor.util.VFirebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.vendor.activity_vendor.*
import kotlinx.android.synthetic.vendor.alert_dialog_layout.view.*
import timber.log.Timber.*



lateinit var adapter: ArrayAdapter<String>
var itemUri:Uri?=null
var selectedImg:Int=-1  // itemUri=0 / categeoryUri=1

lateinit var categoryList:Pair<Array<String>,TypedArray>

class VendorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor)
        initialiseData()

        spinner_layout_ad.setOnClickListener {spinner_category_ad.callOnClick()}
        btn_add_item.setOnClickListener { btnAdd() }
        img_item_icon_ad.setOnClickListener {
            selectedImg = 0
            getImageFromGallery()
        }
        spinner_category_ad?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Glide.with(this@VendorActivity)
                        .load(categoryList.second.getResourceId(position,R.drawable.error))
                        .apply(RequestOptions.circleCropTransform())
                        .into(img_selected_category_ad)

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initialiseData() {
        plant(DebugTree())
        categoryList= Pair(resources.getStringArray(R.array.category_name),  resources.obtainTypedArray(R.array.category_icon))
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoryList.first)
        spinner_category_ad.adapter = adapter

    }
    private fun validateViews(): Boolean {
        var isValid = true
        if (txt_item_description_ad.text.isNullOrEmpty()) {
            txt_item_description_ad.error = "empty field"
            isValid = false
        }
        if (txt_item_price_ad.text.isNullOrEmpty()) {
            txt_item_description_ad.error = "empty field"
            isValid = false
        }
        if (txt_item_stock_ad.text.isNullOrEmpty() || txt_item_stock_ad.text!!.equals("0")) {
            txt_item_description_ad.error = "empty field"
            isValid = false
        }

        return isValid
    }
    private fun btnAdd() {
        if (!validateViews()) return
        val item = Item()
        item.name = txt_item_name_ad.text.toString()
        item.price = txt_item_price_ad.text.toString()
        item.stock = txt_item_stock_ad.text.toString()
        item.categoryId = spinner_category_ad.selectedItemPosition.toString()
        item.description = txt_item_description_ad.text.toString()
        item.manufactureDetails = txt_manufacture_ad.text.toString()

        VFirebase.addItemToFirebase(item)
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
                    .into(img_item_icon_ad)
        }
    }
    fun dialogEditText() {

        val factory = LayoutInflater.from(this)
        val alertDialog = AlertDialog.Builder(this)
        val layoutView: View = factory.inflate(R.layout.alert_dialog_layout, null)
        val btnAdd = layoutView.btn_alert_dialog_add
        val txtCategory = layoutView.alert_dialog_edittext
        val btnCancel = layoutView.btn_alert_dialog_cancel
        val imgAlertDialogCategory = layoutView.alert_dialog_img

        alertDialog.setView(layoutView)
                .setCancelable(false)
        val dialog = alertDialog.create()

        imgAlertDialogCategory.setOnClickListener {
            selectedImg = 1
            getImageFromGallery()
        }

        btnAdd.setOnClickListener {
            if (txtCategory.text.isNullOrEmpty()) {
                txtCategory.error = "enter category name"
                return@setOnClickListener
            }
            dialog.dismiss()
        }
        btnCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}

