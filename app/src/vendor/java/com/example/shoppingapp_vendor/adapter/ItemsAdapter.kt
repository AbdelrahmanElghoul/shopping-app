package com.example.shoppingapp_vendor.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingapp.Item
import com.example.shoppingapp.R
import com.example.shoppingapp_vendor.VendorActivity

class ItemsAdapter(val context: Context,val itemList:List<Item>) : RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>() {

    class ItemsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var txtName: TextView
         var txtPrice: TextView
         var imgIcon: ImageView
         var layoutCard: CardView

        init {
            this.txtName = itemView.findViewById(R.id.txt_item_name_il)
            this.txtPrice = itemView.findViewById(R.id.txt_item_price_il)
            this.imgIcon = itemView.findViewById(R.id.img_il)
            this.layoutCard = itemView.findViewById(R.id.card_il)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val inflater=LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false)
        return ItemsViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.txtName.text=itemList[position].name
        holder.txtPrice.text= itemList[position].price + " $"
        Glide.with(context)
                .load(itemList[position].icon)
//                .apply(RequestOptions.circleCropTransform())
                .error(R.drawable.default_img)
                .into(holder.imgIcon)
        holder.layoutCard.setOnClickListener {
            val intent= Intent(context,VendorActivity::class.java)
            val bundle=Bundle()
            bundle.putParcelable(context.getString(R.string.class_id),itemList[position])
            intent.putExtras(bundle)
            context.startActivity(intent)
            (context as Activity).finish()
        }

    }

    override fun getItemCount(): Int {
       return itemList.size
    }
}