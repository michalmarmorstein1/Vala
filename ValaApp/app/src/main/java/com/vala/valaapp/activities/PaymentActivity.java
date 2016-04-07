package com.vala.valaapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vala.valaapp.R;

public class PaymentActivity extends NavigationDrawerActivity {

    private static final String RECIPIENT_KEY = "RECIPIENT_KEY";
    private static final String AMOUNT_KEY = "AMOUNT_KEY";
    private static final int FEE = 3;

    private String recipient;
    private int amount;

    public static void startPaymentActivity(String recipient, int amount, Activity context){

        Intent intent = new Intent(context, PaymentActivity.class);
        intent.putExtra(RECIPIENT_KEY, recipient);
        intent.putExtra(AMOUNT_KEY, amount);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipient = getIntent().getStringExtra(RECIPIENT_KEY);
        amount = getIntent().getIntExtra(AMOUNT_KEY, 0);

        TextView amountView = (TextView) findViewById(R.id.amount_text);
        TextView feeView = (TextView) findViewById(R.id.fee_text);
        TextView totalView = (TextView) findViewById(R.id.total_text);

        //TODO handle currency
        String amountText = getString(R.string.payment_amount) + " " + amount + "$";
        amountView.setText(amountText);
        String feeText = getString(R.string.payment_fee_1) + " " + FEE + getString(R.string.payment_fee_2);
        feeView.setText(feeText);
        int total = amount - FEE;
        String totalText = recipient + " " + getString(R.string.payment_total) + " " + total + "$";
        totalView.setText(totalText);

        Button continueBtn = (Button) findViewById(R.id.payment_button);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO integrate payment here
            }
        });
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_payment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            startActivity(new Intent(this, HomeActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
