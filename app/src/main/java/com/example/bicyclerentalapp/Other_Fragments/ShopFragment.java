package com.example.bicyclerentalapp.Other_Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bicyclerentalapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ShopFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);


        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);


        ShopPagerAdapter adapter = new ShopPagerAdapter(requireActivity());
        viewPager.setAdapter(adapter);


        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText("Shop " + (position + 1));
        }).attach();

        return view;
    }

    private static class ShopPagerAdapter extends FragmentStateAdapter {

        public ShopPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return new ShopTabFragment(position + 1);
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }


    public static class ShopTabFragment extends Fragment {

        private final int shopNumber;

        public ShopTabFragment(int shopNumber) {
            this.shopNumber = shopNumber;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.tab_shop, container, false);


            TextView shopName = view.findViewById(R.id.shopName);
            TextView shopHours = view.findViewById(R.id.shopHours);
            TextView shopDays = view.findViewById(R.id.shopdays);
            TextView shopDescription = view.findViewById(R.id.shopDescription);
            ImageView shopImage = view.findViewById(R.id.shopImage);


            switch (shopNumber) {
                case 1:
                    shopName.setText("PaddleGo Colombo Malli");
                    shopHours.setText("Open Hours: 9 AM - 6 PM");
                    shopDays.setText("Closed on Sunday and Saturday");
                    shopDescription.setText("Located in the bustling heart of Colombo, Bike Haven is a paradise for mountain biking " +
                            "enthusiasts. Nestled amidst the vibrant city life, this shop offers a wide range of high-quality mountain " +
                            "bikes, rugged accessories, and expert advice for both beginners and seasoned riders. Whether you're planning " +
                            "to conquer the trails of Sinharaja Forest or explore the scenic routes around Colombo, Bike Haven has " +
                            "everything you need to make your adventure unforgettable. With a team of passionate cyclists, the shop also " +
                            "organizes weekend group rides and workshops to help you hone your skills. Closed on Sundays and Saturdays, " +
                            "Bike Haven is your go-to destination for all things mountain biking in the city.");
                    shopImage.setImageResource(R.drawable.colombo);
                    break;
                case 2:
                    shopName.setText("PaddleGo Kandy Lamissi");
                    shopHours.setText("Open Hours: 10 AM - 7 PM");
                    shopDays.setText("Closed on Wednesday till 12.00pm");
                    shopDescription.setText("Situated in the picturesque city of Kandy, Cycle World is your ultimate destination for " +
                            "urban cycling gear. Known for its serene landscapes and cultural heritage, Kandy provides the perfect " +
                            "backdrop for cycling enthusiasts to explore its winding roads and lush greenery. Cycle World offers a " +
                            "curated selection of stylish and functional urban bikes, along with accessories that cater to your daily " +
                            "commuting needs. Whether you're navigating the city streets or taking a leisurely ride around Kandy Lake, " +
                            "Cycle World ensures you do it in style and comfort. Closed on Wednesdays, this shop is a favorite among " +
                            "locals and tourists alike for its friendly service and top-notch products.");
                    shopImage.setImageResource(R.drawable.kandy);
                    break;
                case 3:
                    shopName.setText("PaddleGo Hambantota Whale");
                    shopHours.setText("Open Hours: 8 AM - 5 PM");
                    shopDays.setText("Closed on Sunday");
                    shopDescription.setText("In the sunny coastal town of Hambantota, Pedal Power stands out as a hub for road biking " +
                            "enthusiasts. Specializing in high-performance road bikes and gear, this shop caters to riders who crave " +
                            "speed and endurance. Hambantota's flat terrains and scenic coastal roads make it an ideal location for " +
                            "road cycling, and Pedal Power is here to equip you for the journey. Whether you're training for a race or " +
                            "simply enjoying a ride along the beach, their expert team will help you find the perfect bike and " +
                            "accessories to match your needs. Closed on Sundays, Pedal Power is dedicated to helping you push your " +
                            "limits and enjoy the thrill of the ride.");
                    shopImage.setImageResource(R.drawable.hambanthota);
                    break;
            }

            return view;
        }
    }
}