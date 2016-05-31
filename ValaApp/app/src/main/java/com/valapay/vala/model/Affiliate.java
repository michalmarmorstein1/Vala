package com.valapay.vala.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

public class Affiliate {

    private String name;
    private LatLng location;
    private String openingHours;
    private Bitmap rating;
    private String phone;
    private String id;
    private String address;
    private Bitmap defaultImage;

    public Affiliate(String name, LatLng location, String openingHours, Bitmap rating, String phone
            , Bitmap defaultImage, String address, String id) {
        this.name = name;
        this.location = location;
        this.openingHours = openingHours;
        this.rating = rating;
        this.phone = phone;
        this.id = id;
        this.address = address;
        this.defaultImage = defaultImage;
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

        //TODO get image from cache according to id
        return defaultImage;
    }

    public String getAddress() {
        return address;
    }

}