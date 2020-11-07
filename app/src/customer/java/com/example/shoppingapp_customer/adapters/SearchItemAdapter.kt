package com.example.shoppingapp_customer.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingapp.Item
import com.example.shoppingapp.R
import com.example.shoppingapp_customer.adapters.SearchItemAdapter.SearchItemViewHolder
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import timber.log.Timber
import java.util.*
import kotlinx.android.synthetic.customer.item_search_layout.*
import timber.log.Timber.d

class SearchItemAdapter(val context: Context) : RecyclerView.Adapter<SearchItemViewHolder>() {

    private var itemList = mutableListOf<Item>()
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

        holder.layout.setOnClickListener { d(itemList[position].id) }
        holder.txtPrice.text=itemList[position].price+" $"
        holder.txtName.text=itemList[position].name
        Glide.with(context)
                .load(itemList[position].icon)
                .error(R.drawable.default_img)
                .into(holder.img)
        holder.fab.setOnClickListener { d("add to cart") }
        //        holder.itemLayout.getBackground().setTint(color);
//
//
//        holder.itemLayout.setOnClickListener(v-> {
//            Intent intent=new Intent(context, MainActivity2.class);
//            intent.putExtra(context.getString(R.string.FRAGMENT_NAME_TAG), DescriptionScreen.class.getSimpleName());
//            intent.putExtra(context.getString(R.string.COLOR_TAG),color);
////            intent.putExtra(context.getString(R.string.OBJECT_TAG), itemList.get(position));
//
//            context.startActivity(intent);
//        });
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class SearchItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtName:TextView
        var txtPrice:TextView
        var img:ImageView
        var fab:ConstraintLayout
        var layout:MaterialCardView

        init {
            this.txtName = itemView.findViewById(R.id.txt_item_name_isl)
            this.txtPrice = itemView.findViewById(R.id.txt_item_price_isl)
            this.fab = itemView.findViewById(R.id.layout_add_to_cart_isl)
            this.img = itemView.findViewById(R.id.img_isl)
            this.layout = itemView.findViewById(R.id.layout_item_isl)
        }
    }
}