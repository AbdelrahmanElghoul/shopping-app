package com.example.shoppingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.fragments.ItemsScreen;
import com.example.shoppingapp.util.Animation;
import com.example.shoppingapp.Category;
import com.example.shoppingapp.MainActivity2;
import com.example.shoppingapp.R;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {


    List<Category> categoryList;
    Context context;

    public CategoryAdapter(List<Category> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
    }
    public CategoryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v=layoutInflater.inflate(R.layout.category_layout,parent,false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.category_layout.setOnClickListener(v ->  {


            Pair<View, String> p1 = Pair.create(holder.categoryImg,  context.getString(R.string.CATEGORY_IMG_TRANSITION_TAG));
            Pair<View, String> p2 = Pair.create(holder.categoryName,  context.getString(R.string.CATEGORY_NAME_TRANSITION_TAG));
            Pair<View, String> p3 = Pair.create(holder.category_layout,  context.getString(R.string.CATEGORY_Layout_TRANSITION_TAG));

            Intent intent=new Intent(context, MainActivity2.class);
            intent.putExtra(context.getString(R.string.FRAGMENT_NAME_TAG), ItemsScreen.class.getSimpleName());

            Animation.MultipleSharedElementTransition(
                    context,
                    intent,
                    p3,p2,p1);
        });

        if(categoryList==null) return;
//        holder.categoryImg.setImageAlpha(categoryList.get(position).getIcon());
        holder.categoryName.setText(categoryList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder{


        ImageView categoryImg;

        TextView categoryName;

        LinearLayout category_layout;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImg = itemView.findViewById(R.id.category_img);
            categoryName = itemView.findViewById(R.id.category_txt);
            category_layout = itemView.findViewById(R.id.category_layout);
        }
    }


}
