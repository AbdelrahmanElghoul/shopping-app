package com.example.shoppingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.adapters.CategoryAdapter;
import com.example.shoppingapp.adapters.PreviousOrderAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeScreen extends Fragment {

    @BindView(R.id.category_recycler_view)
    RecyclerView rv_category;
    @BindView(R.id.previous_order_recycler_view)
    RecyclerView rv_previous_order;
    @BindView(R.id.txt_see_all_category)
    TextView txt_see_all_category;
    @BindView(R.id.txt_see_all_previous_order)
    TextView txt_see_all_previous_order;


    CategoryAdapter categoryAdapter;
    PreviousOrderAdapter previousOrderAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_home_screen,container,false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUI();
    }

    private void setUI() {
        RecyclerView.LayoutManager rv_category_lm=new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        categoryAdapter=new CategoryAdapter(getContext());
        rv_category.setHasFixedSize(true);
        rv_category.setLayoutManager(rv_category_lm);
        rv_category.setAdapter(categoryAdapter);

        RecyclerView.LayoutManager rv_previous_order_lm=new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        previousOrderAdapter=new PreviousOrderAdapter(getContext());
        rv_previous_order.setHasFixedSize(true);
        rv_previous_order.setLayoutManager(rv_previous_order_lm);
        rv_previous_order.setAdapter(previousOrderAdapter);
    }
}