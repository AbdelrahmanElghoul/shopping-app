package com.example.shoppingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.Category;
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
        if(categoryList==null) return;
        holder.categoryImg.setImageAlpha(categoryList.get(position).getIcon());
        holder.categoryName.setText(categoryList.get(position).getName());
        holder.category_layout.setOnClickListener(v -> {
            Toast.makeText(context,String.valueOf(categoryList.get(position).getItemList().size()), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder{

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
