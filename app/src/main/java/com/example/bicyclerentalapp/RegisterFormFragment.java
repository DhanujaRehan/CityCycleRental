package com.example.bicyclerentalapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class RegisterFormFragment extends Fragment {

    private TextView btregister,rname;
    private DBHelper dbHelper;
    private EditText firstNameEditText, secondNameEditText, nicEditText, phoneNumberEditText, emailEditText, bdayEditText, usernameEditText, passwordEditText, confirmPasswordEditText;
    private RadioGroup genderRadioGroup;
    private Spinner roleSpinner;
    private ImageButton editImageButton;
    private ImageView imageProfile;
    private static final int PICK_IMAGE_REQUEST = 1;
    private byte[] imageBytes;
    private CheckBox check;

    private String originalFirstName, originalSecondName, originalNic, originalPhoneNumber, originalEmail, originalGender, originalBday, originalRole, originalUsername, originalPassword, originalConfirmPassword;
    private String loggedInUserEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_form, container, false);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String Rname = sharedPreferences.getString("logged_in_user_email", null);


        rname = view.findViewById(R.id.rname);
        firstNameEditText = view.findViewById(R.id.firstname);
        secondNameEditText = view.findViewById(R.id.secondname);
        nicEditText = view.findViewById(R.id.nic);
        phoneNumberEditText = view.findViewById(R.id.phonenumber);
        emailEditText = view.findViewById(R.id.email);
        bdayEditText = view.findViewById(R.id.bday);
        usernameEditText = view.findViewById(R.id.username);
        passwordEditText = view.findViewById(R.id.password);
        confirmPasswordEditText = view.findViewById(R.id.confirmpassword);
        genderRadioGroup = view.findViewById(R.id.radiogroup);
        roleSpinner = view.findViewById(R.id.spinner);
        btregister = view.findViewById(R.id.btregister);
        check = view.findViewById(R.id.checkBox);
        editImageButton = view.findViewById(R.id.editImage);
        imageProfile = view.findViewById(R.id.imageprofile);

        dbHelper = new DBHelper(getContext());

        if (Rname != null) {
            rname.setText(Rname);
        } else {
            rname.setText("User not logged in");
        }

        return view;
    }

    @SuppressLint("Range")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        loggedInUserEmail = sharedPreferences.getString("logged_in_user_email", null);

        if (loggedInUserEmail == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("RegisterFormFragment", "Logged-in user email: " + loggedInUserEmail);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DBHelper.TABLE_USERS,
                null, //okkoma coulmns
                DBHelper.COL_EMAIL + " = ?",
                new String[]{loggedInUserEmail},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {

            originalFirstName = cursor.getString(cursor.getColumnIndex(DBHelper.COL_FIRSTNAME));
            originalSecondName = cursor.getString(cursor.getColumnIndex(DBHelper.COL_SECONDNAME));
            originalNic = cursor.getString(cursor.getColumnIndex(DBHelper.COL_NIC));
            originalPhoneNumber = cursor.getString(cursor.getColumnIndex(DBHelper.COL_PHONENUMBER));
            originalEmail = cursor.getString(cursor.getColumnIndex(DBHelper.COL_EMAIL));
            originalGender = cursor.getString(cursor.getColumnIndex(DBHelper.COL_GENDER));
            originalBday = cursor.getString(cursor.getColumnIndex(DBHelper.COL_BDAY));
            originalRole = cursor.getString(cursor.getColumnIndex(DBHelper.COL_ROLE));
            originalUsername = cursor.getString(cursor.getColumnIndex(DBHelper.COL_USERNAME));
            originalPassword = cursor.getString(cursor.getColumnIndex(DBHelper.COL_PASSWORD));
            originalConfirmPassword = cursor.getString(cursor.getColumnIndex(DBHelper.COL_COMFIRMPASSWORD));

            firstNameEditText.setText(originalFirstName);
            secondNameEditText.setText(originalSecondName);
            nicEditText.setText(originalNic);
            phoneNumberEditText.setText(originalPhoneNumber);
            emailEditText.setText(originalEmail);
            bdayEditText.setText(originalBday);
            usernameEditText.setText(originalUsername);
            passwordEditText.setText(originalPassword);
            confirmPasswordEditText.setText(originalConfirmPassword);

            if (originalGender != null) {
                if (originalGender.equals("Male")) {
                    genderRadioGroup.check(R.id.male);
                } else if (originalGender.equals("Female")) {
                    genderRadioGroup.check(R.id.female);
                }
            }

            if (originalRole != null) {
                int spinnerPosition = getSpinnerPosition(roleSpinner, originalRole);
                roleSpinner.setSelection(spinnerPosition);
            }

            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(DBHelper.COL_IMAGE));
            if (imageBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                imageProfile.setImageBitmap(bitmap);
            }

            cursor.close();
        } else {
            Toast.makeText(requireContext(), "No user data found", Toast.LENGTH_SHORT).show();
        }

        db.close();

        bdayEditText.setOnClickListener(v -> showDatePickerDialog());
        btregister.setOnClickListener(v -> updateUserData(loggedInUserEmail));
        editImageButton.setOnClickListener(v -> openImagePicker());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                imageProfile.setImageBitmap(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageBytes = stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateUserData(String loggedInUserEmail) {
        String currentFirstName = firstNameEditText.getText().toString();
        String currentSecondName = secondNameEditText.getText().toString();
        String currentNic = nicEditText.getText().toString();
        String currentPhoneNumber = phoneNumberEditText.getText().toString();
        String currentEmail = emailEditText.getText().toString();
        String currentBday = bdayEditText.getText().toString();
        String currentUsername = usernameEditText.getText().toString();
        String currentPassword = passwordEditText.getText().toString();
        String currentConfirmPassword = confirmPasswordEditText.getText().toString();
        String currentGender = ((RadioButton) getView().findViewById(genderRadioGroup.getCheckedRadioButtonId())).getText().toString();
        String currentRole = roleSpinner.getSelectedItem().toString();

        if (currentFirstName.isEmpty() || currentSecondName.isEmpty() || currentNic.isEmpty() || currentPhoneNumber.isEmpty() ||
                currentEmail.isEmpty() || currentBday.isEmpty() || currentUsername.isEmpty() || currentPassword.isEmpty() ||
                currentConfirmPassword.isEmpty() || currentGender.isEmpty() || currentRole.isEmpty()) {
            Toast.makeText(requireContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!check.isChecked()) {
            Toast.makeText(requireContext(), "Please confirm that the details are true", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();

        if (!currentFirstName.equals(originalFirstName)) {
            values.put(DBHelper.COL_FIRSTNAME, currentFirstName);
        }
        if (!currentSecondName.equals(originalSecondName)) {
            values.put(DBHelper.COL_SECONDNAME, currentSecondName);
        }
        if (!currentNic.equals(originalNic)) {
            values.put(DBHelper.COL_NIC, currentNic);
        }
        if (!currentPhoneNumber.equals(originalPhoneNumber)) {
            values.put(DBHelper.COL_PHONENUMBER, currentPhoneNumber);
        }
        if (!currentEmail.equals(originalEmail)) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentEmail).matches()) {
                Toast.makeText(requireContext(), "Invalid email format", Toast.LENGTH_SHORT).show();
                return;
            }
            values.put(DBHelper.COL_EMAIL, currentEmail);
        }
        if (!currentBday.equals(originalBday)) {
            values.put(DBHelper.COL_BDAY, currentBday);
        }
        if (!currentUsername.equals(originalUsername)) {
            values.put(DBHelper.COL_USERNAME, currentUsername);
        }
        if (!currentPassword.equals(originalPassword)) {
            values.put(DBHelper.COL_PASSWORD, currentPassword);
        }
        if (!currentConfirmPassword.equals(originalConfirmPassword)) {
            values.put(DBHelper.COL_COMFIRMPASSWORD, currentConfirmPassword);
        }
        if (!currentGender.equals(originalGender)) {
            values.put(DBHelper.COL_GENDER, currentGender);
        }
        if (!currentRole.equals(originalRole)) {
            values.put(DBHelper.COL_ROLE, currentRole);
        }
        if (imageBytes != null) {
            values.put(DBHelper.COL_IMAGE, imageBytes);
        }

        if (values.size() > 0) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int rowsUpdated = db.update(DBHelper.TABLE_USERS, values, DBHelper.COL_EMAIL + " = ?", new String[]{loggedInUserEmail});
            db.close();

            if (rowsUpdated > 0) {
                Toast.makeText(requireContext(), "User data updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to update user data", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "No changes detected", Toast.LENGTH_SHORT).show();
        }
    }

    private int getSpinnerPosition(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            bdayEditText.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    @Override
    public void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}

