package com.moringaschool.myrestaurants.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.moringaschool.myrestaurants.models.Business;
import com.moringaschool.myrestaurants.ui.RestaurantDetailFragment;

import java.util.List;

public class RestaurantPagerAdapter extends FragmentPagerAdapter {

    private List<Business> mRestaurant;

    public RestaurantPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Business> restaurants) {
        super(fm, behavior);
        mRestaurant = restaurants;
    }

    @Override
    public Fragment getItem(int position) {
        return RestaurantDetailFragment.newInstance(mRestaurant, position);
    }

    @Override
    public int getCount() {
        return mRestaurant.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mRestaurant.get(position).getName();
    }
}
