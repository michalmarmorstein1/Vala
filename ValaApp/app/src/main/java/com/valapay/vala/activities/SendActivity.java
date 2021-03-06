package com.valapay.vala.activities;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.valapay.vala.R;
import com.valapay.vala.Vala;
import com.valapay.vala.common.TransactionMessage;
import com.valapay.vala.common.UserNameQueryResult;
import com.valapay.vala.common.UserQueryMessage;
import com.valapay.vala.common.UserRcvrListMessage;
import com.valapay.vala.model.Recipient;
import com.valapay.vala.model.User;
import com.valapay.vala.network.NetworkServices;
import com.valapay.vala.utils.RoundImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Response;

public class SendActivity extends NavigationDrawerActivity {

    private TextView mSelectReceiver;
    private EditText mAmount;
    private View mProgressView;
    private ImageView mUserImage;
    private SendMoneyTask mSendMoneyTask = null;
    private static String currency;
    private User user;
    private static int selectedReceiverIndex;
    public static ArrayList<Recipient> searchResultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = Vala.getUser();
        mProgressView = findViewById(R.id.send_progress);
        mAmount = (EditText) findViewById(R.id.amount_editText);
        mUserImage = (ImageView) findViewById(R.id.userImage);

        final Spinner currencySpinner = (Spinner) findViewById(R.id.send_currency_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currency = currencySpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                currency = "USD";
            }

        });
        currencySpinner.setAdapter(adapter);

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
                if(amount.equals("")){
                    Toast.makeText(SendActivity.this, getString(R.string.send_amount_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(recipient.equals("")){
                    Toast.makeText(SendActivity.this, getString(R.string.send_receiver_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mSendMoneyTask == null){
                    showProgress(true);
                    mSendMoneyTask = new SendMoneyTask(recipient.toString(), Integer.parseInt(amount.toString()));
                    mSendMoneyTask.execute((Void) null);
                }
            }
        });

        RoundImage roundedImage = new RoundImage(user.getImageBitmap());
        mUserImage.setImageDrawable(roundedImage);

        TextView tv = (TextView) findViewById(R.id.textViewName);
        String str = getString(R.string.send_name, user.getFirstName());
        Spannable wordToSpan = new SpannableString(str);
        wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), str.indexOf(user.getFirstName()), wordToSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(wordToSpan);
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mUserImage.setVisibility(show ? View.GONE: View.VISIBLE);
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
        View mFragmentProgressBar;
        View mFragmentImage;
        TextView mListHeader;
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

            // create ContextThemeWrapper from the original Activity Context with the custom theme
            final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.GreenTheme);
            // clone the inflater using the ContextThemeWrapper
            LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
            // inflate the layout using the cloned inflater, not default inflater
            final View root = localInflater.inflate(R.layout.fragment_select_receiver, container, true);
            mSelectBtn = (Button) root.findViewById(R.id.btnSelect);
            mCancelBtn = (Button) root.findViewById(R.id.btnCancel);
            mSearchBtn = (Button) root.findViewById(R.id.btnSearch);
            mAddReceiverBtn = (Button) root.findViewById(R.id.btnAddReceiver);
            mRadioGroup = (RadioGroup) root.findViewById(R.id.radio_group);
            mUserNameET = (EditText) root.findViewById(R.id.editTextUsername);
            mFragmentProgressBar = root.findViewById(R.id.search_progress);
            mFragmentImage = root.findViewById(R.id.imageSelectReceiver);
            mListHeader = (TextView) root.findViewById(R.id.textListHeader);
            searchResultList = new ArrayList<Recipient>();
            refreshReceiversList(Vala.getUser().getRecipients());

            mCancelBtn.setOnClickListener(new MainCancelListener());

            mSearchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mSearchReceiverTask == null){
                        showProgress(true);
                        mSearchReceiverTask = new SearchReceiverTask(mUserNameET.getText().toString());
                        mSearchReceiverTask.execute();
                    }
                }
            });

            mAddReceiverBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int radioButtonID = mRadioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) mRadioGroup.findViewById(radioButtonID);
                    if(radioButton == null){
                        Toast.makeText(getActivity(), getString(R.string.select_receiver_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setSearchMode(false);
                    User user = Vala.getUser();
                    Recipient recipient = searchResultList.get(radioButton.getId());
                    user.addRecipient(recipient);
                    refreshReceiversList(user.getRecipients());
                    new AddRecipientTask(recipient.getId()).execute();
                }
            });

            mSelectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int radioButtonID = mRadioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) mRadioGroup.findViewById(radioButtonID);
                    if(radioButton == null){
                        Toast.makeText(getActivity(), getString(R.string.select_receiver_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    TextView selectReceiver = (TextView) getActivity().findViewById(R.id.select_receiver);
                    selectedReceiverIndex = radioButton.getId();
                    selectReceiver.setText(Vala.getUser().getRecipients().get(selectedReceiverIndex).getName());
                    getDialog().dismiss();
                }
            });

            return root;
        }

        private void setSearchMode(boolean isSearchMode){

            mAddReceiverBtn.setVisibility(isSearchMode ? View.VISIBLE : View.GONE);
            mSelectBtn.setVisibility(isSearchMode ? View.GONE : View.VISIBLE);
            mCancelBtn.setOnClickListener(isSearchMode ? new SearchCancelListener()
                    : new MainCancelListener());
            mListHeader.setText(isSearchMode ? getString(R.string.invite_search_results)
                    : getString(R.string.invite_my_receivers));
        }

        private class MainCancelListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        }

        private class SearchCancelListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                setSearchMode(false);
            }
        }

        private void showProgress(final boolean show) {
            mFragmentProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mFragmentImage.setVisibility(show ? View.GONE: View.VISIBLE);
        }

        public void refreshReceiversList(ArrayList<Recipient> list){

            mRadioGroup.removeAllViews();
            // layout params to use when adding eachR radio button
            LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < list.size(); i++){
                Recipient recipient = list.get(i);
                RadioButton newRadioButton = new RadioButton(getActivity());
                newRadioButton.setText(recipient.getName());
                newRadioButton.setId(i);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT, 1f);
                params.gravity = Gravity.LEFT;
                newRadioButton.setLayoutParams(params);
                Drawable avatarImg = new BitmapDrawable(getResources(),
                        recipient.getImage(BitmapFactory.decodeResource(getResources(), R.drawable.babu)));
                avatarImg.setBounds(0, 0, 100, 100);
                Drawable radioImg = getActivity().getDrawable(R.drawable.radio_btn);
                newRadioButton.setCompoundDrawables(avatarImg, null, null, null);
                newRadioButton.setCompoundDrawablePadding(40);
                newRadioButton.setButtonDrawable(radioImg);
                newRadioButton.setPadding(30,30,0,30);
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
                Response<UserQueryMessage> searchResponse = null;
                UserQueryMessage requestBody = new UserQueryMessage();
                requestBody.setQuery(mUserName);
                if(mUserName.contains("@")){
                    requestBody.setQueryType(UserQueryMessage.QueryType.EMAIL);
                }else{
                    requestBody.setQueryType(UserQueryMessage.QueryType.NAME);
                }
                try {
                    searchResponse = NetworkServices.getTestService().searchUser(requestBody).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(searchResponse.isSuccessful()){
                    UserQueryMessage data = searchResponse.body();
                    searchResultList.clear();
                    for(UserNameQueryResult result : data.getNames()){
                        searchResultList.add(new Recipient(result.getName(), result.getUserId()));
                    }
                    return true;
                }else{
                    Log.d("VALA", "SendActivity:SearchReceiverTask.doInBackground() - search failed");
                    return false;
                }
            }

            @Override
            protected void onPostExecute(final Boolean success) {
                mSearchReceiverTask = null;
                showProgress(false);
                if (success) {
                    refreshReceiversList(searchResultList);
                    mRadioGroup.clearCheck();
                    setSearchMode(true);
                } else {
                    Toast.makeText(getActivity(), R.string.general_error_message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void onCancelled() {
                mSearchReceiverTask = null;
                showProgress(false);
            }
        }

        public class AddRecipientTask extends AsyncTask<Void, Void, Void> {

            private String mReceiver;

            AddRecipientTask(String receiver) {
                mReceiver = receiver;
            }

            @Override
            protected Void doInBackground(Void... params) {
                Response<UserRcvrListMessage> addRecipientResponse = null;
                UserRcvrListMessage requestBody = new UserRcvrListMessage();
                requestBody.setAction(UserRcvrListMessage.Action.ADD);
                requestBody.setRcvrId(mReceiver);
                requestBody.setUserId(Vala.getUser().getUserId());
                try {
                    addRecipientResponse = NetworkServices.getTestService().addRecipient(requestBody).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(!addRecipientResponse.isSuccessful()){
                    Log.d("VALA", "SendActivity:AddRecipientTask.doInBackground() - add recipient failed");
                }
                return null;
            }

        }
    }

    public class SendMoneyTask extends AsyncTask<Void, Void, Boolean> {

        private String mReceiver;
        private int mAmount;
        private double mFee;

        SendMoneyTask(String receiver, int amount) {
            mReceiver = receiver;
            mAmount = amount;
            mFee = 3;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Response<TransactionMessage> response = null;
            TransactionMessage requestBody = new TransactionMessage();
            requestBody.setSenderId(user.getUserId());
            requestBody.setReceiverId(user.getRecipients().get(selectedReceiverIndex).getId());
            requestBody.setAmount(String.valueOf(mAmount));
            requestBody.setSendCurrency(currency);
            requestBody.setTransactionCreationTimeMilliseconds(new Date().getTime());
            try {
                response = NetworkServices.getTestService().createTransaction(requestBody).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(response.isSuccessful()){
                TransactionMessage data = response.body();
                user.setTransactionId(data.getTransactionId());
                mFee = Double.valueOf(data.getPriceResponse().getValaFee().getAmount());
                return true;
            }else{
                Log.d("VALA", "SendActivity:SendMoneyTask.doInBackground() - transaction failed");
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mSendMoneyTask = null;
            showProgress(false);
            if (success) {
                PaymentActivity.startPaymentActivity(getCurrencySymbol(), mFee, mReceiver, mAmount, SendActivity.this);
            } else {
                Toast.makeText(SendActivity.this, R.string.general_error_message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mSendMoneyTask = null;
            showProgress(false);
        }
    }

    private String getCurrencySymbol(){
        if("NIS".equals(currency)){
            return 	"\u20AA";
        }
        if("INR".equals(currency)){
            return "\u20B9";
        }
        return "$";
    }
}
