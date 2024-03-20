package com.app.food.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.app.food.R;
import com.app.food.databinding.OrderListingItemsBinding;
import com.app.food.models.AddOrder;
import com.app.food.utils.OrderPlace;

import java.util.ArrayList;
import java.util.List;

public class OrderListingAdapter extends RecyclerView.Adapter<OrderListingAdapter.ViewHolder> {
    private OrderListingItemsBinding binding;
    private Context context;
    private OrderPlace orderPlace;
    private List<AddOrder> list;
    int tot_amount = 0;

    int count = 1;


    public OrderListingAdapter(Context context, List<AddOrder> list,OrderPlace orderPlace) {
        this.context = context;
        this.orderPlace = orderPlace;
        this.list = list != null ? list : new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        binding = OrderListingItemsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderListingItemsBinding binding = OrderListingItemsBinding.bind(holder.itemView);


        binding.tvTitle.setText(list.get(position).getTitle());
        binding.tvDescription.setText(list.get(position).getDescription());
        binding.tvStatus.setText("Status: " + list.get(position).getStatus());
        binding.tvPrice.setText("₹" + list.get(position).getPrice());
        binding.tvQuantity.setText("Quantity: " + list.get(position).getCount());


        if (list.get(position).getId() == 1 || list.get(position).getId() == 7) {
            binding.ivThumNail.setImageResource(R.drawable.img5);
        } else if (list.get(position).getId() == 2 || list.get(position).getId() == 8 || list.get(position).getId() == 20) {
            binding.ivThumNail.setImageResource(R.drawable.img2);
        } else if (list.get(position).getId() == 4 || list.get(position).getId() == 10 || list.get(position).getId() == 16 || list.get(position).getId() == 22) {
            binding.ivThumNail.setImageResource(R.drawable.img3);
        } else if (list.get(position).getId() == 5 || list.get(position).getId() == 11 || list.get(position).getId() == 17 || list.get(position).getId() == 23) {
            binding.ivThumNail.setImageResource(R.drawable.img4);
        } else if (list.get(position).getId() == 6 || list.get(position).getId() == 18 || list.get(position).getId() == 24) {
            binding.ivThumNail.setImageResource(R.drawable.img6);
        } else if (list.get(position).getId() == 9 || list.get(position).getId() == 13) {
            binding.ivThumNail.setImageResource(R.drawable.img7);
        } else if (list.get(position).getId() == 12 || list.get(position).getId() == 14 || list.get(position).getId() == 19) {
            binding.ivThumNail.setImageResource(R.drawable.img8);
        } else if (list.get(position).getId() == 28 || list.get(position).getId() == 27 || list.get(position).getId() == 26 || list.get(position).getId() == 25) {
            binding.ivThumNail.setImageResource(R.drawable.img9);
        }

        binding.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderPlace.onItemClick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(OrderListingItemsBinding itemView) {
            super(itemView.getRoot());
        }
    }


}


