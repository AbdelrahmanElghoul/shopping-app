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

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;

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

        @BindView(R.id.item_img)
        ImageView itemImg;
        @BindView(R.id.txt_price)
        TextView txtPrice;
        @BindView(R.id.txt_name)
        TextView txtName;
        @BindView(R.id.txt_quantity)
        TextView txtQuantity;
        @BindView(R.id.previous_order_layout)
        LinearLayout layout;
        public PreviousOrderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
