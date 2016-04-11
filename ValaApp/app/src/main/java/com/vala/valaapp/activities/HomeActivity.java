package com.vala.valaapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vala.valaapp.R;

public class HomeActivity extends NavigationDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button collectButton = (Button) findViewById(R.id.collect_btn);
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectActivity.startCollectActivity("Abhimanyusuta", "100$", HomeActivity.this);
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
}
