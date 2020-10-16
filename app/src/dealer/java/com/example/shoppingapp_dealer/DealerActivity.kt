package com.example.shoppingapp_dealer

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shoppingapp.Item
import com.example.shoppingapp.R
import com.example.shoppingapp.util.RequestCode
import kotlinx.android.synthetic.dealer.activity_dealer.*


var item:Item?=null
var imgUri:Uri?=null
var adapter:ArrayAdapter<String>?=null
var stringList= mutableListOf<String>( "1", "2", "3","Add category")
class DealerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dealer)
        btn_add_item.setOnClickListener { btnAdd() }

        img_item_icon.setOnClickListener { getImageFromGallery()}

       adapter=ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stringList)
        spinner_category.adapter=adapter

        spinner_category.setSelection(0)
        Toast.makeText(this@DealerActivity, "${spinner_category.selectedItem}", Toast.LENGTH_LONG).show()
        spinner_category?.onItemSelectedListener =object: AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(spinner_category.selectedItem.toString() == "Add category") dialogEditText()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun validateViews(){
        if(txt_item_name.text.isNullOrEmpty()) txt_item_name.error = "empty field"
        if(txt_item_price.text.isNullOrEmpty()) txt_item_price.error = "empty field"
        if(txt_item_stock.text.isNullOrEmpty() || txt_item_stock.text!!.equals('0')) txt_item_stock.error = "empty field"
        if(spinner_category.selectedItem.toString().equals("Add category")) txt_item_category.error = "empty field"

    }

    private fun btnAdd(){
        validateViews()
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
            imgUri=data.data

            Glide.with(this)
                    .load(imgUri)
                    .apply(RequestOptions.circleCropTransform())
                    .error(R.drawable.default_img)
                    .into(img_item_icon)
        }
    }

    fun dialogEditText(){
        var alertDialog=AlertDialog.Builder(this)
        alertDialog.setTitle("category name")
        val input = EditText(this)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        input.layoutParams = lp
        alertDialog.setPositiveButton(getString(R.string.add)) {_,_ ->
            stringList.add(input.text.toString())
            adapter?.notifyDataSetChanged()
            spinner_category.setSelection(stringList.size-1)
        }

        alertDialog.setNegativeButton(getString(R.string.cancel)) {dialogue,_ ->
           dialogue.dismiss()
        }


        alertDialog.setView(input)
        alertDialog.show()
    }

}