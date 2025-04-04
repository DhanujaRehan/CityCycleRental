package com.example.bicyclerentalapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicyclerentalapp.DBHelper;
import com.example.bicyclerentalapp.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private Context context;
    private List<Bicycle> bicycleList;
    private DBHelper dbHelper;

    public Adapter(Context context, List<Bicycle> bicycleList) {
        this.context = context;
        this.bicycleList = bicycleList;
        dbHelper = new DBHelper(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_item, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Bicycle bicycle = bicycleList.get(position);

        holder.imageView.setImageResource(bicycle.getImage());
        holder.name.setText(bicycle.getName());
        holder.price.setText(bicycle.getPrice());

        holder.rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String priceStr = bicycle.getPrice().replaceAll("[^\\d.]", "");
                    double price = Double.parseDouble(priceStr);

                    Log.d("Adapter", "Attempting to insert rental: " + bicycle.getName() + ", " + price);
                    byte[] imageBytes = getImageAsByteArray(bicycle.getImage());


                    boolean isInserted = dbHelper.insertRentals(
                            bicycle.getName(),
                            1,
                            price,
                            imageBytes
                    );

                    if (isInserted) {
                        Toast.makeText(context, bicycle.getName() + " added to cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to add " + bicycle.getName() + " to cart", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(context, "Invalid price format", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                } catch (Exception e) {
                    Toast.makeText(context, "An error occurred", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private byte[] getImageAsByteArray(int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public int getItemCount() {
        return bicycleList.size();
    }

    public void setSearchList(List<Bicycle> filteredList) {
        this.bicycleList = filteredList;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, price;
        Button rent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            rent = itemView.findViewById(R.id.rent);
        }
    }
}