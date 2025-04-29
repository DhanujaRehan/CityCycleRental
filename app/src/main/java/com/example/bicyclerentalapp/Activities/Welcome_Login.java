package com.example.bicyclerentalapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bicyclerentalapp.Methods.Home;
import com.example.bicyclerentalapp.R;

public class Welcome_Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome_login);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Welcome_Login.this, Home.class);
            startActivity(intent);
        }, 2000);
    }
}
