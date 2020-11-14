package com.example.shoppingapp_customer.adapters

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingapp.*
import com.example.shoppingapp.util.Firebase
import com.example.shoppingapp_customer.adapters.SearchItemAdapter.SearchItemViewHolder
import timber.log.Timber
import timber.log.Timber.tag
import java.util.*

class SearchItemAdapter(val context: Context,var itemList:MutableList<Item>) : RecyclerView.Adapter<SearchItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val v = layoutInflater.inflate(R.layout.item_search_layout, parent, false)
        return SearchItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        holder.txtPrice.text=itemList[position].price+" $"
        holder.txtName.text=itemList[position].name
        holder.layout.setOnClickListener {
            val bundle= Bundle()
            bundle.putString(context.getString(R.string.PASS_ITEM_ID_KEY),itemList[position].id)
            it.findNavController().navigate(R.id.action_itemsScreen_to_descriptionScreen,bundle)
        }
        Glide.with(context)
                .load(itemList[position].icon)
                .error(R.drawable.error)
                .into(holder.img)

        view(holder,(Cart.getIndex(itemList[position].id)>=0))

        holder.layoutAddToCart.setOnClickListener {
            when {
                (Cart.getIndex(itemList[position].id)>=0) -> {
                    Firebase.removeItemFromCart(context,CartItem(itemList[position]))
                    view(holder,false)
                    tag("SearchAdapter").d("removeFromCart")
                    notifyDataSetChanged()
                }
                itemList[position].stock.toInt()==0 -> {
                    Toast.makeText(context,"No items in stock\ntry again later", Toast.LENGTH_LONG).show()
                    tag("SearchAdapter").d("empty")
                }
                else -> {
                    tag("SearchAdapter").d("addToCart")
                    Firebase.addToCart(context, CartItem(itemList[position]))
                    view(holder,true)
                    notifyDataSetChanged()
                }
            }
        }

    }

    fun view(holder: SearchItemViewHolder,inCart:Boolean){
        if(inCart){
            holder.cartImg.setImageResource(R.drawable.img_check)
            holder.txtAddToCart.text = context.getString(R.string.in_cart)
            holder.layoutAddToCart.setBackgroundColor(context.getColor(R.color.in_cart_color))
        }else{
            holder.cartImg.setImageResource(R.drawable.img_add_to_cart)
            holder.txtAddToCart.text = context.getString(R.string.add_to_cart)
            holder.layoutAddToCart.setBackgroundColor(context.getColor(R.color.btn_color))
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class SearchItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtName:TextView
        var txtPrice:TextView
        var img:ImageView
        var layout:ConstraintLayout
        init {
            this.txtName = itemView.findViewById(R.id.txt_item_name_isl)
            this.txtPrice = itemView.findViewById(R.id.txt_item_price_isl)
            this.layout = itemView.findViewById(R.id.layout_item_isl)
            this.img = itemView.findViewById(R.id.img_isl)
        }

        var cartImg:ImageView
        var layoutAddToCart:ConstraintLayout
        var txtAddToCart:TextView

        init{
            this.txtAddToCart = itemView.findViewById(R.id.txt_add_to_cart_isl)
            this.cartImg = itemView.findViewById(R.id.img_cart_isl)
            this.layoutAddToCart = itemView.findViewById(R.id.layout_add_to_cart_isl)
        }
    }
}