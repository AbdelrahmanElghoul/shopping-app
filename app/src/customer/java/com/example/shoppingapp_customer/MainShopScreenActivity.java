package com.example.shoppingapp_customer;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.transition.Transition;

import com.example.shoppingapp.R;
import com.example.shoppingapp_customer.adapters.ViewPagerAdapter;
import com.example.shoppingapp_customer.fragments.CartScreen;
import com.example.shoppingapp_customer.fragments.HomeScreen;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.transition.platform.MaterialFade;


import timber.log.Timber;

public class MainShopScreenActivity extends AppCompatActivity {

    ViewPager2 pager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Transition transition=new MaterialFade();
        getWindow().setExitTransition(transition);

        setContentView(R.layout.activity_main_shop_screen);


        BindView();

        ViewPagerAdapter adapter=new ViewPagerAdapter(this);
        adapter.addList(new HomeScreen(),R.drawable.home_img);
        adapter.addList(new CartScreen(),R.drawable.shopping_cart_img);
        adapter.addList(new HomeScreen(),R.drawable.bookmark_img);
        adapter.addList(new HomeScreen(),R.drawable.avatar_img);

        Timber.d(String.valueOf(pager==null));
        pager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, pager,
                (tab, position) -> tab.setIcon(adapter.getIcon(position))
        ).attach();

    }

    private void BindView() {
        pager=findViewById(R.id.view_pager);
        tabLayout=findViewById(R.id.tab_layout);
    }


}