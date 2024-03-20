package com.app.food.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.food.R;
import com.app.food.databinding.ListingItemsBinding;
import com.app.food.models.ListingResponse;
import com.app.food.ui.activity.OrderSuccessActivity;
import com.app.food.utils.OrderPlace;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ViewHolder> {
    private ListingItemsBinding binding;
    private Context context;
    private OrderPlace orderPlace;
    private List<ListingResponse> list;
    int tot_amount = 0;

    int count = 1;


    public ListingAdapter(Context context, List<ListingResponse> list, OrderPlace orderPlace) {
        this.context = context;
        this.orderPlace = orderPlace;
        this.list = list != null ? list : new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = ListingItemsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListingItemsBinding binding = ListingItemsBinding.bind(holder.itemView);


        binding.tvTitle.setText(list.get(position).getTitle());
        binding.tvDescription.setText(list.get(position).getDescription());
        binding.tvPrice.setText("₹" + list.get(position).getPrice());


        binding.ivThumNail.setImageResource(list.get(position).getImage());


        binding.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomPopup(context, list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(ListingItemsBinding itemView) {
            super(itemView.getRoot());
        }
    }

    private void showBottomPopup(Context context, ListingResponse listingResponse) {
        // Create the bottom popup dialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);

        // Inflate the layout for the bottom popup
        View bottomPopupView = LayoutInflater.from(context).inflate(R.layout.bottom_card, null);

        // Set the view for the bottom popup dialog
        bottomSheetDialog.setContentView(bottomPopupView);

        TextView tvTitle = bottomPopupView.findViewById(R.id.tvTitle);
        TextView tvDescription = bottomPopupView.findViewById(R.id.tvDescription);
        TextView tvPrice = bottomPopupView.findViewById(R.id.tvPrice);
        TextView tvMinus = bottomPopupView.findViewById(R.id.tvMinus);
        TextView tvCount = bottomPopupView.findViewById(R.id.tvCount);
        TextView tvPlus = bottomPopupView.findViewById(R.id.tvPlus);
        ImageView ivThumNail = bottomPopupView.findViewById(R.id.ivThumNail);
        Button btnOrderNow = bottomPopupView.findViewById(R.id.btnOrderNow);

        tvTitle.setText(listingResponse.getTitle());
        tvDescription.setText(listingResponse.getDescription());
        //  tvPrice.setText("₹" + listingResponse.getPrice());

        ivThumNail.setImageResource(listingResponse.getImage());
        tvCount.setText("" + count);
        tot_amount = count * Integer.parseInt(listingResponse.getPrice());
        tvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count > 1) {
                    count--;
                    tvCount.setText("" + count);
                    tot_amount = count * Integer.parseInt(listingResponse.getPrice());
                    tvPrice.setText("Total Amount: ₹" + tot_amount);
                }


            }
        });

        tvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                tvCount.setText("" + count);
                tot_amount = count * Integer.parseInt(listingResponse.getPrice());
                tvPrice.setText("Total Amount: ₹" + tot_amount);
            }
        });

        btnOrderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderPlace.onItemClick(listingResponse, tot_amount, count);

            }
        });

        // Show the bottom popup dialog
        bottomSheetDialog.show();
    }

}


