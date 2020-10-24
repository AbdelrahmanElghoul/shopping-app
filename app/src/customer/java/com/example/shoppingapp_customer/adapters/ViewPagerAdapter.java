package com.example.shoppingapp_customer.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private List<Integer> iconList=new ArrayList<>();
    private List<Fragment> fragmentList=new ArrayList<>();
    public Integer getIcon(int position) {
        return iconList.get(position);
    }

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    public void addList(Fragment fragments,int iconResource){
        fragmentList.add(fragments);

        iconList.add(iconResource);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}