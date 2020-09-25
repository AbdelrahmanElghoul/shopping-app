package com.example.shoppingapp.adapters;

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
import com.example.shoppingapp.MainActivity2;
import com.example.shoppingapp.util.Animation;
import com.example.shoppingapp.R;
import com.example.shoppingapp.fragments.DescriptionScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
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
            Pair<View, String> p1 = Pair.create(holder.itemImg,  context.getString(R.string.DESCRIPTION_IMG_TRANSITION_TAG));
            Pair<View, String> p2 = Pair.create(holder.itemNameTxt,  context.getString(R.string.DESCRIPTION_NAME_TRANSITION_TAG));
            Pair<View, String> p3 = Pair.create(holder.itemPriceTxt,  context.getString(R.string.DESCRIPTION_PRICE_TRANSITION_TAG));
            Pair<View, String> p4 = Pair.create(holder.itemQuantityTxt,  context.getString(R.string.DESCRIPTION_QUANTITY_TRANSITION_TAG));
            Pair<View, String> p5 = Pair.create(holder.itemLayout,  context.getString(R.string.DESCRIPTION_LAYOUT_TRANSITION_TAG));

            Intent intent=new Intent(context, MainActivity2.class);
            intent.putExtra(context.getString(R.string.FRAGMENT_NAME_TAG), DescriptionScreen.class.getSimpleName());
            intent.putExtra(context.getString(R.string.COLOR_TAG),color);
//            intent.putExtra(context.getString(R.string.OBJECT_TAG), itemList.get(position));

            Animation.MultipleSharedElementTransition(
                    context,
                    intent,
                    p1,p2,p3,p4,p5);

        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class SearchItemViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.search_item_layout)
        ConstraintLayout itemLayout;
        @BindView(R.id.search_item_img)
        ImageView itemImg;
        @BindView(R.id.search_item_cart_img)
        ImageView cartImg;
        @BindView(R.id.search_item_bookmark_img)
        ImageView bookmarkImg;
        @BindView(R.id.search_item_name_txt)
        TextView itemNameTxt;
        @BindView(R.id.search_item_price_txt)
        TextView itemPriceTxt;
        @BindView(R.id.search_item_quantity_txt)
        TextView itemQuantityTxt;

        public SearchItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
