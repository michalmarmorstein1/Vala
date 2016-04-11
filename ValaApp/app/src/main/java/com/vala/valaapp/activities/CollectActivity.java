package com.vala.valaapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vala.valaapp.R;

public class CollectActivity extends AppCompatActivity {

    private static final String NAME_KEY = "NAME_KEY";
    private static final String AMOUNT_KEY = "AMOUNT_KEY";

    public static void startCollectActivity(String name, String amount, Activity context){

        Intent intent = new Intent(context, CollectActivity.class);
        intent.putExtra(NAME_KEY, name);
        intent.putExtra(AMOUNT_KEY, amount);
        context.startActivity(intent);
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

            }
        });

        Button cancelBtn = (Button) findViewById(R.id.buttonCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
