package com.app.food.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.app.food.R;
import com.app.food.adapter.ListingAdapter;
import com.app.food.databinding.ActivityListingBinding;
import com.app.food.models.AddOrder;
import com.app.food.models.AddUsers;
import com.app.food.models.ListingResponse;
import com.app.food.utils.Constants;
import com.app.food.utils.CustomProgressDialog;
import com.app.food.utils.FirebaseRepo;
import com.app.food.utils.OrderPlace;
import com.app.food.utils.SharedPreferenceUtil;
import com.app.food.utils.Tools;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ListingActivity extends AppCompatActivity implements OrderPlace {

    private static final String TAG = "ListingActivity";

    Long order_id = 0L;

    @Override
    public void onItemClick(ListingResponse listingResponse, int tot_amount, int count) {
        getLastUserInfo(listingResponse, tot_amount, count);
    }
    @Override
    public void onItemClick(AddOrder addOrder) {

    }

    private void getLastUserInfo(ListingResponse listingResponse, int tot_amount, int count) {
        customProgressDialog.show();
        CollectionReference collectionRef = firebaseFirestore.collection(Constants.ORDER_COLLECTION_NAME);


// Create a query to order the collection in descending order and limit to 1 document
        Query query = collectionRef.orderBy("order_id", Query.Direction.DESCENDING).limit(1);

// Perform the query
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                customProgressDialog.dismiss();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Access the last document
                        // String lastDocumentId = document.getId();
                        // Now you can work with the data in 'document'


                        Tools.logs(TAG, "User Details  " + document.getData());
                        Map<String, Object> documentData = document.getData();
                        if (documentData != null) {
                            // Access 'userid' and 'usertype' fields
                            order_id = (Long) documentData.get("order_id");
                            order_id = order_id + 1L;
                            addOrder(listingResponse, tot_amount, count);
                        }
                    }
                } else {
                    customProgressDialog.dismiss();
                    // Handle any errors
                    Tools.showToast(ListingActivity.this, "Error");
                }
            }
        });

    }

    private void addOrder(ListingResponse listingResponse, int tot_amount, int count) {
        long currentTimeInMillis = System.currentTimeMillis();
        Date currentDate = new Date(currentTimeInMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String formattedDate = sdf.format(currentDate);
        System.out.println("Current datetime: " + formattedDate);


        DocumentReference docRef = firebaseFirestore.collection(Constants.ORDER_COLLECTION_NAME).document(formattedDate); // Firestore database reference
        // Create a new user object
        AddOrder addOrder = new AddOrder(sharedPreferenceUtil.getUserId(), order_id, (long) listingResponse.getId(), listingResponse.getTitle(), listingResponse.getDescription(), String.valueOf(tot_amount), listingResponse.getOwner_name(), "Pending", (long) count,formattedDate);

        // Adding user information to Firestore
        docRef.set(addOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                customProgressDialog.dismiss();
                // User information added successfully
                Tools.logs(TAG, "User Order Placed successfully");
                Intent mainIntent = new Intent(ListingActivity.this, OrderSuccessActivity.class);
                mainIntent.putExtra(Constants.OWNER_NAME,listingResponse.getOwner_name());
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                // Tools.showToast(ListingActivity.this, "User Order Placed successfully");
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                customProgressDialog.dismiss();

                // Handle any errors that may occur
                Tools.logs(TAG, "Error adding user information" + e);
                Tools.showToast(ListingActivity.this, "Error adding user Order" + e);

            }
        });
    }

    private ActivityListingBinding binding;
    private FirebaseFirestore firebaseFirestore;
    private SharedPreferenceUtil sharedPreferenceUtil;

    CustomProgressDialog customProgressDialog;

    List<ListingResponse> modelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        binding = ActivityListingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        firebaseFirestore = FirebaseRepo.createInstance();
        sharedPreferenceUtil = SharedPreferenceUtil.getInstance(this);
        customProgressDialog = new CustomProgressDialog(ListingActivity.this, "Please wait....");
        getIntentData();
    }

    private void getIntentData() {
        String owner_name = getIntent().getStringExtra(Constants.OWNER_NAME);
        assert owner_name != null;
        if (owner_name.equals(Constants.OWNER_1)) {
            binding.tvTitle.setText("Tulsi Vihar Veg Restaurant");
            modelList.add(new ListingResponse(1, "Veg Special Thali", "Paneer Butter Mix+Veg+Pulao", "199", R.drawable.img5, owner_name));
            modelList.add(new ListingResponse(2, "Cheese Pav Bhaji", "Kandivali West, Mumbai", "137", R.drawable.img2, owner_name));
            modelList.add(new ListingResponse(3, "Chinese Veg Crunch by Tulsi Vihar", "North Indian, Mughlai,Chinese", "250", R.drawable.restaurant1, owner_name));
            modelList.add(new ListingResponse(4, "Veg Cheese Grilled Sandwich", "Kandivali West, Mumbai.", "145", R.drawable.img3, owner_name));
            modelList.add(new ListingResponse(5, "Veg Deluxe Thali", "Paneer Butter Masala+Dal Tadka", "175", R.drawable.img4, owner_name));
            modelList.add(new ListingResponse(6, "Paneer Butter Masala", "Full Plat Paneer Butter Masala", "140", R.drawable.img6, owner_name));
            modelList.add(new ListingResponse(28, "Kolhapuri Maggi Masala ", "Marathi Recipe", "120", R.drawable.img9, owner_name));
        } else if (owner_name.equals(Constants.OWNER_2)) {
            binding.tvTitle.setText("Swaraj Family Restaurant");
            modelList.add(new ListingResponse(27, "Kolhapuri Maggi Masala ", "Marathi Recipe", "120", R.drawable.img9, owner_name));
            modelList.add(new ListingResponse(7, "Veg Special Thali", "Paneer Butter Mix+Veg+Pulao", "199", R.drawable.img5, owner_name));
            modelList.add(new ListingResponse(8, "Cheese Pav Bhaji", "Kandivali West, Mumbai", "137", R.drawable.img2, owner_name));
            modelList.add(new ListingResponse(9, "Chicken Tikka Masala", "Most popular Indian chicken recipes", "360", R.drawable.img7, owner_name));
            modelList.add(new ListingResponse(10, "Veg Cheese Grilled Sandwich", "Kandivali West, Mumbai.", "145", R.drawable.img3, owner_name));
            modelList.add(new ListingResponse(11, "Veg Deluxe Thali", "Paneer Butter Masala+Dal Tadka", "175", R.drawable.img4, owner_name));
            modelList.add(new ListingResponse(12, "Maharashtrian Mutton Curry", "1/2 kg goat meat", "260", R.drawable.img8, owner_name));
        } else if (owner_name.equals(Constants.OWNER_3)) {
            binding.tvTitle.setText("The Food Town");
            modelList.add(new ListingResponse(13, "Chicken Tikka Masala", "Most popular Indian chicken recipes", "350", R.drawable.img7, owner_name));
            modelList.add(new ListingResponse(14, "Maharashtrian Mutton Curry", "1/2 kg goat meat", "260", R.drawable.img8, owner_name));
            modelList.add(new ListingResponse(15, "Chinese Veg Crunch by Food Town", "North Indian, Mughlai,Chinese", "250", R.drawable.restaurant1, owner_name));
            modelList.add(new ListingResponse(16, "Veg Cheese Grilled Sandwich", "Kandivali West, Mumbai.", "145", R.drawable.img3, owner_name));
            modelList.add(new ListingResponse(26, "Kolhapuri Maggi Masala ", "Marathi Recipe", "120", R.drawable.img9, owner_name));
            modelList.add(new ListingResponse(17, "Veg Deluxe Thali", "Paneer Butter Masala+Dal Tadka", "175", R.drawable.img4, owner_name));
            modelList.add(new ListingResponse(18, "Paneer Butter Masala", "Full Plat Paneer Butter Masala", "140", R.drawable.img6, owner_name));
        } else if (owner_name.equals(Constants.OWNER_4)) {
            binding.tvTitle.setText("Shri Nidhi Restaurant");
            modelList.add(new ListingResponse(19, "Maharashtrian Mutton Curry", "1/2 kg goat meat", "260", R.drawable.img8, owner_name));
            modelList.add(new ListingResponse(20, "Cheese Pav Bhaji", "Kandivali West, Mumbai", "137", R.drawable.img2, owner_name));
            modelList.add(new ListingResponse(21, "Chinese Veg Crunch by Shri Nidhi", "North Indian, Mughlai,Chinese", "250", R.drawable.restaurant1, owner_name));
            modelList.add(new ListingResponse(22, "Veg Cheese Grilled Sandwich", "Kandivali West, Mumbai.", "145", R.drawable.img3, owner_name));
            modelList.add(new ListingResponse(23, "Veg Deluxe Thali", "Paneer Butter Masala+Dal Tadka", "175", R.drawable.img4, owner_name));
            modelList.add(new ListingResponse(24, "Paneer Butter Masala", "Full Plat Paneer Butter Masala", "140", R.drawable.img6, owner_name));
            modelList.add(new ListingResponse(25, "Kolhapuri Maggi Masala ", "Marathi Recipe", "120", R.drawable.img9, owner_name));
        }


        setAdapter();

    }

    private void setAdapter() {

        ListingAdapter listingAdapter = new ListingAdapter(ListingActivity.this, modelList, this);
        binding.rvListing.setAdapter(listingAdapter);
    }
}