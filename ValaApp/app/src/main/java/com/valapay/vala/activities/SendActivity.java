package com.valapay.vala.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.valapay.vala.R;
import com.valapay.vala.components.RoundImage;

import java.util.ArrayList;
import java.util.Arrays;

public class SendActivity extends NavigationDrawerActivity {

    private TextView mSelectReceiver;
    private EditText mAmount;
    private View mProgressView;
    private View mImage;
    private SendMoneyTask mSendMoneyTask = null;

    public static ArrayList<String> receiversList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mProgressView = findViewById(R.id.send_progress);
        mImage = findViewById(R.id.userImage);

        Spinner currencySpinner = (Spinner) findViewById(R.id.send_currency_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(adapter);

        mAmount = (EditText) findViewById(R.id.amount_editText);
        mSelectReceiver = (TextView) findViewById(R.id.select_receiver);
        mSelectReceiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ReceiversDialogFragment frag = new ReceiversDialogFragment();
                frag.show(ft, "receivers_fragment_tag");
            }
        });

        Button sendBtn = (Button) findViewById(R.id.send_button);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipient = mSelectReceiver.getText().toString();
                String amount = mAmount.getText().toString();
                if(recipient.equals("") && amount.equals("")){
                    Toast.makeText(SendActivity.this, getString(R.string.send_amount_receiver_error), Toast.LENGTH_LONG).show();
                    return;
                }
                if(recipient.equals("")){
                    Toast.makeText(SendActivity.this, getString(R.string.send_receiver_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(amount.equals("")){
                    Toast.makeText(SendActivity.this, getString(R.string.send_amount_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mSendMoneyTask == null){
                    showProgress(true);
                    mSendMoneyTask = new SendMoneyTask(recipient.toString(), Integer.parseInt(amount.toString()));
                    mSendMoneyTask.execute((Void) null);
                }
            }
        });

        ImageView userImage = (ImageView) findViewById(R.id.userImage);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.babu);
        RoundImage roundedImage = new RoundImage(bm);
        userImage.setImageDrawable(roundedImage);

        TextView tv = (TextView) findViewById(R.id.textName);
        Spannable wordToSpan = new SpannableString(tv.getText().toString());
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), 3, wordToSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(wordToSpan);
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mImage.setVisibility(show ? View.GONE: View.VISIBLE);
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_send;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Adds items to the action bar if it is present.
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

    static public class ReceiversDialogFragment extends DialogFragment {

        RadioGroup mRadioGroup;
        Button mSelectBtn;
        Button mCancelBtn;
        Button mSearchBtn;
        Button mAddReceiverBtn;
        EditText mUserNameET;
        ProgressBar mProgressBar;
        SearchReceiverTask mSearchReceiverTask = null;

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
            final View root = inflater.inflate(R.layout.fragment_select_receiver, container, true);
            mSelectBtn = (Button) root.findViewById(R.id.btnSelect);
            mCancelBtn = (Button) root.findViewById(R.id.btnCancel);
            mSearchBtn = (Button) root.findViewById(R.id.btnSearch);
            mAddReceiverBtn = (Button) root.findViewById(R.id.btnAddReceiver);
            mRadioGroup = (RadioGroup) root.findViewById(R.id.radio_group);
            mUserNameET = (EditText) root.findViewById(R.id.editTextUsername);
            mProgressBar = (ProgressBar) root.findViewById(R.id.search_progress);

            receiversList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.receivers_array)));
            refreshReceiversList(receiversList);

//            userNameET.setOnKeyListener(new View.OnKeyListener() {
//
//                @Override
//                public boolean onKey(View v, int keyCode, KeyEvent event) {
//                    if(((EditText)v).getText().toString().length() > 0){
//                        searchBtn.setEnabled(true);
//                    }
//                    return false;
//                }
//            });

            mCancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });

            mSearchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mSearchReceiverTask == null){
                        mRadioGroup.setVisibility(View.GONE);
                        mProgressBar.setVisibility(View.VISIBLE);
                        mSearchReceiverTask = new SearchReceiverTask(mUserNameET.getText().toString());
                        mSearchReceiverTask.execute();
                    }
                }
            });

            mAddReceiverBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mAddReceiverBtn.setVisibility(View.GONE);
                    mSelectBtn.setVisibility(View.VISIBLE);
                    int radioButtonID = mRadioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) mRadioGroup.findViewById(radioButtonID);
                    if(radioButton != null){
                        String receiver = radioButton.getText().toString();
                        receiversList.add(receiver);
                        refreshReceiversList(receiversList);
                    }
                }
            });

            mSelectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int radioButtonID = mRadioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) mRadioGroup.findViewById(radioButtonID);
                    TextView selectReceiver = (TextView) getActivity().findViewById(R.id.select_receiver);
                    CharSequence receiver = radioButton != null ? radioButton.getText() : "";
                    selectReceiver.setText(receiver);
                    getDialog().dismiss();
                }
            });

            return root;
        }

        public void refreshReceiversList(ArrayList<String> list){

            mRadioGroup.removeAllViews();
            String[] receiversArray = new String[list.size()];
            receiversArray = list.toArray(receiversArray);
            // layout params to use when adding each radio button
            LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < receiversArray.length; i++){
                RadioButton newRadioButton = new RadioButton(getActivity());
                String label = receiversArray[i];
                newRadioButton.setText(label);
                newRadioButton.setId(i);
                mRadioGroup.addView(newRadioButton, layoutParams);
            }
        }

        public class SearchReceiverTask extends AsyncTask<Void, Void, Boolean> {

            private final String mUserName;

            SearchReceiverTask(String userName) {
                mUserName = userName;
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                // TODO: search user name
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
            }

            @Override
            protected void onPostExecute(final Boolean success) {
                mSearchReceiverTask = null;
                mRadioGroup.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                if (success) {

                    refreshReceiversList(receiversList);
                    mRadioGroup.clearCheck();
                    mAddReceiverBtn.setVisibility(View.VISIBLE);
                    mSelectBtn.setVisibility(View.GONE);
                } else {
                    //TODO show server errors
                }
            }

            @Override
            protected void onCancelled() {
                mSearchReceiverTask = null;
                mRadioGroup.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }

    public class SendMoneyTask extends AsyncTask<Void, Void, Boolean> {

        private String mReceiver;
        private int mAmount;

        SendMoneyTask(String receiver, int amount) {
            mReceiver = receiver;
            mAmount = amount;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: search user name
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mSendMoneyTask = null;
            showProgress(false);
            if (success) {
                PaymentActivity.startPaymentActivity(mReceiver, mAmount, SendActivity.this);
            } else {
                //TODO show server errors
            }
        }

        @Override
        protected void onCancelled() {
            mSendMoneyTask = null;
            showProgress(false);
        }
    }
}
