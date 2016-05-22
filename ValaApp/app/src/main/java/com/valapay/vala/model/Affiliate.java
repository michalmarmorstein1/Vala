package com.valapay.vala.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class Affiliate {

    private String name;
    private LatLng location;
    private String openingHours;
    private Bitmap rating;
    private String phone;
    private Bitmap image;
    private String address;

    public Affiliate(String name, LatLng location, String openingHours, Bitmap rating, String phone, Bitmap image, String address) {
        this.name = name;
        this.location = location;
        this.openingHours = openingHours;
        this.rating = rating;
        this.phone = phone;
        this.image = image;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public Bitmap getRating() {
        return rating;
    }

    public String getPhone() {
        return phone;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getAddress() {
        return address;
    }

}