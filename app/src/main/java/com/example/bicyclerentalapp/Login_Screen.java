package com.example.bicyclerentalapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class Login_Screen extends AppCompatActivity {

    EditText email, password;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        SharedPreferences sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String loggedInUser = sharedPreferences.getString("logged_in_user_email", null);

        if (loggedInUser != null) {
            Intent intent = new Intent(Login_Screen.this, Welcome_Login.class);
            startActivity(intent);
            finish();
            return;
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailtext = email.getText().toString().trim();
                String passwordtext = password.getText().toString().trim();

                if (emailtext.isEmpty() || passwordtext.isEmpty()) {
                    Toast.makeText(Login_Screen.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailtext).matches()) {
                    Toast.makeText(Login_Screen.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    email.setText("");
                    return;
                }

                DBHelper dbhelper = new DBHelper(Login_Screen.this);
                boolean result = dbhelper.checkUser(emailtext, passwordtext);

                if (result) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("logged_in_user_email", emailtext);
                    editor.apply();

                    Toast.makeText(Login_Screen.this, "Login successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login_Screen.this, Welcome_Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login_Screen.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                    email.setText("");
                    password.setText("");
                }            }
        });

        TextView signup = findViewById(R.id.signupinlogin);
        signup.setOnClickListener(v -> {
            Intent intent = new Intent(Login_Screen.this, Signup_Screen.class);
            startActivity(intent);
        });
    }
}
