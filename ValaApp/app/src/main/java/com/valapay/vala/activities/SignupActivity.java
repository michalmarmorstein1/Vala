package com.valapay.vala.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.valapay.vala.R;
import com.valapay.vala.utils.RoundImage;

public class SignupActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Bitmap mUserBitmap;

    // UI references.
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mEmailView;
    private EditText mPhoneView;
    private EditText mPasswordView;
    private Spinner mCountryView;
    private ImageView mUserImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mFirstNameView = (EditText) findViewById(R.id.first_name);
        mLastNameView = (EditText) findViewById(R.id.last_name);
        mEmailView = (EditText) findViewById(R.id.email);
        mPhoneView = (EditText) findViewById(R.id.phone);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button signupButton = (Button) findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFieldsAndProceed();
            }
        });

        mUserImage = (ImageView) findViewById(R.id.imageViewCamera);
        mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        mCountryView = (Spinner) findViewById(R.id.countries_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View v = super.getView(position, convertView, parent);
                if (position == getCount()) {
                    ((TextView)v.findViewById(android.R.id.text1)).setText("");
                    ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                }
                return v;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1; // Don't display last item. It is used as hint.
            }
        };

        String [] countries = getResources().getStringArray(R.array.countries_array);
        for(int i = 0; i < countries.length; i++){
            adapter.add(countries[i]);
        }
        adapter.add(getString(R.string.signup_country)); //Add hint
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCountryView.setAdapter(adapter);
        mCountryView.setSelection(adapter.getCount()); //display hint
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            mUserBitmap = (Bitmap) extras.get("data");
            mUserImage.setImageDrawable(new RoundImage(mUserBitmap));
        }
    }

    private void checkFieldsAndProceed() {

        // Reset errors.
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mEmailView.setError(null);
        mPhoneView.setError(null);
        mPasswordView.setError(null);

        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String phone = mPhoneView.getText().toString();
        String password= mPasswordView.getText().toString();
        String country = (String) mCountryView.getSelectedItem();

        boolean cancel = false;
        View focusView = null;

        // Check user input.
        if(mUserBitmap == null){
            cancel = true;
            focusView = mUserImage;
            Toast.makeText(SignupActivity.this, getString(R.string.signup_photo_error), Toast.LENGTH_SHORT).show();
        }
        if (country.equals(getString(R.string.signup_country))) {
            focusView = mCountryView;
            Toast.makeText(SignupActivity.this, getString(R.string.signup_country_error), Toast.LENGTH_SHORT).show();
            cancel = true;
        }
        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError(getString(R.string.error_field_required));
            focusView = mPhoneView;
            cancel = true;
        }
        if (TextUtils.isEmpty(lastName)) {
            mLastNameView.setError(getString(R.string.error_field_required));
            focusView = mLastNameView;
            cancel = true;
        }
        if (TextUtils.isEmpty(firstName)) {
            mFirstNameView.setError(getString(R.string.error_field_required));
            focusView = mFirstNameView;
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            AddPinCodeActivity.startActivity(this, firstName, lastName, email, phone, mUserBitmap, country, password);
        }
    }

    //Client email validation
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

}
