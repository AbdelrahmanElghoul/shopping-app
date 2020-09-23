package com.example.shoppingapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.adapters.CartAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class CartScreen extends Fragment {

    @BindView(R.id.back_img)
    ImageView img_back;
    @BindView(R.id.cart_item_count_txt)
    TextView txt_item_count;
    @BindView(R.id.item_count_txt) TextView txt_item_count2;
    @BindView(R.id.item_price_txt) TextView txt_item_price;
    @BindView(R.id.remove_all_txt) TextView txt_remove_all;
    @BindView(R.id.continue_shopping_txt) TextView txt_continue_shopping;
    @BindView(R.id.cart_proceed_checkout) TextView txt_proceed_checkout;
    @BindView(R.id.cart_recycler_view)
    RecyclerView rv_cart;

    CartAdapter cartAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_cart_screen,container,false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUI();
    }

    void setUI(){
        RecyclerView.LayoutManager rv_cart_lm
                =new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        cartAdapter=new CartAdapter(getContext());
        rv_cart.setLayoutManager(rv_cart_lm);
        rv_cart.setHasFixedSize(false);
        rv_cart.setAdapter(cartAdapter);
    }
}