package com.valapay.vala.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.valapay.vala.R;

public class PickupConfirmationActivity extends AppCompatActivity {

    private ConfirmCollectionTask confirmCollectionTask = null;
    private ReportIssueTask reportIssueTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_confirmation);

        TextView codeView = (TextView) findViewById(R.id.textViewCode);
        codeView.setText("SKT8N7");

        Button okBtn = (Button) findViewById(R.id.buttonOk);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmCollectionTask == null){
                    //TODO show progress
                    confirmCollectionTask = new ConfirmCollectionTask();
                    confirmCollectionTask.execute((Void) null);
                }
            }
        });

        Button problemBtn = (Button) findViewById(R.id.buttonProblem);
        problemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reportIssueTask == null){
                    //TODO show progress
                    reportIssueTask = new ReportIssueTask();
                    reportIssueTask.execute((Void) null);
                }
            }
        });
    }

    private class ConfirmCollectionTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: confirm money received
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            confirmCollectionTask = null;
            //TODO hide progress
            if (success) {
                startActivity(new Intent(PickupConfirmationActivity.this, HomeActivity.class));
            } else {
                //TODO show server errors
            }
        }

        @Override
        protected void onCancelled() {
            confirmCollectionTask = null;
            //TODO hide progress
        }
    }

    private class ReportIssueTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: report issue
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            reportIssueTask = null;
            //TODO hide progress
            if (success) {
                Toast.makeText(PickupConfirmationActivity.this, "That\'s a shame :)", Toast.LENGTH_SHORT).show();
            } else {
                //TODO show server errors
            }
        }

        @Override
        protected void onCancelled() {
            reportIssueTask = null;
            //TODO hide progress
        }
    }
}
