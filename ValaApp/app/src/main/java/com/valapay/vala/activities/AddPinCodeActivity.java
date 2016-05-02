package com.valapay.vala.activities;

import android.content.Context;
import android.content.Intent;
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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.valapay.vala.R;

public class AddPinCodeActivity extends AppCompatActivity {

    private static final String FIRST_NAME_KEY = "FIRST_NAME_KEY";
    private static final String LAST_NAME_KEY = "LAST_NAME_KEY";
    private static final String EMAIL_KEY = "EMAIL_KEY";
    private static final String PHONE_KEY = "PHONE_KEY";
    private static final String PHOTO_KEY = "PHOTO_KEY";

    private UserSignupTask mAuthTask = null;
    private View mProgressView;
    private View mImage;

    public static void startActivity(Context context, String firstName, String lastName,
                                     String email, String phone, String photo){

        Intent intent = new Intent(context, AddPinCodeActivity.class);
        intent.putExtra(FIRST_NAME_KEY, firstName);
        intent.putExtra(LAST_NAME_KEY, lastName);
        intent.putExtra(EMAIL_KEY, email);
        intent.putExtra(PHONE_KEY, phone);
        intent.putExtra(PHOTO_KEY, photo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pin_code);

        mProgressView = findViewById(R.id.signup_progress);
        mImage = findViewById(R.id.imageLock);
        TextView tv = (TextView) findViewById(R.id.textView5);
        Spannable wordToSpan = new SpannableString(tv.getText().toString());
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), 13, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordToSpan.setSpan(new ForegroundColorSpan(Color.rgb(0, 172, 163)), 13, 22, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(wordToSpan);

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
                bundle.getString(LAST_NAME_KEY), bundle.getString(EMAIL_KEY),
                bundle.getString(PHONE_KEY), bundle.getString(PHOTO_KEY));
        mAuthTask.execute((Void) null);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mImage.setVisibility(show ? View.GONE: View.VISIBLE);
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
        private final String mPhoto;

        UserSignupTask(String firstName, String lastName, String email, String phone, String photo) {
            mFirstName = firstName;
            mLastName = lastName;
            mEmail = email;
            mPhone = phone;
            mPhoto = photo;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: signup
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // TODO: upload photo
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
