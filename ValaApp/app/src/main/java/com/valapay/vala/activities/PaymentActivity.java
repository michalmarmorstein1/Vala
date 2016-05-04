package com.valapay.vala.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valapay.vala.R;

public class PaymentActivity extends NavigationDrawerActivity {

    private static final String RECIPIENT_KEY = "RECIPIENT_KEY";
    private static final String AMOUNT_KEY = "AMOUNT_KEY";
    private static final int FEE = 3;

    private String recipient;
    private int amount;
    private String currency = "$";
    private ConfirmPaymentTask confirmPaymentTask = null;

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
        String amountStr = currency + amount;
        Spannable wordToSpan = new SpannableString(amountStr);
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), currency.length(), wordToSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        amountView.setText(wordToSpan);

        String feeStr = getString(R.string.payment_fee, FEE);
        wordToSpan = new SpannableString(feeStr);
        int startIndex = feeStr.indexOf("pay") + 4;
        wordToSpan.setSpan(new ForegroundColorSpan(Color.rgb(242, 160, 104)), startIndex, startIndex + currency.length() + String.valueOf(FEE).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), startIndex + currency.length(), startIndex + currency.length() + String.valueOf(FEE).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        feeView.setText(wordToSpan);

        int total = amount - FEE;
        String totalText = recipient + " " + getString(R.string.payment_total) + " $" + total;
        wordToSpan = new SpannableString(totalText);
        startIndex = totalText.indexOf(currency);
        wordToSpan.setSpan(new ForegroundColorSpan(Color.rgb(0, 153, 219)), startIndex, startIndex + currency.length() + String.valueOf(total).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), startIndex + currency.length(), startIndex + currency.length() + String.valueOf(total).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, recipient.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        totalView.setText(wordToSpan);

        Button okBtn = (Button) findViewById(R.id.buttonPaymentOk);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmPaymentTask == null){
                    //TODO show progress
                    confirmPaymentTask = new ConfirmPaymentTask();
                    confirmPaymentTask.execute((Void) null);
                }
            }
        });

        Button cancelBtn = (Button) findViewById(R.id.buttonPaymentCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Button continueBtn = (Button) findViewById(R.id.buttonPayment);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO integrate payment here
                LinearLayout layout = (LinearLayout) findViewById(R.id.LayoutConfirmPayment);
                layout.setVisibility(View.VISIBLE);
                continueBtn.setVisibility(View.GONE);
                ImageView steps = (ImageView) findViewById(R.id.stepsImage);
                steps.setImageDrawable(getDrawable(R.drawable.stepstabs_confirm));
                findViewById(R.id.card_text).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_payment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Add items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_home) {
            startActivity(new Intent(this, HomeActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static public class PaymentConfirmationDialogFragment extends DialogFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog);
        }

        @Override
        public void onStart() {
            super.onStart();
            Dialog d = getDialog();
            if (d != null){
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                d.getWindow().setLayout(width, height);
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            final View root = inflater.inflate(R.layout.fragment_payment_confirmation, container, true);

            Button button = (Button) root.findViewById(R.id.con_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                    Intent i = new Intent(getActivity(), HomeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            });

            String recipient = getActivity().getIntent().getStringExtra(RECIPIENT_KEY);
            TextView recipientTextView = (TextView) root.findViewById(R.id.textViewName);
            recipientTextView.setText(recipient + " " + getString(R.string.payment_confirmation_top_2));

            TextView trackingNumberTextView = (TextView) root.findViewById(R.id.textViewTrackingNumber);
            trackingNumberTextView.setText("WE456TYU7");

            return root;
        }

    }

    public class ConfirmPaymentTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: get tracking number
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            confirmPaymentTask = null;
            //TODO hide progress
            if (success) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                PaymentConfirmationDialogFragment frag = new PaymentConfirmationDialogFragment();
                frag.show(ft, "payment_confirmation_fragment_tag");

            } else {
                //TODO show server errors
            }
        }

        @Override
        protected void onCancelled() {
            confirmPaymentTask = null;
            //TODO hide progress
        }
    }
}
