package com.example.bicyclerentalapp.adapters;

public class Bicycle {

    private String name;
    private String price;
    private int image;


    public Bicycle(String name, String price, int image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getImage() {
        return image;
    }
}
