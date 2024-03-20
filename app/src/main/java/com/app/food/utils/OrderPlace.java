package com.app.food.utils;

import com.app.food.models.AddOrder;
import com.app.food.models.ListingResponse;

public interface OrderPlace {
    void onItemClick(ListingResponse listingResponse ,int tot_amount,int count);
    void onItemClick(AddOrder addOrder);
}