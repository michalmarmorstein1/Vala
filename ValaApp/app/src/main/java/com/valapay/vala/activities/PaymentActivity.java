package com.valapay.vala.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.valapay.vala.R;
import com.valapay.vala.Vala;
import com.valapay.vala.common.FundingSourceDetails;
import com.valapay.vala.common.TransactionConfirmMessage;
import com.valapay.vala.model.User;
import com.valapay.vala.network.NetworkServices;
import com.valapay.vala.utils.RoundImage;

import java.io.IOException;

import io.card.payment.*;
import retrofit2.Response;

public class PaymentActivity extends NavigationDrawerActivity {

    private int CARD_IO_REQUEST_CODE = 1;
    private static final String RECIPIENT_KEY = "RECIPIENT_KEY";
    private static final String AMOUNT_KEY = "AMOUNT_KEY";
    private static final String CURRENCY_KEY = "CURRENCY_KEY";
    private static final String FEE_KEY = "FEE_KEY";

    private String recipient;
    private int amount;
    private double fee;
    private String currency;
    private ConfirmPaymentTask confirmPaymentTask = null;
    private Button mContinueButton;
    private TextView mCardTextView;
    private CreditCard creditCard;
    private View mProgressBar;
    private TextView amountView;
    private Button okBtn;
    private static String trackingNumber;

    public static void startPaymentActivity(String currency, double fee, String recipient, int amount, Activity context){

        Intent intent = new Intent(context, PaymentActivity.class);
        intent.putExtra(RECIPIENT_KEY, recipient);
        intent.putExtra(AMOUNT_KEY, amount);
        intent.putExtra(FEE_KEY, fee);
        intent.putExtra(CURRENCY_KEY, currency);
        context.startActivity(intent);
    }

    private void showProgress(final boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        okBtn.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipient = getIntent().getStringExtra(RECIPIENT_KEY);
        amount = getIntent().getIntExtra(AMOUNT_KEY, 0);
        currency = getIntent().getStringExtra(CURRENCY_KEY);
        fee = getIntent().getDoubleExtra(FEE_KEY, 0);

        mProgressBar = findViewById(R.id.payment_progress);
        amountView = (TextView) findViewById(R.id.amount_text);
        TextView feeView = (TextView) findViewById(R.id.fee_text);
        TextView totalView = (TextView) findViewById(R.id.total_text);
        mCardTextView = (TextView) findViewById(R.id.card_text);

        String amountStr = currency + amount;
        Spannable wordToSpan = new SpannableString(amountStr);
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), currency.length(), wordToSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        amountView.setText(wordToSpan);

        String feeStr = getString(R.string.payment_fee, currency + fee);
        wordToSpan = new SpannableString(feeStr);
        int startIndex = feeStr.indexOf("pay") + 4;
        wordToSpan.setSpan(new ForegroundColorSpan(Color.rgb(242, 160, 104)), startIndex, startIndex + currency.length() + String.valueOf(fee).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), startIndex + currency.length(), startIndex + currency.length() + String.valueOf(fee).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        feeView.setText(wordToSpan);

        double total = amount - fee;
        String totalText = recipient + " " + getString(R.string.payment_total) + " " + currency + total;
        wordToSpan = new SpannableString(totalText);
        startIndex = totalText.indexOf(currency);
        wordToSpan.setSpan(new ForegroundColorSpan(Color.rgb(0, 153, 219)), startIndex, startIndex + currency.length() + String.valueOf(total).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), startIndex + currency.length(), startIndex + currency.length() + String.valueOf(total).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, recipient.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        totalView.setText(wordToSpan);

