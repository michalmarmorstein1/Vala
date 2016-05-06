package com.valapay.vala.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.valapay.vala.R;

public class PickupConfirmationActivity extends AppCompatActivity {

    private ConfirmCollectionTask confirmCollectionTask = null;
    private ReportIssueTask reportIssueTask = null;
    View mProgressView;
    View mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_confirmation);

        mProgressView = findViewById(R.id.pickup_progress);
        mImage = findViewById(R.id.imageViewHeader);

        TextView codeView = (TextView) findViewById(R.id.textViewCode);
        SpannableString content = new SpannableString("SKT8N7");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        codeView.setText(content);

        Button okBtn = (Button) findViewById(R.id.buttonOk);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmCollectionTask == null){
                    showProgress(true);
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
                    showProgress(true);
                    reportIssueTask = new ReportIssueTask();
                    reportIssueTask.execute((Void) null);
                }
            }
        });
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mImage.setVisibility(show ? View.GONE : View.VISIBLE);
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
            showProgress(false);
            if (success) {
                startActivity(new Intent(PickupConfirmationActivity.this, HomeActivity.class));
            } else {
                //TODO show server errors
            }
        }

        @Override
        protected void onCancelled() {
            confirmCollectionTask = null;
            showProgress(false);
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
            showProgress(false);
            if (success) {
                Toast.makeText(PickupConfirmationActivity.this, "That\'s a shame :)", Toast.LENGTH_SHORT).show();
            } else {
                //TODO show server errors
            }
        }

        @Override
        protected void onCancelled() {
            reportIssueTask = null;
            showProgress(false);
        }
    }
}
