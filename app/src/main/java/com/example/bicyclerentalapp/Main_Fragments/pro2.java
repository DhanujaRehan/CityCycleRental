package com.example.bicyclerentalapp.Main_Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bicyclerentalapp.Database.DBHelper;
import com.example.bicyclerentalapp.Activities.Login_Screen;
import com.example.bicyclerentalapp.R;
import com.example.bicyclerentalapp.Other_Fragments.RegisterFormFragment;
import com.example.bicyclerentalapp.Methods.User;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class pro2 extends Fragment {
    private TextView firstname1, secondname1, firstname2, textView46;
    private ImageView propic;
    private DBHelper dbHelper;
    private Button logout;
    private String userEmail;

    public pro2() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString("logged_in_user_email", null);

        if (userEmail == null) {
            Toast.makeText(getContext(), "User email not found in SharedPreferences!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pro2, container, false);

        logout = view.findViewById(R.id.logout);
        firstname1 = view.findViewById(R.id.firstname1);
        secondname1 = view.findViewById(R.id.secondname1);
        firstname2 = view.findViewById(R.id.firstname2);
        textView46 = view.findViewById(R.id.textView46);
        propic = view.findViewById(R.id.propic);



        logout.setOnClickListener(go ->
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes", (dialog, which) -> {

                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_session", getActivity().MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();

                            Toast.makeText(getContext(), "User logged out successfully!", Toast.LENGTH_SHORT).show();


                            Intent intent = new Intent(getActivity(), Login_Screen.class);
                            startActivity(intent);
                            getActivity().finish();
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show()
        );

        dbHelper = new DBHelper(getContext());


        if (userEmail != null) {
            User user = dbHelper.getUserDetails(userEmail);

            if (user != null) {

                firstname1.setText(user.getFirstName());
                secondname1.setText(user.getSecondName());
                firstname2.setText(user.getFirstName() + " " + user.getSecondName());
                textView46.setText("Contact Number : " + user.getPhoneNumber() +
                        "\nEmail : " + user.getEmail() +
                        "\nNIC No : " + user.getNic());


                if (user.getImage() != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(user.getImage(), 0, user.getImage().length);
                    propic.setImageBitmap(bitmap);
                    
                }
            } else {
                Toast.makeText(getContext(), "User details not found in database!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "User email is null!", Toast.LENGTH_SHORT).show();
        }


        TextView editProfileText = view.findViewById(R.id.editprofile);
        editProfileText.setOnClickListener(view1 -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, new RegisterFormFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;



    }
}