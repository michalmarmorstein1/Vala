package com.valapay.vala.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraUtils {


    public static File createImageFile(){

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (FileNotFoundException e) {
            Log.d("VALA", "CameraUtils:createImageFile() - File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("VALA", "CameraUtils:createImageFile() - Error accessing file: " + e.getMessage());
        }

        return image;
    }

    public static void storeImageToFile(Bitmap image, File pictureFile) {
        Log.d("VALA", "CameraUtils:storeImageToFile() - saving image to: " + pictureFile.getAbsolutePath());
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d("VALA", "CameraUtils:storeImageToFile() - File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d("VALA", "CameraUtils:storeImageToFile() - Error accessing file: " + e.getMessage());
        }
    }
}
