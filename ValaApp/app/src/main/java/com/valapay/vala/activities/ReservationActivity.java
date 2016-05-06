package com.valapay.vala.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.valapay.vala.R;

public class ReservationActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 0;

    private static final String NAME_KEY = "NAME_KEY";
    private static final String AMOUNT_KEY = "AMOUNT_KEY";

    private ConfirmReservationTask confirmReservationTask = null;
    View mProgressView;
    View mImage;

    public static void startCollectActivity(String name, String amount, Activity context){

        Intent intent = new Intent(context, ReservationActivity.class);
        intent.putExtra(NAME_KEY, name);
        intent.putExtra(AMOUNT_KEY, amount);
        context.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);

        String name = getIntent().getStringExtra(NAME_KEY);
        String amount = getIntent().getStringExtra(AMOUNT_KEY);

        mProgressView = findViewById(R.id.collect_progress);
        mImage = findViewById(R.id.imageView);
        TextView amountView = (TextView) findViewById(R.id.textViewAmount);
        Spannable wordToSpan = new SpannableString(amount);
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), 1, wordToSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        amountView.setText(wordToSpan);

        TextView nameView = (TextView) findViewById(R.id.textViewName);
        String txt = getString(R.string.collect_2, name);
        wordToSpan = new SpannableString(txt);
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), txt.indexOf(name), txt.indexOf(name) + name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        nameView.setText(wordToSpan);

        TextView textView3 = (TextView) findViewById(R.id.textView3);
        String boldText = "next 5 hours";
        String txt3 = getString(R.string.collect_3);
        wordToSpan = new SpannableString(txt3);
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), txt3.indexOf(boldText), txt3.indexOf(boldText) + boldText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView3.setText(wordToSpan);

        Button okBtn = (Button) findViewById(R.id.buttonOk);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmReservationTask == null){
                    showProgress(true);
                    confirmReservationTask = new ConfirmReservationTask();
                    confirmReservationTask.execute((Void) null);
                }
            }
        });

        Button cancelBtn = (Button) findViewById(R.id.buttonCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mImage.setVisibility(show ? View.GONE: View.VISIBLE);
    }

    private class ConfirmReservationTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: confirm reservation
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            confirmReservationTask = null;
            showProgress(false);
            if (success) {
                setResult(RESULT_OK);
                finish();
            } else {
                //TODO show server errors
            }
        }

        @Override
        protected void onCancelled() {
            confirmReservationTask = null;
            showProgress(false);
        }
    }
}
