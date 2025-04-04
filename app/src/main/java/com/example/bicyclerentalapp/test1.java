package com.example.bicyclerentalapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class test1 extends Dialog {

    private final Context context;
    private final String bikeName;
    private final String bikePrice;
    private final Drawable bikeImage;
    private final int pricePerDay;

    private ImageView popupBikeImage;
    private TextView popupBikeName;
    private TextView popupBikePrice;
    private TextView rentalDateText;
    private TextView returnDateText;
    private TextView totalPriceText;
    private ImageButton rentalDatePickButton;
    private ImageButton returnDatePickButton;
    private Button confirmRentButton;
    private Button cancelButton;

    private Calendar rentalDate = Calendar.getInstance();
    private Calendar returnDate = Calendar.getInstance();
    private boolean isRentalDateSelected = false;
    private boolean isReturnDateSelected = false;

    public interface RentalDialogListener {
        void onRentalConfirmed(String bikeName, Date rentalDate, Date returnDate, int totalPrice);
    }

    private RentalDialogListener listener;

    public test1(@NonNull Context context, String bikeName, String bikePrice, Drawable bikeImage, RentalDialogListener listener) {
        super(context);
        this.context = context;
        this.bikeName = bikeName;
        this.bikePrice = bikePrice;
        this.bikeImage = bikeImage;
        this.listener = listener;

        // Extract numeric price value
        String priceStr = bikePrice.replaceAll("[^0-9]", "");
        this.pricePerDay = Integer.parseInt(priceStr);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test1);

        // Initialize views
        initViews();

        // Set data
        setData();

        // Set listeners
        setListeners();
    }

    private void initViews() {
        popupBikeImage = findViewById(R.id.popupBikeImage);
        popupBikeName = findViewById(R.id.popupBikeName);
        popupBikePrice = findViewById(R.id.popupBikePrice);
        rentalDateText = findViewById(R.id.rentalDateText);
        returnDateText = findViewById(R.id.returnDateText);
        totalPriceText = findViewById(R.id.totalPriceText);
        rentalDatePickButton = findViewById(R.id.rentalDatePickButton);
        returnDatePickButton = findViewById(R.id.returnDatePickButton);
        confirmRentButton = findViewById(R.id.confirmRentButton);
        cancelButton = findViewById(R.id.cancelButton);
    }

    private void setData() {
        popupBikeImage.setImageDrawable(bikeImage);
        popupBikeName.setText(bikeName);
        popupBikePrice.setText(bikePrice);

        // Set today's date as the minimum date for rental
        rentalDate = Calendar.getInstance();

        // Set tomorrow as default return date
        returnDate = Calendar.getInstance();
        returnDate.add(Calendar.DAY_OF_MONTH, 1);
    }

    private void setListeners() {
        rentalDatePickButton.setOnClickListener(v -> showRentalDatePicker());
        rentalDateText.setOnClickListener(v -> showRentalDatePicker());

        returnDatePickButton.setOnClickListener(v -> showReturnDatePicker());
        returnDateText.setOnClickListener(v -> showReturnDatePicker());

        confirmRentButton.setOnClickListener(v -> confirmRental());

        cancelButton.setOnClickListener(v -> dismiss());
    }

    private void showRentalDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {
                    rentalDate.set(Calendar.YEAR, year);
                    rentalDate.set(Calendar.MONTH, month);
                    rentalDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    isRentalDateSelected = true;
                    updateRentalDateText();

                    // Make sure return date is after rental date
                    if (returnDate.before(rentalDate)) {
                        returnDate.setTime(rentalDate.getTime());
                        returnDate.add(Calendar.DAY_OF_MONTH, 1);
                        isReturnDateSelected = true;
                        updateReturnDateText();
                    }

                    calculateTotalPrice();
                    updateConfirmButtonState();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set min date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showReturnDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(Calendar.YEAR, year);
                    selectedDate.set(Calendar.MONTH, month);
                    selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    // Check if return date is before rental date
                    if (selectedDate.before(rentalDate)) {
                        Toast.makeText(context, "Return date must be after rental date", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    returnDate = selectedDate;
                    isReturnDateSelected = true;
                    updateReturnDateText();
                    calculateTotalPrice();
                    updateConfirmButtonState();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Set min date to rental date or today if rental date not selected
        datePickerDialog.getDatePicker().setMinDate(isRentalDateSelected ?
                rentalDate.getTimeInMillis() : System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void updateRentalDateText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        rentalDateText.setText(dateFormat.format(rentalDate.getTime()));
    }

    private void updateReturnDateText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        returnDateText.setText(dateFormat.format(returnDate.getTime()));
    }

    private void calculateTotalPrice() {
        if (isRentalDateSelected && isReturnDateSelected) {
            long diffInMillis = returnDate.getTimeInMillis() - rentalDate.getTimeInMillis();
            long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);

            // Ensure at least one day is charged
            diffInDays = Math.max(1, diffInDays);

            int totalPrice = (int) (pricePerDay * diffInDays);
            totalPriceText.setText(totalPrice + "LKR");
        }
    }

    private void updateConfirmButtonState() {
        confirmRentButton.setEnabled(isRentalDateSelected && isReturnDateSelected);
        confirmRentButton.setAlpha(confirmRentButton.isEnabled() ? 1.0f : 0.5f);
    }

    private void confirmRental() {
        if (!isRentalDateSelected || !isReturnDateSelected) {
            Toast.makeText(context, "Please select both rental and return dates", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate total price
        long diffInMillis = returnDate.getTimeInMillis() - rentalDate.getTimeInMillis();
        long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillis);
        diffInDays = Math.max(1, diffInDays);
        int totalPrice = (int) (pricePerDay * diffInDays);

        // Notify listener
        if (listener != null) {
            listener.onRentalConfirmed(
                    bikeName,
                    rentalDate.getTime(),
                    returnDate.getTime(),
                    totalPrice
            );
        }

        // Close dialog
        dismiss();
    }
}