package com.valapay.vala.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.valapay.vala.R;
import com.valapay.vala.components.RoundImage;

public class HomeActivity extends NavigationDrawerActivity {

    private GetAffiliatesTask getAffiliatesTask = null;
    private View mProgressView;
    private View mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgressView = findViewById(R.id.home_progress);
        mImage = findViewById(R.id.userImage);

        TextView tv = (TextView) findViewById(R.id.name_text);
        Spannable wordToSpan = new SpannableString(tv.getText().toString());
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), 3, wordToSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(wordToSpan);

        tv = (TextView) findViewById(R.id.amount_text);
        wordToSpan = new SpannableString(tv.getText().toString());
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), 1, wordToSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(wordToSpan);

        Button collectButton = (Button) findViewById(R.id.collect_btn);
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getAffiliatesTask == null) {
                    showProgress(true);
                    getAffiliatesTask = new GetAffiliatesTask();
                    getAffiliatesTask.execute((Void) null);
                }
            }
        });

        Button sendButton = (Button) findViewById(R.id.send_btn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SendActivity.class));
            }
        });

        ImageView userImage = (ImageView) findViewById(R.id.userImage);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.babu);
        RoundImage roundedImage = new RoundImage(bm);
        userImage.setImageDrawable(roundedImage);
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mImage.setVisibility(show ? View.GONE: View.VISIBLE);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_home;
    }

    public class GetAffiliatesTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: get affiliates details
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getAffiliatesTask = null;
            showProgress(false);
            if (success) {
                startActivity(new Intent(HomeActivity.this, PickupLocationActivity.class));
            } else {
                //TODO show server errors
            }
        }

        @Override
        protected void onCancelled() {
            getAffiliatesTask = null;
            showProgress(false);

        }
    }
}
