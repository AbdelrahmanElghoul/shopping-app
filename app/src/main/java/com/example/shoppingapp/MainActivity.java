package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.transition.Transition;

import com.example.shoppingapp.adapters.ViewPagerAdapter;
import com.example.shoppingapp.fragments.DescriptionScreen;
import com.example.shoppingapp.fragments.HomeScreen;
import com.example.shoppingapp.util.Animation;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.transition.platform.MaterialFade;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager2 pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Transition transition=new MaterialFade();
        getWindow().setExitTransition(transition);

        setContentView(R.layout.activity_main);
        Timber.plant(new Timber.DebugTree());
        ButterKnife.bind(this);

        ViewPagerAdapter adapter=new ViewPagerAdapter(this);
        adapter.addList(new HomeScreen(),R.drawable.home_img);
        adapter.addList(new HomeScreen(),R.drawable.shopping_cart_img);
        adapter.addList(new HomeScreen(),R.drawable.bookmark_img);
        adapter.addList(new HomeScreen(),R.drawable.avatar_img);

        pager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, pager,
                (tab, position) -> tab.setIcon(adapter.getIcon(position))
        ).attach();


    }
}