        okBtn = (Button) findViewById(R.id.buttonPaymentOk);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confirmPaymentTask == null){
                    showProgress(true);
                    confirmPaymentTask = new ConfirmPaymentTask(creditCard);
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

        mContinueButton = (Button) findViewById(R.id.buttonPayment);
        mContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScanCreditCard();
            }
        });
    }

    private void showCreditCardDetails(Intent data){


        String resultStr = "Failed to scan credit card";
        if (data != null && data.hasExtra(io.card.payment.CardIOActivity.EXTRA_SCAN_RESULT)) {
            creditCard = data.getParcelableExtra(io.card.payment.CardIOActivity.EXTRA_SCAN_RESULT);
            resultStr = "Card Number: " + creditCard.getRedactedCardNumber() + "\n";
            if (creditCard.isExpiryValid()) {
                resultStr += "Expiration Date: " + creditCard.expiryMonth + "/" + creditCard.expiryYear + "\n";
            }
            if (creditCard.cvv != null) {
                resultStr += "CVV: " + creditCard.cvv + "\n";
            }
            if (creditCard.cardholderName != null) {
                resultStr += "Cardholder Name: " + creditCard.cardholderName + "\n";
            }
            // Update UI after successful scan
            LinearLayout layout = (LinearLayout) findViewById(R.id.LayoutConfirmPayment);
            layout.setVisibility(View.VISIBLE);
            mContinueButton.setVisibility(View.GONE);
            ImageView steps = (ImageView) findViewById(R.id.stepsImage);
            steps.setImageDrawable(getDrawable(R.drawable.stepstabs_confirm));
        }
        mCardTextView.setText(resultStr);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCardTextView.setVisibility(View.VISIBLE);
        if(resultCode == RESULT_CANCELED){
            mCardTextView.setText("Scan was canceled.");
        }else{
            showCreditCardDetails(data);
        }
    }

    private void openScanCreditCard(){

        Intent scanIntent = new Intent(this, io.card.payment.CardIOActivity.class);
        scanIntent.putExtra(io.card.payment.CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
        scanIntent.putExtra(io.card.payment.CardIOActivity.EXTRA_REQUIRE_CVV, true);
        scanIntent.putExtra(io.card.payment.CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, true);

        startActivityForResult(scanIntent, CARD_IO_REQUEST_CODE);
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

            ImageView userImage = (ImageView) root.findViewById(R.id.imageViewUser);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.babu);
            RoundImage roundedImage = new RoundImage(bm);
            userImage.setImageDrawable(roundedImage);

            String recipient = getActivity().getIntent().getStringExtra(RECIPIENT_KEY);
            TextView recipientTextView = (TextView) root.findViewById(R.id.textViewName);
            recipientTextView.setText(getString(R.string.payment_confirmation_top_2, recipient));
            Spannable wordToSpan = new SpannableString(recipientTextView.getText().toString());
            wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, recipient.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            recipientTextView.setText(wordToSpan);

            TextView trackingNumberTextView = (TextView) root.findViewById(R.id.textViewTrackingNumber);
            SpannableString content = new SpannableString(trackingNumber);
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            trackingNumberTextView.setText(content);

            return root;
        }

    }

    public class ConfirmPaymentTask extends AsyncTask<Void, Void, Boolean> {

        private CreditCard cc;

        public ConfirmPaymentTask(CreditCard cc){
            this.cc = cc;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Response<TransactionConfirmMessage> response = null;
            TransactionConfirmMessage requestBody = new TransactionConfirmMessage();
            User user = Vala.getUser();
            requestBody.setTransactionId(user.getTransactionId());
            FundingSourceDetails fsd = new FundingSourceDetails();
//            fsd.setCardHolderName(cc.cardholderName); //TODO uncomment
//            fsd.setCardNumber(cc.cardNumber); //TODO uncomment
//            fsd.setCcv(cc.cvv); //TODO uncomment
//            fsd.setExpiresMonth(String.valueOf(cc.expiryMonth)); //TODO uncomment
//            fsd.setExpiresYear(String.valueOf(cc.expiryYear)); //TODO uncomment
//            fsd.setType(cc.getCardType().toString()); //TODO uncomment
            fsd.setCardHolderName("Moshe Cohen"); //TODO remove
            fsd.setCardNumber("4580123412341234"); //TODO remove
            fsd.setCcv("123"); //TODO remove
            fsd.setExpiresMonth(String.valueOf(1)); //TODO remove
            fsd.setExpiresYear(String.valueOf(20)); //TODO remove
            fsd.setType(cc.getCardType().toString()); //TODO remove
            requestBody.setFundingSourceDetails(fsd); //TODO remove
            try {
                response = NetworkServices.getTestService().confirmTransaction(requestBody).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(response.isSuccessful()){
                TransactionConfirmMessage data = response.body();
                trackingNumber = data.getTrackingNumber();
                return true;
            }else{
                Log.d("VALA", "PaymentActivity:v.doInBackground() - confirm transaction failed");
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            confirmPaymentTask = null;
            if (success) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                PaymentConfirmationDialogFragment frag = new PaymentConfirmationDialogFragment();
                frag.show(ft, "payment_confirmation_fragment_tag");

            } else {
                Toast.makeText(PaymentActivity.this, R.string.general_error_message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
            confirmPaymentTask = null;
        }
    }
}
