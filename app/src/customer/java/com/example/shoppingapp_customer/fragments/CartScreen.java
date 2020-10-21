package com.example.shoppingapp_customer.fragments;

import android.content.Intent;
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

import com.example.shoppingapp_customer.MainActivity2;
import com.example.shoppingapp.R;
import com.example.shoppingapp_customer.adapters.CartAdapter;

public class CartScreen extends Fragment {

    ImageView img_back;
    TextView txt_item_count;
    TextView txt_item_count2;
    TextView txt_item_price;
    TextView txt_remove_all;
    TextView txt_continue_shopping;
    TextView txt_proceed_checkout;
    RecyclerView rv_cart;

    CartAdapter cartAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_cart_screen,container,false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUI();

        txt_proceed_checkout.setOnClickListener(v->{
            Intent intent=new Intent(getContext(), MainActivity2.class);
            intent.putExtra(getContext().getString(R.string.FRAGMENT_NAME_TAG), CheckoutScreen.class.getSimpleName());
            startActivity(intent);
        });
    }

    void BindView() {

        img_back =getView().findViewById(R.id.back_img);
        txt_item_count = getView().findViewById(R.id.cart_item_count_txt);
        txt_item_count2 = getView().findViewById(R.id.item_count_txt);
        txt_item_price = getView().findViewById(R.id.item_price_txt);
        txt_remove_all = getView().findViewById(R.id.remove_all_txt);
        txt_continue_shopping = getView().findViewById(R.id.continue_shopping_txt);
        txt_proceed_checkout = getView().findViewById(R.id.cart_proceed_checkout);
        rv_cart = getView().findViewById(R.id.cart_recycler_view);
    }
    void setUI(){
        BindView();
        
        RecyclerView.LayoutManager rv_cart_lm
                =new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        cartAdapter=new CartAdapter(getContext());
        rv_cart.setLayoutManager(rv_cart_lm);
        rv_cart.setHasFixedSize(false);
        rv_cart.setAdapter(cartAdapter);
    }
}