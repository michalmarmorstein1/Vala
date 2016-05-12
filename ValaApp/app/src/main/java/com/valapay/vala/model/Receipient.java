package com.valapay.vala.model;

import android.graphics.Bitmap;

public class Receipient {

    private Bitmap image;
    private String name;
    private String id;

    public Receipient(Bitmap image, String name, String id) {
        this.image = image;
        this.name = name;
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
