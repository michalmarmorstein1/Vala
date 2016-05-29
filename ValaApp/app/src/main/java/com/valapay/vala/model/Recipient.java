package com.valapay.vala.model;

import android.graphics.Bitmap;

public class Recipient {

    private Bitmap image;
    private String name;
    private String id;

    public Recipient(String name, String id) {
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
