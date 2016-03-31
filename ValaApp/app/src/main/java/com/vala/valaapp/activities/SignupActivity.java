package com.vala.valaapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.vala.valaapp.R;

/**
 * A sign up screen that offers registration.
 */

public class SignupActivity extends AppCompatActivity {

    /**
     * Keep track of the signup task to ensure we can cancel it if requested.
     */
    private UserSignupTask mAuthTask = null;

    // UI references.
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mEmailView;
    private EditText mPhoneView;
    private View mProgressView;
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
                attemptSignup();
            }
        });

        mProgressView = findViewById(R.id.signup_progress);
        mCountryView = (Spinner) findViewById(R.id.countries_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.countries_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCountryView.setAdapter(adapter);
    }

    /**
     * Attempts to register the account specified by the sign up form.
     * If there are form errors, the errors are presented and no actual sign up attempt is made.
     */
    private void attemptSignup() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mEmailView.setError(null);
        mPhoneView.setError(null);

        // Store values at the time of the login attempt.
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
            // There was an error; don't attempt sign up and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user sign up attempt.
            showProgress(true);
            mAuthTask = new UserSignupTask(firstName, lastName, email, phone);
            mAuthTask.execute((Void) null);
        }
    }

    //Client email validation
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * Represents an asynchronous registration task used to authenticate
     * the user.
     */
    public class UserSignupTask extends AsyncTask<Void, Void, Boolean> {

        private final String mFirstName;
        private final String mLastName;
        private final String mEmail;
        private final String mPhone;

        UserSignupTask(String firstName, String lastName, String email, String phone) {
            mFirstName = firstName;
            mLastName = lastName;
            mEmail = email;
            mPhone = phone;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // TODO: register the new account.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
                Intent i = new Intent(SignupActivity.this, HomeActivity.class);
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
