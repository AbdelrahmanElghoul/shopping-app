package com.example.shoppingapp.adapters;

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

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        @BindView(R.id.cart_item_img) ImageView img_item;
        @BindView(R.id.cart_item_name_txt) TextView txt_itemName;
        @BindView(R.id.cart_item_quantity_txt) TextView txt_quantity;
        @BindView(R.id.cart_item_price_txt)    TextView txt_price;
        @BindView(R.id.decrement_img) ImageView img_decrement;
        @BindView(R.id.cart_item_counter_txt) TextView txt_counter;
        @BindView(R.id.increment_img) ImageView img_increment;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
