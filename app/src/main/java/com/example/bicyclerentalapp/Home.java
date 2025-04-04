package com.example.bicyclerentalapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            DrawableCompat.setTint(
                    item.getIcon(),
                    getResources().getColor(android.R.color.black)
            );

            if(itemId == R.id.profile){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new pro2()).commit();
            } else if (itemId == R.id.home) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();
            } else if (itemId == R.id.notifications) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new NotificationFragment()).commit();
            } else if(itemId == R.id.cart){
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new CartFragment()).commit();
            }else if(itemId == R.id.search){
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new SearchFragment()).commit();
            }





            return true;
        });
    }
}
