package com.example.shoppingapp_customer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.shoppingapp.Item;
import com.example.shoppingapp.R;

import java.util.List;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    Context context;
    List<Item> itemList;

    public CartAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public CartAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.cart_item_details_layout,parent,false);
        return new CartAdapter.CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class CartViewHolder extends RecyclerView.ViewHolder{

        ImageView img_item;
        TextView txt_itemName;
        TextView txt_quantity;
        TextView txt_price;
        ImageView img_decrement;
        TextView txt_counter;
        ImageView img_increment;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            img_item = itemView.findViewById(R.id.cart_item_img);
            txt_itemName = itemView.findViewById(R.id.cart_item_name_txt);
            txt_quantity = itemView.findViewById(R.id.cart_item_quantity_txt);
            txt_price = itemView.findViewById(R.id.cart_item_price_txt);
            img_decrement = itemView.findViewById(R.id.decrement_img);
            txt_counter = itemView.findViewById(R.id.cart_item_counter_txt);
            img_increment = itemView.findViewById(R.id.increment_img);

        }
    }
}
