package com.example.bicyclerentalapp.Main_Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bicyclerentalapp.Database.DBHelper;
import com.example.bicyclerentalapp.R;
import com.example.bicyclerentalapp.Adapters.CartAdapter;
import com.example.bicyclerentalapp.Adapters.Rental;

import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView cartRecyclerView;
    private TextView emptyCartTextView;
    private DBHelper dbHelper;
    private CartAdapter cartAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
        emptyCartTextView = view.findViewById(R.id.emptyCartTextView);

        dbHelper = new DBHelper(getContext());


        List<Rental> rentalList = dbHelper.getAllRentals();

        if (rentalList.isEmpty()) {
            emptyCartTextView.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
        } else {
            emptyCartTextView.setVisibility(View.GONE);
            cartRecyclerView.setVisibility(View.VISIBLE);

            cartAdapter = new CartAdapter(getContext(), rentalList);
            cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            cartRecyclerView.setAdapter(cartAdapter);
        }

        return view;
    }
}