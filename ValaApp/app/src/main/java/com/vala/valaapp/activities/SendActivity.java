package com.vala.valaapp.activities;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.vala.valaapp.R;

public class SendActivity extends NavigationDrawerActivity {

    TextView mSelectReceiver;
    EditText mAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                InviteDialogFragment frag = new InviteDialogFragment();
                frag.show(ft, "invite_fragment_tag");
            }
        });
         Button sendBtn = (Button) findViewById(R.id.send_button);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence recipient = mSelectReceiver.getText();
                CharSequence amount = mAmount.getText();
                //TODO check null? display error
                if(!recipient.equals("") && !amount.equals("")){

                    PaymentActivity.startPaymentActivity(recipient.toString(), Integer.parseInt(amount.toString()), SendActivity.this);
                }
            }
        });
    }

    @Override
    protected int getContentId() {
        return R.layout.activity_send;
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

    static public class InviteDialogFragment extends DialogFragment {

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

            Button cancelBtn = (Button) root.findViewById(R.id.btn_cancel);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDialog().dismiss();
                }
            });

            final RadioGroup radioGroup = (RadioGroup) root.findViewById(R.id.radio_group);

            Button inviteBtn = (Button) root.findViewById(R.id.invite_button);
            inviteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // layout params to use when adding each radio button
                    LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                            RadioGroup.LayoutParams.WRAP_CONTENT,
                            RadioGroup.LayoutParams.WRAP_CONTENT);

                    String[] receiversArray = getResources().getStringArray(R.array.receivers_array);
                    for (int i = 0; i < receiversArray.length; i++){
                        RadioButton newRadioButton = new RadioButton(getActivity());
                        String label = receiversArray[i];
                        newRadioButton.setText(label);
                        newRadioButton.setId(i);
                        radioGroup.addView(newRadioButton, layoutParams);
                    }

                }
            });

            Button selectBtn = (Button) root.findViewById(R.id.btn_select);
            selectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    RadioButton radioButton = (RadioButton) radioGroup.findViewById(radioButtonID);
                    TextView selectReceiver = (TextView) getActivity().findViewById(R.id.select_receiver);
                    CharSequence receiver = radioButton != null ? radioButton.getText() : "";
                        selectReceiver.setText(receiver);
                    getDialog().dismiss();
                }
            });


            return root;
        }

    }

}
