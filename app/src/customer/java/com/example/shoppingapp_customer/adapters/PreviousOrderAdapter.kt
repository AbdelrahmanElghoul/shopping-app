package com.example.shoppingapp_customer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.CartItem
import com.example.shoppingapp.R
import com.example.shoppingapp_customer.adapters.PreviousOrderAdapter.PreviousOrderViewHolder

class PreviousOrderAdapter(var context: Context, var itemList: List<CartItem>) : RecyclerView.Adapter<PreviousOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviousOrderViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.previous_order_layout, parent, false)
        return PreviousOrderViewHolder(v)
    }

    override fun onBindViewHolder(holder: PreviousOrderViewHolder, position: Int) {
        holder.txtName.text=itemList[position].name
    }
    override fun getItemCount(): Int {
        return itemList.count()
    }

    class PreviousOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImg: ImageView
        val txtPrice: TextView
        val txtName: TextView
        val txtQuantity: TextView


        init {
            itemImg = itemView.findViewById(R.id.img_item_pol)
            txtPrice = itemView.findViewById(R.id.txt_price_pol)
            txtName = itemView.findViewById(R.id.txt_name_dsf)
            txtQuantity = itemView.findViewById(R.id.txt_quantity_dsf)

        }
    }
}