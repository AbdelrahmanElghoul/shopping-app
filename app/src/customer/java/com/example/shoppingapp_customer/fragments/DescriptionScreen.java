package com.example.shoppingapp_customer.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shoppingapp.Item;
import com.example.shoppingapp_customer.util.Navigation;
import com.example.shoppingapp.R;


import timber.log.Timber;


public class DescriptionScreen extends Fragment {

    ImageView backImg;
    Navigation navigation;
    ImageView itemImg;
    ConstraintLayout layout;
    int color;
    Item item;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_description_screen,container,false);
        color=getArguments().getInt(getString(R.string.COLOR_TAG));

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUI();


    }
void BindView() {
    backImg = getView().findViewById(R.id.back_img);
    itemImg = getView().findViewById(R.id.item_img);
    layout = getView().findViewById(R.id.description_layout);
}
    void setUI(){
        BindView();

        navigation=(Navigation)getContext();
        backImg.setOnClickListener(v-> navigation.goBack());

        Timber.d(String.valueOf(color));

//        Drawable unwrappedDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.semi_circle_view);
//        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
//        DrawableCompat.setTint(wrappedDrawable, color);

       layout.getBackground().setTint(color);

    }
}