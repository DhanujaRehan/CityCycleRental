package com.example.bicyclerentalapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.bicyclerentalapp.adapters.Rental;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {


    public static final String COL_COMFIRMPASSWORD = "Confirmed_Password";
    private static final String DATABASE_NAME = "PaddleGo.db";
    private static final int DATABASE_VERSION = 4;
    static final String TABLE_USERS = "User";
    private static final String COL_ID = "id";
    static final String COL_FIRSTNAME = "First_Name";
    static final String COL_SECONDNAME = "Second_Name";
    static final String COL_NIC = "NIC";
    static final String COL_PHONENUMBER = "Phone_Number";
    static final String COL_EMAIL = "Email";
    public static final String COL_GENDER = "Gender" ;
    public static final String COL_BDAY = "Date_of_Birth";
    public static final String COL_ROLE = "Role";
    public static final String COL_USERNAME = "Username";
    public static final String COL_PASSWORD = "Password";
    static final String COL_IMAGE = "Image";
    public static final String TABLE_RENTALS = "Rents";
    public static final String COL_BIKE = "Bike";
    public static final String COL_TIME = "Time";
    public static final String COL_PRICE = "Price";
    private static final String COLUMN_IMAGE = "Image";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DBHelper", "onCreate called: Creating User table");
        String createTable =
                "CREATE TABLE " + TABLE_USERS + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_FIRSTNAME + " TEXT, " +
                        COL_SECONDNAME + " TEXT, " +
                        COL_NIC + " TEXT, " +
                        COL_PHONENUMBER + " TEXT UNIQUE, " +
                        COL_EMAIL + " TEXT UNIQUE, " +
                        COL_GENDER + " TEXT, " +
                        COL_BDAY + " TEXT, " +
                        COL_ROLE + " TEXT, " +
                        COL_USERNAME + " TEXT UNIQUE, " +
                        COL_PASSWORD + " TEXT, " +
                        COL_COMFIRMPASSWORD + " TEXT, " +
                        COL_IMAGE + " BLOB)";
        db.execSQL(createTable);

                String createRentsTable =
                "CREATE TABLE " + TABLE_RENTALS + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_BIKE + " TEXT, " +
                        COL_TIME + " INT, " +
                        COL_PRICE + " DOUBLE, " +
                        COLUMN_IMAGE + " BLOB)";
        db.execSQL(createRentsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RENTALS);
        onCreate(db);
    }

    public boolean insertRentals(String bikenameText, int timeText, double priceText, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BIKE, bikenameText);
        values.put(COL_TIME, timeText);
        values.put(COL_PRICE, priceText);
        values.put(COLUMN_IMAGE, image);

        try {
            long result = db.insert(TABLE_RENTALS, null, values);
            if (result == -1) {
                Log.e("DBHelper", "Failed to insert rental: " + bikenameText);
                return false;
            } else {
                Log.d("DBHelper", "Rental inserted successfully: " + bikenameText);
                return true;
            }
        } catch (Exception e) {
            Log.e("DBHelper", "Error inserting rental: ", e);
            return false;
        } finally {
            db.close();
        }
    }

    public boolean insertUser(String usernameText, String emailText, String passwordText, String confirmPasswordText) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, usernameText);
        values.put(COL_EMAIL, emailText);
        values.put(COL_PASSWORD, passwordText);
        values.put(COL_COMFIRMPASSWORD, confirmPasswordText);

        try {
            long result = db.insert(TABLE_USERS, null, values);
            return result != -1;
        } catch (Exception e) {
            Log.e("DBHelper", "Error inserting user: ", e);
            return false;
        }
    }

    public boolean checkUser(String emailtext, String passwordtext) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?",
                new String[]{emailtext, passwordtext});

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    @SuppressLint("Range")
    public List<Rental> getAllRentals() {
        List<Rental> rentalList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RENTALS, null);

        if (cursor.moveToFirst()) {
            do {
                Rental rental = new Rental();
                rental.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
                rental.setBikeName(cursor.getString(cursor.getColumnIndex(COL_BIKE)));
                rental.setTime(cursor.getInt(cursor.getColumnIndex(COL_TIME)));
                rental.setPrice(cursor.getDouble(cursor.getColumnIndex(COL_PRICE)));
                rental.setImage(cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE)));
                rentalList.add(rental);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return rentalList;
    }

    @SuppressLint("Range")
    public User getUserDetails(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COL_FIRSTNAME, COL_SECONDNAME, COL_NIC, COL_PHONENUMBER, COL_EMAIL, COL_IMAGE},
                COL_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setFirstName(cursor.getString(cursor.getColumnIndex(COL_FIRSTNAME)));
            user.setSecondName(cursor.getString(cursor.getColumnIndex(COL_SECONDNAME)));
            user.setNic(cursor.getString(cursor.getColumnIndex(COL_NIC)));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex(COL_PHONENUMBER)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COL_EMAIL)));
            user.setImage(cursor.getBlob(cursor.getColumnIndex(COL_IMAGE)));
            cursor.close();
        } else {
            Log.e("DBHelper", "No user found with email: " + email);
        }
        db.close();
        return user;
    }




}