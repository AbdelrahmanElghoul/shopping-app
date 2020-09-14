package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.transition.Fade;
import android.widget.FrameLayout;

import com.example.shoppingapp.fragments.ItemsScreen;

import butterknife.BindView;

public class MainActivity2 extends AppCompatActivity implements Navigation{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Animation.FadeTransition(this,false,true);

        FragmentTransaction fragmentTransaction=getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction
                .add(R.id.activity2_frame,new ItemsScreen())
                .commit();
    }

    @Override
    public void goBack() {
        super.onBackPressed();
    }
}

