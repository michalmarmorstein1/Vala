package com.valapay.vala.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.valapay.vala.utils.CameraUtils;

import java.io.File;
import java.lang.reflect.Type;
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
    private final static String TOKEN_KEY = "TOKEN_KEY";
    private final static String ID_KEY = "ID_KEY";

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
    private String token;
    private String userId;

    private SharedPreferences preferences;

    public User(Context context){

        preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        isRegistered = preferences.getBoolean(IS_REGISTERED_KEY, false);
        Log.d("VALA", "User:User() - isRegistered=" + isRegistered);
        if(isRegistered()){
            restoreFromPreferences();
        }
    }

    public void login(String firstName, String lastName, String email,
                      String phone, String country, int balance, String currency, String token,
                      String userId) {
        this.firstName = firstName;
        preferences.edit().putString(FIRST_NAME_KEY, firstName).apply();
        this.lastName = lastName;
        preferences.edit().putString(LAST_NAME_KEY, lastName).apply();
        this.email = email;
        preferences.edit().putString(EMAIL_KEY, email).apply();
        this.phone = phone;
        preferences.edit().putString(PHONE_KEY, phone).apply();
        this.country = country;
        preferences.edit().putString(COUNTRY_KEY, country).apply();
        this.isRegistered = true;
        preferences.edit().putBoolean(IS_REGISTERED_KEY, true).apply();
        recipients = new ArrayList<>();
        this.balance = balance;
        preferences.edit().putInt(BALANCE_KEY, balance).apply();
        //TODO - move this logic to the server
        if(currency.equals("NIS")){
            this.currency = "\\u20AA";
        }else{
            this.currency = "$";
        }
        preferences.edit().putString(CURRENCY_KEY, this.currency).apply();
        this.token = token;
        preferences.edit().putString(TOKEN_KEY, token).apply();
        this.userId = userId;
        preferences.edit().putString(ID_KEY, userId).apply();

        Log.d("VALA", "User:login() - firstName=" + this.firstName +
                ", lastName=" + this.lastName + ", email=" + this.email +
                ", phone=" + this.phone+ ", country=" + this.country +
                ", balance=" + this.balance + ", currency=" + this.currency +
                ", token=" + this.token + ", userId=" + this.userId);
    }

    public void saveImage(Bitmap userBitmap){

        File imageFile = CameraUtils.createImageFile(this.firstName);
        this.imagePath = imageFile.getAbsolutePath();
        preferences.edit().putString(IMAGE_KEY, imagePath).apply();
        CameraUtils.storeImageToFile(userBitmap, imageFile);
    }

    public void saveImageFile(File userImageFile){

        this.imagePath = userImageFile.getAbsolutePath();
        preferences.edit().putString(IMAGE_KEY, imagePath).apply();
    }

    public void logout() {
        Log.d("VALA", "User:logout()");
        preferences.edit().clear().apply();
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
        this.token = null;
        userId = null;
    }

    public boolean isRegistered(){
        return  isRegistered;
    }

    public Bitmap getImageBitmap() {
        Log.d("VALA", "User:getImageBitmap() - imagePath=" + imagePath);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        return bitmap;
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

    public synchronized void addRecipient(Recipient recipient) {
        this.recipients.add(recipient);
        setRecipients(this.recipients);
    }

    public void setRecipients(ArrayList<Recipient> recipients) {
        this.recipients = recipients;

        //Save in sharedPreferences
        Gson gson = new Gson();
        String json = gson.toJson(recipients);
        preferences.edit().putString(RECIPIENTS_KEY, json).commit();
    }

    public String getCurrency() {

        return currency;
    }

    public int getBalance() {
        return balance;
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
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
        balance = preferences.getInt(BALANCE_KEY, 0);
        currency = preferences.getString(CURRENCY_KEY, null);
        token = preferences.getString(TOKEN_KEY, null);
        userId = preferences.getString(ID_KEY, null);

        Gson gson = new Gson();
        String json = preferences.getString(RECIPIENTS_KEY, null);
        Type type = new TypeToken<ArrayList<Recipient>>() {}.getType();
        recipients = gson.fromJson(json, type);
        if(recipients == null){
            recipients = new ArrayList<>();
        }
    }
}
