package com.example.shoppingapp_customer.adapters

import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp_customer.MainActivity2
import com.example.shoppingapp_customer.adapters.CategoryAdapter.CategoryViewHolder
import com.example.shoppingapp_customer.fragments.ItemsScreen


class CategoryAdapter(val context: Context) : RecyclerView.Adapter<CategoryViewHolder>() {

    private var categoryList: Pair<Array<String>, TypedArray> = Pair(context.resources.getStringArray(R.array.category_name),
            context.resources.obtainTypedArray(R.array.category_icon))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(this.context).inflate(R.layout.category_layout, parent, false)
        return CategoryViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.category_layout.setOnClickListener {
            val p1 = Pair.create<View, String>(holder.categoryImg, context.getString(R.string.CATEGORY_IMG_TRANSITION_TAG))
            val p2 = Pair.create<View, String>(holder.categoryName, context.getString(R.string.CATEGORY_NAME_TRANSITION_TAG))
            val p3 = Pair.create<View, String>(holder.category_layout, context.getString(R.string.CATEGORY_Layout_TRANSITION_TAG))
            val intent = Intent(context, MainActivity2::class.java)
            intent.putExtra(context.getString(R.string.FRAGMENT_NAME_TAG), ItemsScreen::class.java.simpleName)
            context.startActivity(intent)
        }

        holder.categoryImg.setImageResource(categoryList.second?.getResourceId(position,R.drawable.error)!!);
        holder.categoryName.text = categoryList.first?.get(position);
    }

    override fun getItemCount(): Int {
        return categoryList.first?.size!!
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryImg: ImageView
        var categoryName: TextView
        var category_layout: LinearLayout

        init {
            categoryImg = itemView.findViewById(R.id.category_img)
            categoryName = itemView.findViewById(R.id.category_txt)
            category_layout = itemView.findViewById(R.id.category_layout)
        }
    }
}