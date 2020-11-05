package com.example.shoppingapp_customer.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Item;
import com.example.shoppingapp_customer.MainActivity2;

import com.example.shoppingapp.R;
import com.example.shoppingapp_customer.fragments.DescriptionScreen;

import java.util.List;
import java.util.Random;

import timber.log.Timber;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.SearchItemViewHolder> {

    Context context;
    List<Item> itemList;
    Random rnd = new Random();
    int maxValue=180;

    public SearchItemAdapter(Context context) {
        this.context = context;
    }

    public SearchItemAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public SearchItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View v=layoutInflater.inflate(R.layout.item_search_layout,parent,false);
        return new SearchItemAdapter.SearchItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemViewHolder holder, int position)
    {
        int color=Color.argb(130, rnd.nextInt(maxValue), rnd.nextInt(maxValue), rnd.nextInt(maxValue));
        Timber.e(String.valueOf(color));
        holder.itemLayout.getBackground().setTint(color);


        holder.itemLayout.setOnClickListener(v-> {
            Intent intent=new Intent(context, MainActivity2.class);
            intent.putExtra(context.getString(R.string.FRAGMENT_NAME_TAG), DescriptionScreen.class.getSimpleName());
            intent.putExtra(context.getString(R.string.COLOR_TAG),color);
//            intent.putExtra(context.getString(R.string.OBJECT_TAG), itemList.get(position));

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class SearchItemViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout itemLayout;
        ImageView itemImg;
        ImageView cartImg;
        ImageView bookmarkImg;
        TextView itemNameTxt;
        TextView itemPriceTxt;
        TextView itemQuantityTxt;

        public SearchItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemLayout = itemView.findViewById(R.id.search_item_layout);
            itemImg = itemView.findViewById(R.id.search_item_img);
            cartImg = itemView.findViewById(R.id.search_item_cart_img);
            bookmarkImg = itemView.findViewById(R.id.search_item_bookmark_img);
            itemNameTxt = itemView.findViewById(R.id.search_item_name_txt);
            itemPriceTxt = itemView.findViewById(R.id.search_item_price_txt);
            itemQuantityTxt = itemView.findViewById(R.id.search_item_quantity_txt);
        }
    }
}
