package com.example.bicyclerentalapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bicyclerentalapp.Database.DBHelper;
import com.example.bicyclerentalapp.R;

public class Signup_Screen extends AppCompatActivity {

    EditText username, email, password, confirmpassword;
    TextView signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup_screen);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        signup = findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usernameText = username.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                String passwordText = password.getText().toString().trim();
                String confirmPasswordText = confirmpassword.getText().toString().trim();

                if (usernameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() || confirmPasswordText.isEmpty()) {
                    Toast.makeText(Signup_Screen.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (passwordText.length() < 6) {
                    Toast.makeText(Signup_Screen.this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!passwordText.equals(confirmPasswordText)) {
                    Toast.makeText(Signup_Screen.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    confirmpassword.setText("");
                    password.setText("");
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
                    Toast.makeText(Signup_Screen.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    email.setText("");
                    return;
                }


                DBHelper dbhelper = new DBHelper(Signup_Screen.this);
                boolean result = dbhelper.insertUser(usernameText, emailText,passwordText,confirmPasswordText);

                if (result) {
                    Toast.makeText(Signup_Screen.this, "Signup successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Signup_Screen.this, Welcome_Sinup.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(Signup_Screen.this, "Signup failed", Toast.LENGTH_SHORT).show();
                    username.setText("");
                    email.setText("");
                    password.setText("");
                    confirmpassword.setText("");

                }
            }
        });

        TextView signup = findViewById(R.id.loginbuttonin);
        signup.setOnClickListener(v -> {
            Intent intent = new Intent(Signup_Screen.this, Login_Screen.class);
            startActivity(intent);
        });

    }


}