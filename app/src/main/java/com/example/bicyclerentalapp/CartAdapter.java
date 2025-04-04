package com.example.bicyclerentalapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bicyclerentalapp.adapters.Rental;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<Rental> rentalList;

    public CartAdapter(Context context, List<Rental> rentalList) {
        this.context = context;
        this.rentalList = rentalList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Rental rental = rentalList.get(position);
        holder.bikeNameTextView.setText(rental.getBikeName());
        holder.timeTextView.setText("Time: " + rental.getTime() + " days");
        holder.priceTextView.setText("Price: " + rental.getPrice() + " LKR");

        byte[] image = rental.getImage();
        if (image != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            holder.bikeImageView.setImageBitmap(bitmap);
        }

        holder.deleteimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Item deleted from cart", Toast.LENGTH_SHORT).show();


                rentalList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, rentalList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return rentalList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView bikeNameTextView, timeTextView, priceTextView;
        ImageView bikeImageView, deleteimage;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            deleteimage = itemView.findViewById(R.id.delete);
            bikeNameTextView = itemView.findViewById(R.id.bikeNameTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            bikeImageView = itemView.findViewById(R.id.bikeImageView);
        }
    }
}