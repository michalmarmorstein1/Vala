package com.valapay.vala.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.valapay.vala.R;

public class PickupConfirmationActivity extends AppCompatActivity {

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
                startActivity(new Intent(PickupConfirmationActivity.this, HomeActivity.class));
            }
        });

        Button problemBtn = (Button) findViewById(R.id.buttonProblem);
        problemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PickupConfirmationActivity.this, "That\'s a shame :)", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
