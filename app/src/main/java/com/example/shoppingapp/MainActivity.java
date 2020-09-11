package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.example.shoppingapp.adapters.ViewPagerAdapter;
import com.example.shoppingapp.fragments.HomeScreen;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager2 pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        ViewPagerAdapter adapter=new ViewPagerAdapter(this);
        adapter.addList(new HomeScreen(),R.drawable.home_img);
        adapter.addList(new HomeScreen(),R.drawable.shopping_card_img);
        adapter.addList(new HomeScreen(),R.drawable.bookmark_img);
        adapter.addList(new HomeScreen(),R.drawable.avatar_img);
        pager.setAdapter(adapter);
//        tabs.setupWithViewPager(pages);
        new TabLayoutMediator(tabLayout, pager,
                (tab, position) -> tab.setIcon(adapter.getIcon(position))
        ).attach();


      /* pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
           @Override
           public void onPageSelected(int position) {
               super.onPageSelected(position);
               Drawable unwrappedDrawable = AppCompatResources.getDrawable(MainActivity.this, adapter.getIcon(position));
               Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
               DrawableCompat.setTint(wrappedDrawable, Color.alpha(R.color.selectedIcon));
           }
       });*/

    }
}