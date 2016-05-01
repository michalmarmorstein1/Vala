package com.valapay.vala.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.valapay.vala.R;

public class HomeActivity extends NavigationDrawerActivity {

    private GetAffiliatesTask getAffiliatesTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button collectButton = (Button) findViewById(R.id.collect_btn);
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getAffiliatesTask == null){
                    //TODO show progress
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
            //TODO hide progress
            if (success) {
                startActivity(new Intent(HomeActivity.this, PickupLocationActivity.class));
            } else {
                //TODO show server errors
            }
        }

        @Override
        protected void onCancelled() {
            getAffiliatesTask = null;
            //TODO hide progress
        }
    }
}
