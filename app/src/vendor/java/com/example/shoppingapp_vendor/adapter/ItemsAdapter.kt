package com.example.shoppingapp_vendor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R

class ItemsAdapter(val context: Context) : RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>() {

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

    }

    override fun getItemCount(): Int {
       return 10
    }
}