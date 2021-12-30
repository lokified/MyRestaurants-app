package com.moringaschool.myrestaurants.util;

import com.moringaschool.myrestaurants.models.Business;

import java.util.ArrayList;
import java.util.List;

public interface OnRestaurantSelectedListener {
    public void onRestaurantSelected(Integer position, List<Business> restaurants, String source);
}
