package com.example.shoppingapp_dealer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shoppingapp.Category
import com.example.shoppingapp.Item
import com.example.shoppingapp.R
import com.example.shoppingapp.util.RequestCode
import com.example.shoppingapp_dealer.util.Firebase
import kotlinx.android.synthetic.dealer.activity_dealer.*
import kotlinx.android.synthetic.dealer.alert_dialog_layout.view.*
import kotlinx.android.synthetic.main.category_layout.*
import kotlinx.android.synthetic.main.fragment_description_screen.*
import timber.log.Timber
import timber.log.Timber.DebugTree


var itemUri:Uri?=null
var categoryUri:Uri?=null
var selectedImg:Int=-1  // itemUri=0 / categeoryUri=1
var adapter:ArrayAdapter<Category>?=null
var categoryList= mutableListOf<Category>()

class DealerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dealer)
        Timber.plant(DebugTree())
        setUI()

        spinner_layout.setOnClickListener{
            Timber.tag("spinner").d("called")
            spinner_category.callOnClick()
        }
        btn_add_item.setOnClickListener { btnAdd() }
        img_item_icon.setOnClickListener {
            selectedImg=0
            getImageFromGallery()
        }
//        img_category.setOnClickListener {
//            selectedImg=1
//            getImageFromGallery()
//        }
        spinner_category?.onItemSelectedListener =object: AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(spinner_category.selectedItem.toString() == getString(R.string.add_category_txt))
                    dialogEditText()
                else
                    Glide.with(this@DealerActivity)
                        .load(categoryList.get(position).icon)
                        .apply(RequestOptions.circleCropTransform())
                        .error(R.drawable.default_img)
                        .into(category_img)

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setUI() {
        var category=Category()
        category.name=getString(R.string.add_category_txt)

        categoryList.add(category)
        adapter=ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, categoryList)
        spinner_category.adapter=adapter

    }

    private fun validateViews():Boolean {
        var isValid =true
        if (txt_item_name.text.isNullOrEmpty()) {
            txt_item_name.error = "empty field"
            isValid=false
        }
        if (txt_item_price.text.isNullOrEmpty()) {
            txt_item_price.error = "empty field"
            isValid= false
        }
        if (txt_item_stock.text.isNullOrEmpty() || txt_item_stock.text!!.equals("0")) {
            txt_item_stock.error = "empty field"
            isValid= false
        }
        if (spinner_category.selectedItem.toString() == getString(R.string.add_category_txt)) {
               Toast.makeText(this, "select category", Toast.LENGTH_LONG).show()
            isValid= false
        }

        return isValid
    }

    private fun btnAdd() {
        if (!validateViews()) return

        val category = categoryList[spinner_category.selectedItemPosition]
        if (category.id == null) {
            if (category.icon != null) category.icon = Firebase.addImageToStorage(category.icon!!)
            category.id = Firebase.addCategoryToFirebase(category)
        }

        val item = Item()
        item_name_txt.text.toString()
        item.price = price_txt.text.toString()
        item.stock = txt_item_stock.text.toString()
        item.categoryId = category.id!!
        item.description = txt_item_description.text.toString()
        item.manufactureDetails = item_manufacture_txt.text.toString()

        Firebase.addItemToFirebase(item)

    }

    private fun getImageFromGallery(){
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, RequestCode.GET_IMAGE_RESULT.getValue)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCode.GET_IMAGE_RESULT.getValue && resultCode == RESULT_OK && data != null) {
            when(selectedImg){
                1 -> {
                    itemUri = data.data

                    Glide.with(this)
                            .load(data.data)
                            .apply(RequestOptions.circleCropTransform())
                            .error(R.drawable.default_img)
                            .into(img_item_icon)
                }
                2 -> categoryUri = data.data
            }
        }
    }

    fun dialogEditText() {
        val factory = LayoutInflater.from(this)
        val alertDialog = AlertDialog.Builder(this)
        val layoutView: View = factory.inflate(R.layout.alert_dialog_layout, null)
        val btnAdd = layoutView.btn_alert_dialog_add
        val txtCategory = layoutView.alert_dialog_edittext
        val btnCancel = layoutView.btn_alert_dialog_cancel
        val imgCategory = layoutView.alert_dialog_img
        alertDialog.setView(layoutView)
                .setCancelable(false)
        val dialog=alertDialog.create()

        imgCategory.setOnClickListener {
            selectedImg=1
            getImageFromGallery()
            imgCategory.setImageURI(categoryUri)
        }
        btnAdd.setOnClickListener{
            if(txtCategory.text.isNullOrEmpty()){
                txtCategory.error="enter category name"
                return@setOnClickListener
            }

            newCategory(txtCategory.text.toString(), categoryUri.toString())
        }
        btnCancel.setOnClickListener{dialog.dismiss()}

        dialog.show()
    }

    private fun newCategory(name: String,uri:String?=null){
        val category=Category()
        category.name=name
        if(uri!=null)category.icon=uri
        categoryList.add(index = 0, element = category)
        adapter?.notifyDataSetChanged()
        spinner_category.setSelection(0)
    }

}