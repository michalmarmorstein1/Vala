package com.valapay.vala.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.valapay.vala.R;
import com.valapay.vala.Vala;
import com.valapay.vala.common.UserLoginMessage;
import com.valapay.vala.model.User;
import com.valapay.vala.network.NetworkUtils;

import java.io.IOException;

import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;
    private ForgotPasswordTask mPasswordTask = null;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mProgressView = findViewById(R.id.login_progress);
        mLogo = findViewById(R.id.login_logo);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        TextView forgotPassword = (TextView) findViewById(R.id.textViewForgotPassword);
        forgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPasswordTask == null){
                    mPasswordTask = new ForgotPasswordTask(mEmailView.getText().toString());
                    mPasswordTask.execute();
                }

            }
        });

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
//        if (TextUtils.isEmpty(email)) {
//            mEmailView.setError(getString(R.string.error_field_required));
//            focusView = mEmailView;
//            cancel = true;
//        } else if (!isEmailValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    //Client email validation
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    //Client password validation
    private boolean isPasswordValid(String password) {
        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLogo.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            UserLoginMessage userLoginMessage = new UserLoginMessage();
            userLoginMessage.setEmail(mEmail);
            userLoginMessage.setPassword(mPassword);
            Response<UserLoginMessage> loginResponse = null;
            try {
                loginResponse = NetworkUtils.getTestService().login(userLoginMessage).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(loginResponse.isSuccessful()){
                UserLoginMessage data = loginResponse.body();
                User user = Vala.getUser();
                user.login(BitmapFactory.decodeResource(getResources(), R.drawable.babu),
                        "Babu", "bla", data.getEmail(), "123456", "Pakistan", 250, "\u20AA");
                //TODO set recipients
                return true;
            }else{
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            } else {
                //TODO show server errors
                Toast.makeText(LoginActivity.this, "An error as occurred", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public class ForgotPasswordTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;

        ForgotPasswordTask(String email) {
            mEmail = email;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt to reset password using a network service.
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // TODO: register the new account.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mPasswordTask = null;
            showProgress(false);

            if (success) {
                Toast.makeText(LoginActivity.this, String.format("email will be sent to %1$s", mEmail), Toast.LENGTH_SHORT).show();

            } else {
                //TODO show server errors
            }
        }

        @Override
        protected void onCancelled() {
            mPasswordTask = null;
            showProgress(false);
        }
    }
}

