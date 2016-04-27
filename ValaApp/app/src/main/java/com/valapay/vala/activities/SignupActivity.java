package com.valapay.vala.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.valapay.vala.R;

public class SignupActivity extends AppCompatActivity {

    // UI references.
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mEmailView;
    private EditText mPhoneView;
    private Spinner mCountryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mFirstNameView = (EditText) findViewById(R.id.first_name);
        mLastNameView = (EditText) findViewById(R.id.last_name);
        mEmailView = (EditText) findViewById(R.id.email);
        mPhoneView = (EditText) findViewById(R.id.phone);

        Button signupButton = (Button) findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFieldsAndProceed();
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

    private void checkFieldsAndProceed() {

        // Reset errors.
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mEmailView.setError(null);
        mPhoneView.setError(null);

        String firstName = mFirstNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String phone = mPhoneView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check user input.
//        if (TextUtils.isEmpty(phone)) {
//            mPhoneView.setError(getString(R.string.error_field_required));
//            focusView = mPhoneView;
//            cancel = true;
//        }
//        if (TextUtils.isEmpty(email)) {
//            mEmailView.setError(getString(R.string.error_field_required));
//            focusView = mEmailView;
//            cancel = true;
//        } else if (!isEmailValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }
//        if (TextUtils.isEmpty(lastName)) {
//            mLastNameView.setError(getString(R.string.error_field_required));
//            focusView = mLastNameView;
//            cancel = true;
//        }
//        if (TextUtils.isEmpty(firstName)) {
//            mFirstNameView.setError(getString(R.string.error_field_required));
//            focusView = mFirstNameView;
//            cancel = true;
//        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            AddPinCodeActivity.startActivity(this, firstName, lastName, email, phone, "photo file location");
        }
    }

    //Client email validation
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

}
