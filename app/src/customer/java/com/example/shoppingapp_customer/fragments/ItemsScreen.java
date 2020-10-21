package com.example.shoppingapp_customer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.shoppingapp_customer.util.Navigation;
import com.example.shoppingapp.R;
import com.example.shoppingapp_customer.util.SpacesItemDecoration;
import com.example.shoppingapp_customer.adapters.SearchItemAdapter;




public class ItemsScreen extends Fragment {

    RecyclerView rv_items;
    ImageView backImg;
    TextView searchTxt;
    ImageView searchImg;

    Navigation back;
    SearchItemAdapter searchItemAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_items_screen,container,false);

        return v;
    }
void BindView(){
     rv_items=getView().findViewById(R.id.items_recycler_view);
     backImg  =getView().findViewById(R.id.img_back);
     searchTxt =getView().findViewById(R.id.search_txt);
     searchImg  =getView().findViewById(R.id.search_img);
}
    void setUI(){
        BindView();
        back= (Navigation) getContext();
        searchItemAdapter=new SearchItemAdapter(getContext());
        rv_items.setHasFixedSize(false);
        RecyclerView.LayoutManager rv_items_lm=
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rv_items.addItemDecoration(new SpacesItemDecoration(150));
        rv_items.setLayoutManager(rv_items_lm);
        rv_items.setAdapter(searchItemAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUI();

        searchTxt.setOnClickListener(v -> Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show());
        backImg.setOnClickListener(v-> back.goBack());

    }

}

