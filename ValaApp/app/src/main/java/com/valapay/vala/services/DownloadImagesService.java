package com.valapay.vala.services;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.valapay.vala.R;
import com.valapay.vala.Vala;
import com.valapay.vala.model.Recipient;
import com.valapay.vala.model.User;
import com.valapay.vala.network.NetworkServices;
import com.valapay.vala.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;


public class DownloadImagesService extends IntentService {

    public DownloadImagesService() {
        super("DownloadImagesService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("VALA", "DownloadImagesService:onHandleIntent()");

        if (intent != null) {
            User user = Vala.getUser();
            ArrayList<Recipient> recipients = user.getRecipients();
            for(Recipient r : recipients){
                saveRecipientImage(r);
            }
            user.setRecipients(user.getRecipients());//Save image paths in sharedPref
        }
    }

    private void saveRecipientImage(Recipient recipient){
        Log.d("VALA", "DownloadImagesService:saveRecipientImage()");
        Response<ResponseBody> response = null;
        try {
            response = NetworkServices.getTestService().getImageFile(recipient.getId()).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response.isSuccessful()) {
            Log.d("VALA", "DownloadImagesService:saveRecipientImage() - server contacted and has file");

            File imageFile = NetworkUtils.writeResponseBodyToFile(response.body());
            if(imageFile != null){
                recipient.saveImage(imageFile);
            }else{
                Log.d("VALA", "DownloadImagesService:saveRecipientImage() - invalid image file");
            }
            Log.d("VALA", "DownloadImagesService:saveRecipientImage() - image file: " + imageFile);
        } else {
            Log.d("VALA", "DownloadImagesService:saveRecipientImage() - image download failed");
        }
    }
}
