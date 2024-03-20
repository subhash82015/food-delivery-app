package com.app.food.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.food.R;
import com.app.food.adapter.OrderListingAdapter;
import com.app.food.databinding.FragmentHomeBinding;
import com.app.food.databinding.FragmentOrderListBinding;
import com.app.food.models.AddOrder;
import com.app.food.models.ListingResponse;
import com.app.food.utils.Constants;
import com.app.food.utils.CustomProgressDialog;
import com.app.food.utils.FirebaseRepo;
import com.app.food.utils.OrderPlace;
import com.app.food.utils.SharedPreferenceUtil;
import com.app.food.utils.Tools;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderListFragment extends Fragment implements OrderPlace {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private final String TAG = "OrderListFragment";

    private FragmentOrderListBinding binding;
    CustomProgressDialog customProgressDialog;
    List<AddOrder> modelList = new ArrayList<>();

    private FirebaseFirestore firebaseFirestore;
    private SharedPreferenceUtil sharedPreferenceUtil;

    public OrderListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderListFragment newInstance(String param1, String param2) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentOrderListBinding.inflate(inflater, container, false);
        init();
        return binding.getRoot();
    }

    private void init() {
        firebaseFirestore = FirebaseRepo.createInstance();
        sharedPreferenceUtil = SharedPreferenceUtil.getInstance(getActivity());

        customProgressDialog = new CustomProgressDialog(getActivity(), "Please wait....");
        if (sharedPreferenceUtil.getUserType() == 1) {
            getOrderListAdmin();
        } else {
            getOrderList();
        }
    }

    private void getOrderList() {
        modelList.clear();
        customProgressDialog.show();
        Query collectionRef = firebaseFirestore.collection(Constants.ORDER_COLLECTION_NAME).whereEqualTo("userid", sharedPreferenceUtil.getUserId()); // Replace with your collection name
        // CollectionReference collectionRef = firebaseFirestore.collection(Constants.ORDER_COLLECTION_NAME).whereEqualTo("userid",sharedPreferenceUtil.getUserId());

        collectionRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    customProgressDialog.dismiss();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        customProgressDialog.dismiss();
                        if (documentSnapshot.exists()) {
                            AddOrder model = documentSnapshot.toObject(AddOrder.class);
                            modelList.add(model);
                            setAdapter1();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    customProgressDialog.dismiss();
                    // Handle any errors that occur while fetching documents
                    Tools.logs(TAG, "Error getting documents: " + e);
                });

    }

    private void getOrderListAdmin() {
        modelList.clear();
        customProgressDialog.show();
        Query collectionRef = firebaseFirestore.collection(Constants.ORDER_COLLECTION_NAME); // Replace with your collection name
        // CollectionReference collectionRef = firebaseFirestore.collection(Constants.ORDER_COLLECTION_NAME).whereEqualTo("userid",sharedPreferenceUtil.getUserId());

        collectionRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    customProgressDialog.dismiss();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        customProgressDialog.dismiss();
                        if (documentSnapshot.exists()) {
                            AddOrder model = documentSnapshot.toObject(AddOrder.class);
                            modelList.add(model);
                            setAdapter1();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    customProgressDialog.dismiss();
                    // Handle any errors that occur while fetching documents
                    Tools.logs(TAG, "Error getting documents: " + e);
                });

    }


    private void setAdapter1() {
        if (modelList != null && modelList.size() > 0) {
            OrderListingAdapter orderListingAdapter = new OrderListingAdapter(getActivity(), modelList, this);
            binding.rvList.setAdapter(orderListingAdapter);
            binding.rvList.setVisibility(View.VISIBLE);
        } else {
            binding.rvList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(ListingResponse listingResponse, int tot_amount, int count) {

    }

    @Override
    public void onItemClick(AddOrder addOrder) {
        if (sharedPreferenceUtil.getUserType() == 1) {
            showBottomPopup(addOrder);
        }
    }

    private void showBottomPopup(AddOrder addOrder) {
        // Create the bottom popup dialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());

        // Inflate the layout for the bottom popup
        View bottomPopupView = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_status, null);

        // Set the view for the bottom popup dialog
        bottomSheetDialog.setContentView(bottomPopupView);

        Spinner spType = bottomPopupView.findViewById(R.id.spType);

        Button btnChangeStatus = bottomPopupView.findViewById(R.id.btnChangeStatus);

        btnChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orderType = spType.getSelectedItem().toString();
                updateOrderStatus(addOrder, orderType);
            }
        });

        // Show the bottom popup dialog
        bottomSheetDialog.show();
    }

    private void updateOrderStatus(AddOrder addOrder, String status) {
        customProgressDialog.show();
        // Get a reference to the document you want to update
        DocumentReference docRef = firebaseFirestore.collection(Constants.ORDER_COLLECTION_NAME).document(addOrder.getDoc_id());

        Map<String, Object> updates = new HashMap<>();
        updates.put("status", status);


        docRef.update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        customProgressDialog.dismiss();
                        getOrderList();
                        // Updated successfully
                        Tools.showToast(getActivity(), "Order Updated");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        customProgressDialog.dismiss();

                        Tools.showToast(getActivity(), "Error while Order Updation");
                        // Handle any errors
                    }
                });


    }


}