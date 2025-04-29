package com.example.bicyclerentalapp.Main_Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.bicyclerentalapp.R;
import com.example.bicyclerentalapp.Other_Fragments.ShopFragment;
import com.example.bicyclerentalapp.Adapters.Adapter;
import com.example.bicyclerentalapp.Adapters.Bicycle;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private List<Bicycle> bicycleList;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private SearchView searchView;

    public SearchFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString("param1");
            String mParam2 = getArguments().getString("param2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_search, container, false);
        TextView shopstext = view.findViewById(R.id.shoptext);

        shopstext.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new ShopFragment())
                    .addToBackStack(null)
                    .commit();

        });


        recyclerView = view.findViewById(R.id.recycleview);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);


        bicycleList = new ArrayList<>();
        bicycleList.add(new Bicycle("Red Lady", "350LKR / Day", R.drawable.red));
        bicycleList.add(new Bicycle("Blue Thunder", "650LKR / Day", R.drawable.haya));
        bicycleList.add(new Bicycle("Green Speed", "550LKR / Day", R.drawable.green));
        bicycleList.add(new Bicycle("Yellow Flash", "550LKR / Day", R.drawable.dahahathara));
        bicycleList.add(new Bicycle("White Ghost", "350LKR / Day", R.drawable.white));
        bicycleList.add(new Bicycle("Falcon", "450LKR / Day", R.drawable.paha));
        bicycleList.add(new Bicycle("Blue Sky", "550LKR / Day", R.drawable.haya));
        bicycleList.add(new Bicycle("Smoker", "850LKR / Day", R.drawable.ekolaha));
        bicycleList.add(new Bicycle("Thunder", "950LKR / Day", R.drawable.eka));
        bicycleList.add(new Bicycle("Greeny Forest", "750LKR / Day", R.drawable.green));
        bicycleList.add(new Bicycle("Blue Berry", "650LKR / Day", R.drawable.deka));
        bicycleList.add(new Bicycle("Muddy Buddy", "650LKR / Day", R.drawable.namaya));


        adapter = new Adapter(getContext(), bicycleList);
        recyclerView.setAdapter(adapter);


        searchView = view.findViewById(R.id.searchView);
        setupSearchView();

        return view;
    }

    private FragmentActivity requiredActivity() {
        return null;
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filter(newText);
                return true;
            }
        });
    }

    private void filter(String text) {
        List<Bicycle> filteredList = new ArrayList<>();

        if (text.isEmpty()) {

            filteredList.addAll(bicycleList);
        } else {

            for (Bicycle bicycle : bicycleList) {
                if (bicycle.getName().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(bicycle);
                }
            }
        }

        adapter.setSearchList(filteredList);
    }
}