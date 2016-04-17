package com.vala.valaapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vala.valaapp.R;

public class AddPinCodeActivity extends AppCompatActivity {

    private static final String FIRST_NAME_KEY = "FIRST_NAME_KEY";
    private static final String LAST_NAME_KEY = "LAST_NAME_KEY";
    private static final String EMAIL_KEY = "EMAIL_KEY";
    private static final String PHONE_KEY = "PHONE_KEY";

    private UserSignupTask mAuthTask = null;
    private View mProgressView;

    public static void startActivity(Context context, String firstName, String lastName, String email, String phone){

        Intent intent = new Intent(context, AddPinCodeActivity.class);
        intent.putExtra(FIRST_NAME_KEY, firstName);
        intent.putExtra(LAST_NAME_KEY, lastName);
        intent.putExtra(EMAIL_KEY, email);
        intent.putExtra(PHONE_KEY, phone);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pin_code);

        mProgressView = findViewById(R.id.signup_progress);

        final EditText e1 = (EditText) findViewById(R.id.editText1);
        final EditText e2 = (EditText) findViewById(R.id.editText2);
        final EditText e3 = (EditText) findViewById(R.id.editText3);
        final EditText e4 = (EditText) findViewById(R.id.editText4);

        final Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                attemptSignUp();
            }
        });

        View.OnKeyListener listener = new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(!e1.getText().toString().equals("") && !e2.getText().toString().equals("") &&
                        !e3.getText().toString().equals("") && !e4.getText().toString().equals("")) {
                    button.setEnabled(true);
                }
                return false;
            }

        };

        e1.setOnKeyListener(listener);
        e2.setOnKeyListener(listener);
        e3.setOnKeyListener(listener);
        e4.setOnKeyListener(listener);
    }

    private void attemptSignUp(){

        if (mAuthTask != null) {
            return;
        }

        Bundle bundle = getIntent().getExtras();
        // Show a progress spinner, and kick off a background task to
        // perform the user sign up attempt.
        showProgress(true);
        mAuthTask = new UserSignupTask(bundle.getString(FIRST_NAME_KEY),
                bundle.getString(LAST_NAME_KEY), bundle.getString(EMAIL_KEY), bundle.getString(PHONE_KEY));
        mAuthTask.execute((Void) null);
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
