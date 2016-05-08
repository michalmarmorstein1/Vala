package com.valapay.vala.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.valapay.vala.R;
import com.valapay.vala.components.RoundImage;

public class MyTransactionsActivity extends NavigationDrawerActivity {

    private Transaction[] receivedTransactions;
    private Transaction[] sentTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.my_transactions_title));
        setSupportActionBar(toolbar);
        populateLists();

        ListView receivedList = (ListView) findViewById(R.id.listViewReceived);
        receivedList.setAdapter(new TransactionAdapter(this, receivedTransactions));
        ListView sentList = (ListView) findViewById(R.id.listViewSent);
        sentList.setAdapter(new TransactionAdapter(this, sentTransactions));
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
            ImageView userImage = (ImageView) findViewById(R.id.userImage);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.babu);
            RoundImage roundedImage = new RoundImage(bm);
            image.setImageDrawable(roundedImage);
//            image.setImageResource(t.getImage());
            return rowView;
        }
    }

    public class Transaction {

        private String name;
        private int image;
        private String amount;
        private String date;
        private boolean isSent;
        private String status;

        public Transaction(String name, int image, String amount, String date, boolean isSent, String status) {
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

        public int getImage() {
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

    //TODO delete this
    private void populateLists(){

        sentTransactions = new Transaction[3];
        receivedTransactions = new Transaction[1];
        Transaction t1 = new Transaction("Kumar", R.drawable.babu, "$ 100", "19/03/2016", true, "Kumar is notified");
        Transaction t2 = new Transaction("Moshe", R.drawable.babu, "$ 50", "21/03/2016", true, "Moshe is on his way to pick up the cash");
        Transaction t3 = new Transaction("Ashish", R.drawable.babu, "$ 200", "21/03/2016", true, "Ashish has got the cash");
        sentTransactions[0] = t1;
        sentTransactions[1] = t2;
        sentTransactions[2] = t3;
        Transaction t4 = new Transaction("Haim", R.drawable.ic_menu_share, "$ 200", "21/03/2016", false, "You got the cash");
        receivedTransactions[0] = t4;
    }
}
