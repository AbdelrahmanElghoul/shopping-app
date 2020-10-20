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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shoppingapp.Category
import com.example.shoppingapp.Item
import com.example.shoppingapp.R
import com.example.shoppingapp.util.RequestCode
import com.example.shoppingapp_dealer.util.Firebase
import kotlinx.android.synthetic.dealer.activity_dealer.*
import kotlinx.android.synthetic.dealer.alert_dialog_layout.view.*


import timber.log.Timber
import timber.log.Timber.DebugTree


var adapter: ArrayAdapter<Category> ?= null
var selectedImg:Int=-1  // itemUri=0 / categeoryUri=1
lateinit var viewModel:MainViewModel
var categoryList= listOf<Category>()

class DealerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dealer)
        initialiseData()

        viewModel.categoryList.observe(this, {
//            Timber.d("Spinner adapter size ${categoryList.size}")
//            categoryList=it
//            Timber.d("Spinner adapter size ${categoryList.size}")
//            adapter?.notifyDataSetChanged()

            adapter=ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, it)
            spinner_category_ad.adapter=adapter
        })

        spinner_layout_ad.setOnClickListener{
            Timber.tag("spinner").d("called")
            spinner_category_ad.callOnClick()
        }
        btn_add_item.setOnClickListener { btnAdd() }
        img_item_icon_ad.setOnClickListener {
            selectedImg=0
            getImageFromGallery()
        }

        spinner_category_ad?.onItemSelectedListener =object: AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(spinner_category_ad.selectedItem.toString() == getString(R.string.add_category_txt))
                  {
                      viewModel.categoryUri.value=null
                      dialogEditText()
                  }
                else
                    {Glide.with(this@DealerActivity)
                        .load(viewModel.categoryList.value?.get(position)?.icon)
                        .apply(RequestOptions.circleCropTransform())
                        .error(R.drawable.default_img)
                        .into(img_selected_category_ad)

                        Timber.tag("icon").d(viewModel.categoryList.value?.get(position)?.icon)
                    }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initialiseData(){

        Timber.plant(DebugTree())
        viewModel=ViewModelProvider(this@DealerActivity).get(MainViewModel::class.java)

        adapter=ArrayAdapter<Category>(this, android.R.layout.simple_spinner_dropdown_item, categoryList)
        spinner_category_ad.adapter=adapter

        if(viewModel.categoryList.value.isNullOrEmpty()) {
            val cat=Category()
            cat.name=getString(R.string.add_category_txt)
            viewModel.addCategory(cat)
        }

    }

    private fun validateViews():Boolean {
        var isValid =true
        if (txt_item_description_ad.text.isNullOrEmpty()) {
            txt_item_description_ad.error = "empty field"
            isValid=false
        }
        if (txt_item_price_ad.text.isNullOrEmpty()) {
            txt_item_description_ad.error = "empty field"
            isValid= false
        }
        if (txt_item_stock_ad.text.isNullOrEmpty() || txt_item_stock_ad.text!!.equals("0")) {
            txt_item_description_ad.error = "empty field"
            isValid= false
        }
        if (spinner_category_ad.selectedItem.toString() == getString(R.string.add_category_txt)) {
               Toast.makeText(this, "select category", Toast.LENGTH_LONG).show()
            isValid= false
        }

        return isValid
    }

    private fun btnAdd() {
        if (!validateViews()) return
        val category = viewModel.categoryList.value?.get(spinner_category_ad.selectedItemPosition)
        if (category?.id == null) {
            if (category?.icon != null)
                category.icon = Firebase.addImageToStorage(category.icon!!)
            category?.id = Firebase.addCategoryToFirebase(category as Category)
        }

        val item = Item()
        item.name = txt_item_name_ad.text.toString()
        item.price = txt_item_price_ad.text.toString()
        item.stock = txt_item_stock_ad.text.toString()
        item.categoryId = category.id!!
        item.description = txt_item_description_ad.text.toString()
        item.manufactureDetails = txt_manufacture_ad.text.toString()

        Firebase.addItemToFirebase(item)

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
            when(selectedImg){
                0 -> {
                    viewModel.itemUri = data.data
                    Glide.with(this)
                            .load(viewModel.itemUri)
                            .apply(RequestOptions.circleCropTransform())
                            .error(R.drawable.default_img)
                            .into(img_item_icon_ad)
                }
                1 -> {
//                    categoryUri = data.data
                    viewModel.categoryUri.value =data.data
                }
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
        val imgAlertDialogCategory = layoutView.alert_dialog_img

        alertDialog.setView(layoutView)
                .setCancelable(false)
        val dialog = alertDialog.create()

        imgAlertDialogCategory.setOnClickListener {
            selectedImg = 1
            getImageFromGallery()
            viewModel.categoryUri.observe(this, {
                Glide.with(this@DealerActivity)
                        .load(it)
                        .apply(RequestOptions.circleCropTransform())
                        .error(R.drawable.default_img)
                        .into(imgAlertDialogCategory)
            })
        }

        btnAdd.setOnClickListener {
            if (txtCategory.text.isNullOrEmpty()) {
                txtCategory.error = "enter category name"
                return@setOnClickListener
            }

            newCategory(txtCategory.text.toString(), viewModel.categoryUri.value)
            dialog.dismiss()
        }
        btnCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun newCategory(name: String,uri:Uri?=null){
        val category=Category()
        category.name=name
        if(uri!=null)category.icon=uri.toString()
        viewModel.addCategory(category)
        spinner_category_ad.setSelection(0)
    }

}

