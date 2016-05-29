package com.valapay.vala.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;

public class Recipient {

    private String imagePath;
    private String name;
    private String id;
    private Bitmap defaultBitmap;

    public Recipient(String name, String id, Bitmap defaultBitmap) {
        this.name = name;
        this.id = id;
        this.defaultBitmap = defaultBitmap;
    }

    public void saveImage(File image){
        imagePath = image.getAbsolutePath();
    }

    public Bitmap getImage() {
        Log.d("VALA", "Recipient:getImage() - imagePath=" + imagePath);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        if(bitmap == null){
            Log.d("VALA", "Recipient:getImage() - using default image");
            return defaultBitmap;
        }
        return bitmap;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
