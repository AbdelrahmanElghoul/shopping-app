package com.example.shoppingapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.TransitionRes;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Animation;
import com.example.shoppingapp.Category;
import com.example.shoppingapp.MainActivity2;
import com.example.shoppingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

//            Animation.ContextSceneTransition(
//                    holder.category_layout,
//                    context.getString(R.string.layout_transition),
//                    context,
//                    new Intent(context, MainActivity2.class));

            Pair<View, String> p1 = Pair.create(holder.category_layout,  context.getString(R.string.layout_transition));
            Pair<View, String> p2 = Pair.create(holder.categoryImg,  context.getString(R.string.image_transition));
            Pair<View, String> p3 = Pair.create(holder.categoryName,  context.getString(R.string.text_transition));

            Animation.MultipleSharedElementTransition(
                    context,
                    new Intent(context, MainActivity2.class),
                    p1,p2,p3);
        });

        if(categoryList==null) return;
        holder.categoryImg.setImageAlpha(categoryList.get(position).getIcon());
        holder.categoryName.setText(categoryList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.category_img)
        ImageView categoryImg;
        @BindView(R.id.category_txt)
        TextView categoryName;
        @BindView(R.id.category_layout)
        LinearLayout category_layout;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
