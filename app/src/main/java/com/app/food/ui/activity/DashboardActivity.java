package com.app.food.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.food.R;
import com.app.food.SplashActivity;
import com.app.food.databinding.ActivityDashboardBinding;
import com.app.food.databinding.ActivityRegisterBinding;
import com.app.food.ui.fragments.HomeFragment;
import com.app.food.ui.fragments.OrderListFragment;
import com.app.food.ui.fragments.ProfileFragment;
import com.app.food.utils.Constants;
import com.app.food.utils.CustomProgressDialog;
import com.app.food.utils.FirebaseRepo;
import com.app.food.utils.SharedPreferenceUtil;
import com.google.firebase.firestore.FirebaseFirestore;


public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding binding;

    private FirebaseFirestore firebaseFirestore;
    private SharedPreferenceUtil sharedPreferenceUtil;

    CustomProgressDialog customProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        firebaseFirestore = FirebaseRepo.createInstance();
        sharedPreferenceUtil = SharedPreferenceUtil.getInstance(this);
        customProgressDialog = new CustomProgressDialog(DashboardActivity.this, "Please wait....");
        setViews();
        handleClickListener();
    }

    private void setViews() {
        binding.tvFullName.setText("Hi, " + sharedPreferenceUtil.getUserDetails(Constants.FULL_NAME));
        binding.tvEmail.setText(sharedPreferenceUtil.getUserDetails(Constants.EMAIL));
    }


    private void handleClickListener() {
        HomeFragment homeFragment = new HomeFragment();
        openFragment(homeFragment);
        binding.ivHomeBottomMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment homeFragment = new HomeFragment();
                openFragment(homeFragment);
                binding.tvFullName.setText("Hi, " + sharedPreferenceUtil.getUserDetails(Constants.FULL_NAME));
                binding.tvEmail.setText(sharedPreferenceUtil.getUserDetails(Constants.EMAIL));

                binding.tvFullName.setVisibility(View.VISIBLE);
                binding.tvEmail.setVisibility(View.VISIBLE);
            }
        });
        binding.ivOrderBottomMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderListFragment orderListFragment = new OrderListFragment();
                openFragment(orderListFragment);
                binding.tvFullName.setText("Order List");

                binding.tvFullName.setVisibility(View.VISIBLE);
                binding.tvEmail.setVisibility(View.GONE);
            }
        });
        binding.ivProfileBottomMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment profileFragment = new ProfileFragment();
                openFragment(profileFragment);
                binding.tvFullName.setText("Profile");

                binding.tvFullName.setVisibility(View.VISIBLE);
                binding.tvEmail.setVisibility(View.GONE);
            }
        });
        binding.ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferenceUtil.setLoginAlready(false);
                Intent mainIntent = new Intent(DashboardActivity.this, LoginActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();
            }
        });
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.commonFrameLayout, fragment);
        transaction.commit();
    }

}