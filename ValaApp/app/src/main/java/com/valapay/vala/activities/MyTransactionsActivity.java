package com.valapay.vala.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.valapay.vala.R;
import com.valapay.vala.Vala;
import com.valapay.vala.common.TransactionHistoryMessage;
import com.valapay.vala.network.NetworkServices;
import com.valapay.vala.utils.RoundImage;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class MyTransactionsActivity extends NavigationDrawerActivity {

    private Transaction[] receivedTransactions;
    private Transaction[] sentTransactions;

    private View mSentList;
    private View mReceivedList;
    private View mSentProgress;
    private View mReceivedProgress;

    private GetTransactionsTask getTransactionsTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView title = (TextView) findViewById(R.id.textViewToolbarTitle);
        title.setText(R.string.my_transactions_title);

        mReceivedList = findViewById(R.id.listViewReceived);
        mReceivedProgress = findViewById(R.id.progressReceived);
        mSentList = findViewById(R.id.listViewSent);
        mSentProgress = findViewById(R.id.progressSent);

//        populateDummyLists();
        if(getTransactionsTask == null){
            showProgress(true);
            getTransactionsTask = new GetTransactionsTask();
            getTransactionsTask.execute((Void) null);
        }
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

    @Override
    protected int getContentId() {
        return R.layout.activity_my_transactions;
    }

    @Override
    protected boolean shouldDisplayLogo() {
        return false;
    }

    public class TransactionAdapter extends BaseAdapter {

        Transaction [] transactions;
        Context context;

        public TransactionAdapter(Context context, Transaction[] transactions) {
            this.context = context;
            this.transactions = transactions;
        }

        @Override
        public int getCount() {
            return transactions.length;
        }

        //TODO
        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_item_transaction, null);
            TextView date = (TextView) rowView.findViewById(R.id.textViewDate);
            TextView desc = (TextView) rowView.findViewById(R.id.textViewDesc);
            TextView status = (TextView) rowView.findViewById(R.id.textViewStatus);
            ImageView image =(ImageView) rowView.findViewById(R.id.image);
            Transaction t = transactions[position];
            String descText = transactions[position].isSent ?
                    getString(R.string.my_transactions_sent, t.getAmount(), t.getName()) :
                    getString(R.string.my_transactions_received, t.getAmount(), t.getName());
            Spannable wordToSpan = new SpannableString(descText);
            wordToSpan.setSpan(new StyleSpan(Typeface.BOLD), 1, t.getAmount().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            wordToSpan.setSpan(new ForegroundColorSpan(Color.rgb(0, 172, 163)), 0, t.getAmount().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            desc.setText(wordToSpan);
            status.setText(t.getStatus());
            date.setText(t.getDate());
            RoundImage roundedImage = new RoundImage(t.getImage());
            image.setImageDrawable(roundedImage);
            return rowView;
        }
    }

    public class Transaction {

        private String name;
        private Bitmap image;
        private String amount;
        private String date;
        private boolean isSent;
        private String status;

        public Transaction(String name, Bitmap image, String amount, String date, boolean isSent, String status) {
            this.name = name;
            this.image = image;
            this.amount = amount;
            this.date = date;
            this.isSent = isSent;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public Bitmap getImage() {
            return image;
        }

        public String getAmount() {
            return amount;
        }

        public String getDate() {
            return date;
        }

        public boolean isSent() {
            return isSent;
        }

        public String getStatus() {
            return status;
        }
    }

    private class GetTransactionsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            Response<TransactionHistoryMessage> response = null;
            try {
                response = NetworkServices.getTestService().getTransactionList(Vala.getUser().getUserId()).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response.isSuccessful()) {
                Log.d("VALA", "MyTransactionsActivity:GetTransactionsTask.doInBackground() - get transactions succeeded");
                populateDummyResponse(response.body()); //TODO remove this when the server is ready
                populateLists(response.body());
                return true;
            } else {
                Log.d("VALA", "MyTransactionsActivity:GetTransactionsTask.doInBackground() - get transactions failed");
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            getTransactionsTask = null;
            showProgress(false);
            if (success) {
                ListView receivedList = (ListView) findViewById(R.id.listViewReceived);
                receivedList.setAdapter(new TransactionAdapter(MyTransactionsActivity.this, receivedTransactions));
                ListView sentList = (ListView) findViewById(R.id.listViewSent);
                sentList.setAdapter(new TransactionAdapter(MyTransactionsActivity.this, sentTransactions));
            } else {
                Toast.makeText(MyTransactionsActivity.this, R.string.general_error_message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            getTransactionsTask = null;
            showProgress(false);
        }
    }

    private void showProgress(final boolean show) {
        mSentProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        mSentList.setVisibility(show ? View.GONE : View.VISIBLE);
        mReceivedProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        mReceivedList.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void populateLists(TransactionHistoryMessage response){

        TransactionHistoryMessage.Transaction[] received = response.getReceivedTransactions();
        TransactionHistoryMessage.Transaction[] sent = response.getSentTransactions();

        receivedTransactions = new Transaction[received.length];
        sentTransactions = new Transaction[sent.length];

        //TODO get image from cache according to id
        Bitmap bm = Vala.getUser().getImageBitmap();

        for(int i = 0; i < receivedTransactions.length; i++){
            TransactionHistoryMessage.Transaction t = received[i];
            receivedTransactions[i] = new Transaction(t.getName(), bm, t.getAmount(), t.getDate(), false, t.getStatus());
        }

        for(int i = 0; i < sentTransactions.length; i++){
            TransactionHistoryMessage.Transaction t = sent[i];
            sentTransactions[i] = new Transaction(t.getName(), bm, t.getAmount(), t.getDate(), true, t.getStatus());
        }
    }

    //TODO delete
    private static void populateDummyResponse(TransactionHistoryMessage response){
        TransactionHistoryMessage.Transaction[] sentTransactions = new TransactionHistoryMessage.Transaction[3];
        TransactionHistoryMessage.Transaction[] receivedTransactions = new TransactionHistoryMessage.Transaction[1];
        TransactionHistoryMessage.Transaction t1 = new TransactionHistoryMessage.Transaction("Kumar", "$ 100", "19/03/2016", "Kumar is notified", "abc");
        TransactionHistoryMessage.Transaction t2 = new TransactionHistoryMessage.Transaction("Moshe", "$ 50", "21/03/2016", "Moshe is on his way to pick up the cash", "abc");
        TransactionHistoryMessage.Transaction t3 = new TransactionHistoryMessage.Transaction("Ashish", "$ 200", "21/03/2016", "Ashish has got the cash", "acb");
        sentTransactions[0] = t1;
        sentTransactions[1] = t2;
        sentTransactions[2] = t3;
        TransactionHistoryMessage.Transaction t4 = new TransactionHistoryMessage.Transaction("Haim", "$ 200", "21/03/2016", "You got the cash", "abcs");
        receivedTransactions[0] = t4;
        response.setReceivedTransactions(receivedTransactions);
        response.setSentTransactions(sentTransactions);
    }

    //TODO delete
    private void populateDummyLists(){

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.babu);

        sentTransactions = new Transaction[3];
        receivedTransactions = new Transaction[1];
        Transaction t1 = new Transaction("Kumar", bm, "$ 100", "19/03/2016", true, "Kumar is notified");
        Transaction t2 = new Transaction("Moshe", bm, "$ 50", "21/03/2016", true, "Moshe is on his way to pick up the cash");
        Transaction t3 = new Transaction("Ashish", bm, "$ 200", "21/03/2016", true, "Ashish has got the cash");
        sentTransactions[0] = t1;
        sentTransactions[1] = t2;
        sentTransactions[2] = t3;
        Transaction t4 = new Transaction("Haim", bm, "$ 200", "21/03/2016", false, "You got the cash");
        receivedTransactions[0] = t4;
    }
}
