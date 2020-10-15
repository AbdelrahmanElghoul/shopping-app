package com.example.shoppingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Item;
import com.example.shoppingapp.R;

import java.util.List;


public class PreviousOrderAdapter extends RecyclerView.Adapter<PreviousOrderAdapter.PreviousOrderViewHolder> {

    Context context;
    List<Item> itemList;

    public PreviousOrderAdapter(Context context) {
        this.context = context;
    }

    public PreviousOrderAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public PreviousOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View v=layoutInflater.inflate(R.layout.previous_order_layout,parent,false);
        return new PreviousOrderAdapter.PreviousOrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PreviousOrderViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class PreviousOrderViewHolder extends RecyclerView.ViewHolder{

        ImageView itemImg;
        TextView txtPrice;
        TextView txtName;
        TextView txtQuantity;
        LinearLayout layout;
        public PreviousOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImg = itemView.findViewById(R.id.item_img);
            txtPrice = itemView.findViewById(R.id.txt_price);
            txtName = itemView.findViewById(R.id.txt_name);
            txtQuantity = itemView.findViewById(R.id.txt_quantity);
            layout = itemView.findViewById(R.id.previous_order_layout);
        }
    }
}
