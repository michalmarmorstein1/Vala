package com.valapay.vala.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.valapay.vala.R;
import com.valapay.vala.Vala;
import com.valapay.vala.common.UserLoginMessage;
import com.valapay.vala.common.UserSignupMessage;
import com.valapay.vala.model.User;
import com.valapay.vala.network.NetworkServices;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class AddPinCodeActivity extends AppCompatActivity {

    private static final String FIRST_NAME_KEY = "FIRST_NAME_KEY";
    private static final String LAST_NAME_KEY = "LAST_NAME_KEY";
    private static final String EMAIL_KEY = "EMAIL_KEY";
    private static final String PHONE_KEY = "PHONE_KEY";
    private static final String COUNTRY_KEY = "COUNTRY_KEY";
    private static final String PASSWORD_KEY = "PASSWORD_KEY";

    private UserSignupTask mAuthTask = null;
    private View mProgressView;
    private View mImage;
    private EditText e1;
    private EditText e2;
    private EditText e3;
    private EditText e4;

    private static Bitmap userImage;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String country;
    private String password;

    public static void startActivity(Context context, String firstName, String lastName,
                                     String email, String phone, Bitmap image, String country, String password){

        Intent intent = new Intent(context, AddPinCodeActivity.class);
        intent.putExtra(FIRST_NAME_KEY, firstName);
        intent.putExtra(LAST_NAME_KEY, lastName);
        intent.putExtra(EMAIL_KEY, email);
        intent.putExtra(PHONE_KEY, phone);
        intent.putExtra(COUNTRY_KEY, country);
        intent.putExtra(PASSWORD_KEY, password);
        userImage = image;
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pin_code);

        firstName = getIntent().getStringExtra(FIRST_NAME_KEY);
        lastName = getIntent().getStringExtra(LAST_NAME_KEY);
        email = getIntent().getStringExtra(EMAIL_KEY);
        phone = getIntent().getStringExtra(PHONE_KEY);
        country = getIntent().getStringExtra(COUNTRY_KEY);
        password = getIntent().getStringExtra(PASSWORD_KEY);

        mProgressView = findViewById(R.id.signup_progress);
        mImage = findViewById(R.id.imageLock);
        TextView tv = (TextView) findViewById(R.id.textView5);
        Spannable wordToSpan = new SpannableString(tv.getText().toString());
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), 13, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordToSpan.setSpan(new ForegroundColorSpan(Color.rgb(0, 172, 163)), 13, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(wordToSpan);

        e1 = (EditText) findViewById(R.id.editText1);
        e2 = (EditText) findViewById(R.id.editText2);
        e3 = (EditText) findViewById(R.id.editText3);
        e4 = (EditText) findViewById(R.id.editText4);

        final Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                if (e1.getText().toString().equals("") || e2.getText().toString().equals("") ||
                        e3.getText().toString().equals("") || e4.getText().toString().equals("")) {
                    Toast.makeText(AddPinCodeActivity.this, getString(R.string.pin_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                attemptSignUp();
            }
        });

        View.OnKeyListener listener = new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(v == e1 && !e1.getText().toString().equals("")) {
                    e2.requestFocus();
                } else if (v == e2 && !e2.getText().toString().equals("")) {
                    e3.requestFocus();
                } else if(v == e3 && !e3.getText().toString().equals("")) {
                    e4.requestFocus();
                }
                return false;
            }

        };

        e1.setOnKeyListener(listener);
        e2.setOnKeyListener(listener);
        e3.setOnKeyListener(listener);
    }

    private void attemptSignUp(){

        if (mAuthTask != null) {
            return;
        }

        String pin = e1.getText().toString() + e2.getText().toString() + e3.getText().toString() + e4.getText().toString();
        showProgress(true);
        mAuthTask = new UserSignupTask(firstName, lastName, email, phone, country, password, pin);
        mAuthTask.execute((Void) null);
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mImage.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public class UserSignupTask extends AsyncTask<Void, Void, Boolean> {

        private final String mFirstName;
        private final String mLastName;
        private final String mEmail;
        private final String mPhone;
        private final String mCountry;
        private final String mPassword;
        private final String mPin;

        UserSignupTask(String firstName, String lastName, String email, String phone, String country,
                       String password, String pin) {
            mFirstName = firstName;
            mLastName = lastName;
            mEmail = email;
            mPhone = phone;
            mCountry = country;
            mPassword = password;
            mPin = pin;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            UserSignupMessage userSignupMessage = new UserSignupMessage();
            userSignupMessage.setCountry(mCountry);
            userSignupMessage.setEmail(mEmail);
            userSignupMessage.setFirstName(mFirstName);
            userSignupMessage.setLastName(mLastName);
            userSignupMessage.setPassword(mPassword);
            userSignupMessage.setPhoneNumber(mPhone);
            userSignupMessage.setPin(mPin);
            Response<UserLoginMessage> signupResponse = null;
            try {
                signupResponse = NetworkServices.getTestService().signup(userSignupMessage).execute();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if(signupResponse.isSuccessful()){
                User user = Vala.getUser();
                UserLoginMessage data = signupResponse.body();
                user.login(mFirstName, mLastName, mEmail, mPhone, mCountry, 0, "$", data.getToken(),
                        data.getUserId());
                //Upload photo
                user.saveImage(userImage);
                File file = user.getImageFile();
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("name", user.getUserId(), requestFile);

                Response<ResponseBody> uploadImageResponse = null;
                try {
                    uploadImageResponse = NetworkServices.getTestService().uploadImage(body).execute();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                if(uploadImageResponse.isSuccessful()){
                    Log.d("VALA", "AddPinCodeActivity:UserSignupTask.doInBackground() - uploaded image successfully");
                }else{
                    Log.d("VALA", "AddPinCodeActivity:UserSignupTask.doInBackground() - upload image failed");
                    return false;
                }
            }else{
                Log.d("VALA", "AddPinCodeActivity:UserSignupTask.doInBackground() - signup failed");
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
                Intent i = new Intent(AddPinCodeActivity.this, HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            } else {
                //TODO show server errors
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
