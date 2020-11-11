package com.example.shoppingapp_customer.adapters

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.shoppingapp.*
import com.example.shoppingapp.util.Firebase
import com.example.shoppingapp_customer.adapters.SearchItemAdapter.SearchItemViewHolder
import kotlinx.android.synthetic.main.fragment_sign_up.*
import timber.log.Timber
import java.util.*

class SearchItemAdapter(val context: Context,var itemList:MutableList<Item> = mutableListOf<Item>()) : RecyclerView.Adapter<SearchItemViewHolder>() {


    private var rnd = Random()
    private var maxValue = 180
    fun addItem(item: Item) {
        itemList.add(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val v = layoutInflater.inflate(R.layout.item_search_layout, parent, false)
        return SearchItemViewHolder(v)
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        val color = Color.argb(130, rnd.nextInt(maxValue), rnd.nextInt(maxValue), rnd.nextInt(maxValue))
        Timber.tag("color").d(color.toString())

        val inCart=Cart.alreadyExist(itemList[position].id)
        if(inCart){
           view(holder)
        }
        holder.layout.setOnClickListener {
            val bundle= Bundle()
            bundle.putParcelable(context.getString(R.string.PASS_CLASS_KEY),itemList[position])
            it.findNavController().navigate(R.id.action_itemsScreen_to_descriptionScreen,bundle)
        }

        Glide.with(context)
                .load(itemList[position].icon)
                .error(R.drawable.error)
                .into(holder.img)

        holder.txtPrice.text=itemList[position].price+" $"
        holder.txtName.text=itemList[position].name

        holder.layoutAddToCart.setOnClickListener {
            if(inCart)return@setOnClickListener
            Firebase.addToCart(context, CartItem(itemList[position]))
            view(holder)
        }

    }
    fun view(holder: SearchItemViewHolder){
        holder.cartImg.setImageResource(R.drawable.img_check)
        holder.txtAddToCart.text=context.getString(R.string.in_cart)
        holder.layoutAddToCart.setBackgroundColor(context.getColor(R.color.in_cart_color))
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