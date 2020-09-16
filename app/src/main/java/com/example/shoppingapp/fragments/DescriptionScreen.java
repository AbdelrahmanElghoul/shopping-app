package com.example.shoppingapp.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.SharedElementCallback;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.transition.Transition;
import androidx.transition.TransitionInflater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shoppingapp.Item;
import com.example.shoppingapp.util.Navigation;
import com.example.shoppingapp.R;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class DescriptionScreen extends Fragment {

    @BindView(R.id.bacK_img)
    ImageView backImg;
    Navigation navigation;
    @BindView(R.id.item_img)
    ImageView itemImg;
    @BindView(R.id.description_layout)
    ConstraintLayout layout;
    int color;
    Item item;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_description_screen,container,false);
        ButterKnife.bind(this,v);
        color=getArguments().getInt(getString(R.string.COLOR_TAG));

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUI();


    }

    void setUI(){
        navigation=(Navigation)getContext();
        backImg.setOnClickListener(v-> navigation.goBack());

        Timber.d(String.valueOf(color));

//        Drawable unwrappedDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.semi_circle_view);
//        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
//        DrawableCompat.setTint(wrappedDrawable, color);

       layout.getBackground().setTint(color);

    }
}