package com.valapay.vala.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.valapay.vala.utils.CameraUtils;

import java.io.File;
import java.util.ArrayList;

public class User {

    private final static String SHARED_PREFERENCES_NAME = "com.valapay.vala.model.User";
    private final static String IMAGE_KEY = "IMAGE_KEY";
    private final static String FIRST_NAME_KEY = "FIRST_NAME_KEY";
    private final static String LAST_NAME_KEY = "LAST_NAME_KEY";
    private final static String EMAIL_KEY = "EMAIL_KEY";
    private final static String PHONE_KEY = "PHONE_KEY";
    private final static String COUNTRY_KEY = "COUNTRY_KEY";
    private final static String IS_REGISTERED_KEY = "IS_REGISTERED_KEY";
    private final static String RECIPIENTS_KEY = "RECIPIENTS_KEY";
    private final static String BALANCE_KEY = "BALANCE_KEY";
    private final static String CURRENCY_KEY = "CURRENCY_KEY";

    private String imagePath;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String country;
    private int balance;
    private ArrayList<Recipient> recipients;
    private boolean isRegistered;
    private String currency;

    private SharedPreferences preferences;

    public User(Context context){

        preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        isRegistered = preferences.getBoolean(IS_REGISTERED_KEY, false);
        if(isRegistered()){
            restoreFromPreferences();
        }
    }

    public void login(Bitmap userBitmap, String firstName, String lastName, String email,
                      String phone, String country, int balance, String currency) {
        this.firstName = firstName;
        preferences.edit().putString(FIRST_NAME_KEY, firstName);
        this.lastName = lastName;
        preferences.edit().putString(LAST_NAME_KEY, lastName);
        this.email = email;
        preferences.edit().putString(EMAIL_KEY, email);
        this.phone = phone;
        preferences.edit().putString(PHONE_KEY, phone);
        this.country = country;
        preferences.edit().putString(COUNTRY_KEY, country);
        this.isRegistered = true;
        preferences.edit().putBoolean(IS_REGISTERED_KEY, true);
        recipients = new ArrayList<>(); //TODO handle preferences?
        this.balance = balance;
        preferences.edit().putFloat(BALANCE_KEY, balance);
        this.currency = currency;
        preferences.edit().putString(CURRENCY_KEY, currency);

        File imageFile = CameraUtils.createImageFile();
        this.imagePath = imageFile.getAbsolutePath();
        CameraUtils.storeImageToFile(userBitmap, imageFile);
    }

    public void logout() {
        preferences.edit().clear().commit();
        getImageFile().delete();
        this.imagePath = null;
        this.firstName = null;
        this.lastName = null;
        this.email = null;
        this.phone = null;
        this.country = null;
        this.recipients = null;
        this.isRegistered = false;
        this.balance = 0;
        this.currency = null;
    }

    public boolean isRegistered(){
        return  isRegistered;
    }

    public Bitmap getImageBitmap() {
        return BitmapFactory.decodeFile(imagePath);
    }

    public File getImageFile() {
        return new File(imagePath);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    public ArrayList<Recipient> getRecipients() {
        return recipients;
    }

    public void addRecipient(Recipient recipient) {
        this.recipients.add(recipient);
        //TODO handle preferences
    }

    public void setRecipients(ArrayList<Recipient> recipients) {
        this.recipients = recipients;
        //TODO handle preferences
    }

    public String getCurrency() {
        return currency;
    }

    public int getBalance() {
        return balance;
    }

    public String getBalanceString() {
        return currency + balance;
    }

    private void restoreFromPreferences(){
        imagePath = preferences.getString(IMAGE_KEY, null);
        firstName = preferences.getString(FIRST_NAME_KEY, null);
        lastName = preferences.getString(LAST_NAME_KEY, null);
        email = preferences.getString(EMAIL_KEY, null);
        phone = preferences.getString(PHONE_KEY, null);
        country = preferences.getString(COUNTRY_KEY, null);
        //TODO handle this: recipients = ;
        balance = preferences.getInt(BALANCE_KEY, 0);
        currency = preferences.getString(CURRENCY_KEY, null);
    }
}
