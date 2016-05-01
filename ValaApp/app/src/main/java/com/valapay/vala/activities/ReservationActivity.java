package com.valapay.vala.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.valapay.vala.R;

public class ReservationActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 0;

    private static final String NAME_KEY = "NAME_KEY";
    private static final String AMOUNT_KEY = "AMOUNT_KEY";

    private ConfirmReservationTask confirmReservationTask = null;

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

        TextView amountView = (TextView) findViewById(R.id.textViewAmount);
        amountView.setText(amount);
        TextView nameView = (TextView) findViewById(R.id.textViewName);
        nameView.setText(getString(R.string.collect_2, name));

        Button okBtn = (Button) findViewById(R.id.buttonOk);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmReservationTask == null){
                    //TODO show progress
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
            //TODO hide progress
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
            //TODO hide progress
        }
    }
}
