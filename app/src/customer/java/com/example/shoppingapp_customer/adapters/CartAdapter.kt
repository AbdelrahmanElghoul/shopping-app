package com.example.shoppingapp_customer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppingapp.Cart
import com.example.shoppingapp.R
import com.example.shoppingapp.util.Firebase
import com.example.shoppingapp.util.UpdateUI
import com.example.shoppingapp_customer.adapters.CartAdapter.CartViewHolder

class CartAdapter(val fragment: Fragment) : RecyclerView.Adapter<CartViewHolder>() {

    val update=fragment as UpdateUI
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val v = LayoutInflater.from(this.fragment.requireContext()).inflate(R.layout.cart_item_details_layout, parent, false)
        return CartViewHolder(v)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.txtName.text=Cart.cartList[position].name
        holder.txtPrice.text=Cart.cartList[position].price+" $"
        Glide.with(this.fragment.requireContext())
                .load(Cart.cartList[position].icon)
                .error(R.drawable.error)
                .into(holder.img)
        holder.txtQantity.text=Cart.cartList[position].quantity.toString()

        holder.imgInc.setOnClickListener{
            if(Cart.cartList[position].stock.toInt()==0) {
                Toast.makeText(fragment.requireContext(),"no more items in stock",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            Cart.updateStock(fragment.requireContext() ,position,remove=true)
            Cart.cartList[position].quantity++
            Firebase.updateCart(fragment.requireContext(),Cart.cartList[position])
//            item.stock= (item.stock.toInt()-1).toString()
            update.update(null)

        }
        holder.imgDec.setOnClickListener{
            if(Cart.cartList[position].quantity ==1) {
                Firebase.removeItemFromCart(fragment.requireContext(),position)
                update.update(null)
                return@setOnClickListener
            }
            Cart.updateStock(fragment.requireContext() ,position,remove=false)
            Cart.cartList[position].quantity--
            Firebase.updateCart(fragment.requireContext(),Cart.cartList[position])
            update.update(null)
        }
    }

    override fun getItemCount(): Int {
        return Cart.cartList.count()
    }

   class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView
        var txtName: TextView
        var txtPrice: TextView

        init {
            img = itemView.findViewById(R.id.img_cidl)
            txtName = itemView.findViewById(R.id.txt_name_cidl)
            txtPrice = itemView.findViewById(R.id.txt_price_cidl)
        }

       var txtQantity:TextView
       var imgInc:ImageView
       var imgDec:ImageView
       init {
           txtQantity = itemView.findViewById(R.id.txt_quantity_cidl)
           imgDec = itemView.findViewById(R.id.img_decrement_cidl)
           imgInc = itemView.findViewById(R.id.img_increment_cidl)
       }
    }
}