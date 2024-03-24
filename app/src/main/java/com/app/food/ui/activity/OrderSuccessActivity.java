package com.app.food.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.app.food.R;
import com.app.food.utils.Constants;

public class OrderSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);
        TextView tvOrderDescription = (TextView) findViewById(R.id.tvOrderDescription);
        String owner_name = getIntent().getStringExtra(Constants.OWNER_NAME);
        if (owner_name.equals(Constants.OWNER_1)) {
            tvOrderDescription.setText("Order has been placed from Tulsi Vihar Veg Restaurant");
        } else if (owner_name.equals(Constants.OWNER_2)) {
            tvOrderDescription.setText("Order has been placed from Swaraj Family Restaurant");
        } else if (owner_name.equals(Constants.OWNER_3)) {
            tvOrderDescription.setText("Order has been placed from The Food Town");
        } else if (owner_name.equals(Constants.OWNER_4)) {
            tvOrderDescription.setText("Order has been placed from Shri Nidhi Restaurant");
        }
    }
}