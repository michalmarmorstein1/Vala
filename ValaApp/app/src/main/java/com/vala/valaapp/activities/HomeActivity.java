package com.vala.valaapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vala.valaapp.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button collectButton = (Button) findViewById(R.id.collect_btn);
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "collect", Toast.LENGTH_SHORT).show();
            }
        });

        Button sendButton = (Button) findViewById(R.id.send_btn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "send", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
