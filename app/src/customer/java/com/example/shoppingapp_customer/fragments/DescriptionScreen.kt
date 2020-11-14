package com.example.shoppingapp_customer.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle

import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.shoppingapp.Cart
import com.example.shoppingapp.CartItem
import com.example.shoppingapp.Item
import com.example.shoppingapp.R
import com.example.shoppingapp.util.Firebase
import com.google.firebase.database.*
import kotlinx.android.synthetic.customer.fragment_description_screen.*
import kotlinx.android.synthetic.customer.fragment_description_screen.img_item_dsf
import timber.log.Timber.d


class DescriptionScreen : Fragment() {

    var item= Item()
    lateinit var myRef:DatabaseReference
    private lateinit var listener:ValueEventListener
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_description_screen, container, false)
//        color = requireArguments().getInt(getString(R.string.COLOR_TAG))
        item.id=requireArguments().getString(getString(R.string.PASS_ITEM_ID_KEY))!!
        d("item id = ${item.id}")
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = FirebaseDatabase.getInstance()
        myRef =database.getReference(Firebase.Items.ITEMS.Key).child(item.id)
        listener=object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val tmpID=item.id
                d("child change $snapshot")
                item=snapshot.getValue(Item::class.java)!!
                item.id=tmpID
                setUI()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        myRef.addValueEventListener(listener)

        img_back_dsf.setOnClickListener {
           requireActivity().onBackPressed()
       }
        fab_add_to_cart_dsf.setOnClickListener {
            val index=Cart.getIndex(item.id)
            when {
                index>=0 -> {//remove from cart
                    Firebase.removeItemFromCart(requireContext(), CartItem(item))
                    inCartUI(false)
                }
                item.stock.toInt()==0 -> {
                    Toast.makeText(requireContext(),"No items in stock\ntry again later",Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                else -> {//add to cart
                    Firebase.addToCart(requireContext(), CartItem(item))
                    inCartUI(true)
                }
            }
        }
    }

    private fun inCartUI(inCart: Boolean){
            if(inCart) {
                txt_quantity_dsf.text = "${item.stock} in stock"
                fab_add_to_cart_dsf.backgroundTintList = ColorStateList.valueOf(getColor(requireContext(), R.color.in_cart_color))
                fab_add_to_cart_dsf.setImageResource(R.drawable.img_check)
            }
            else{
                txt_quantity_dsf.text="${item.stock} in stock"
                fab_add_to_cart_dsf.backgroundTintList = ColorStateList.valueOf(getColor(requireContext(), R.color.colorAccent))
                fab_add_to_cart_dsf.setImageResource(R.drawable.img_add_to_cart)
                ImageViewCompat.setImageTintList(
                        fab_add_to_cart_dsf,
                        ColorStateList.valueOf(Color.WHITE)
                )
            }
    }

    fun setUI() {
        txt_name_dsf.text=item.name
        txt_price_dsf.text="${item.price} $"
        txt_quantity_dsf.text="${item.stock} in stock"
        txt_description_dsf.text=item.description
        txt_manufacture_dsf.text=item.manufacture
        Glide.with(context as Context)
                .load(item.icon)
                .error(R.drawable.default_img)
                .into(img_item_dsf)

        val inCart= Cart.getIndex(item.id)
        if(inCart>=0) inCartUI(true)
//        Drawable unwrappedDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.semi_circle_view);
//        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
//        DrawableCompat.setTint(wrappedDrawable, color);
//        layout_description_dsf.background.setTint(color)
    }

    override fun onDetach() {
        myRef.removeEventListener(listener)
        super.onDetach()
    }
}