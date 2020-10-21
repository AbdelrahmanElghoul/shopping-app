package com.example.shoppingapp_dealer

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppingapp.Category


class MainViewModel: ViewModel() {

    var itemUri:Uri?=null
    private var mutableCategoryList:MutableList<Category> =ArrayList()

    val categoryUri:MutableLiveData<Uri> by lazy {
        MutableLiveData<Uri>()
    }

    val categoryList: MutableLiveData<MutableList<Category>> by lazy {
        MutableLiveData<MutableList<Category>>()
    }

    fun addCategory(category: Category) {
        mutableCategoryList.add(index=0,element=category)
        categoryList.value=mutableCategoryList
    }


}