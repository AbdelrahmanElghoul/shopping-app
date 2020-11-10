package com.example.shoppingapp_customer.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.shoppingapp.Item
import com.example.shoppingapp.R
import kotlinx.android.synthetic.customer.fragment_description_screen.*
import kotlinx.android.synthetic.customer.fragment_description_screen.img_item_dsf
import kotlinx.android.synthetic.customer.previous_order_layout.*


class DescriptionScreen : Fragment() {
    lateinit var item: Item

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_description_screen, container, false)
//        color = requireArguments().getInt(getString(R.string.COLOR_TAG))
        item=requireArguments().getParcelable<Item>(getString(R.string.PASS_CLASS_KEY))!!
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
       img_back_dsf.setOnClickListener {
           requireActivity().onBackPressed()
       }
    }



    fun setUI() {
        txt_name_dsf.text=item.name
        txt_price_dsf.text=item.price+" $"
        txt_stock_dsf.text=item.stock+" in stock"
        txt_description_dsf.text=item.description
        txt_manufacture_dsf.text=item.manufacture
        Glide.with(context as Context)
                .load(item.icon)
                .error(R.drawable.default_img)
                .into(img_item_dsf)
//        Drawable unwrappedDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.semi_circle_view);
//        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
//        DrawableCompat.setTint(wrappedDrawable, color);
//        layout_description_dsf.background.setTint(color)
    }
}