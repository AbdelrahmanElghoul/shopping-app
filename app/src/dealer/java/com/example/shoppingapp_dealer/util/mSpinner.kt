package com.example.shoppingapp_dealer.util

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.AbsSpinner
import android.widget.Spinner

@SuppressLint("AppCompatCustomView")
class mSpinner : Spinner {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}


    override fun setSelection(position: Int, animate: Boolean) {
        val sameSelected = position == getSelectedItemPosition()
        super.setSelection(position, animate)
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            onItemSelectedListener?.onItemSelected(this, getSelectedView(), position, getSelectedItemId())
        }
    }


    override fun setSelection(position: Int) {
        val sameSelected = position == getSelectedItemPosition()
        super.setSelection(position)
        if (sameSelected) {
            // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
            onItemSelectedListener?.onItemSelected(this, getSelectedView(), position, getSelectedItemId())
        }
    }
}