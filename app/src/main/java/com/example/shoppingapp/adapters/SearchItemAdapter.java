package com.example.shoppingapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Item;
import com.example.shoppingapp.R;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        int color=Color.argb(255, rnd.nextInt(maxValue), rnd.nextInt(maxValue), rnd.nextInt(maxValue));
//        holder.itemLayout.setBackgroundColor(R.drawable.round_corner);
//        Toast.makeText(context, String.valueOf(color), Toast.LENGTH_SHORT).show();
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.round_corner);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, color);


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
