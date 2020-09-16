package com.example.shoppingapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.transition.Explode;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import com.example.shoppingapp.util.Navigation;
import com.example.shoppingapp.R;
import com.example.shoppingapp.util.SpacesItemDecoration;
import com.example.shoppingapp.adapters.SearchItemAdapter;

import java.util.List;
import java.util.Map;

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

    Navigation back;
    SearchItemAdapter searchItemAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_items_screen,container,false);
        ButterKnife.bind(this,v);
        return v;
    }

    void setUI(){
        back= (Navigation) getContext();
        searchItemAdapter=new SearchItemAdapter(getContext());
        itemsRecyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager=
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        itemsRecyclerView.addItemDecoration(new SpacesItemDecoration(150));
        itemsRecyclerView.setLayoutManager(layoutManager);
        itemsRecyclerView.setAdapter(searchItemAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUI();

        searchTxt.setOnClickListener(v -> Toast.makeText(getContext(), "clicked", Toast.LENGTH_SHORT).show());
        backImg.setOnClickListener(v-> back.goBack());

    }

}

