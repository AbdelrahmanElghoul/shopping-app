package com.example.shoppingapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoppingapp.R;
import com.example.shoppingapp.SpacesItemDecoration;
import com.example.shoppingapp.adapters.PreviousOrderAdapter;
import com.example.shoppingapp.adapters.SearchItemAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ItemsScreen extends Fragment {

    @BindView(R.id.items_recycler_view)
    RecyclerView itemsRecyclerView;
    @BindView(R.id.img_back)
    ImageView backImg;
    @BindView(R.id.search_txt)
    TextView searchTxt;
    @BindView(R.id.search_img)
    ImageView searchImg;


    SearchItemAdapter searchItemAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_items_screen,container,false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchItemAdapter=new SearchItemAdapter(getContext());
        itemsRecyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager=
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        itemsRecyclerView.addItemDecoration(new SpacesItemDecoration(150));
        itemsRecyclerView.setLayoutManager(layoutManager);
        itemsRecyclerView.setAdapter(searchItemAdapter);

    }

}

