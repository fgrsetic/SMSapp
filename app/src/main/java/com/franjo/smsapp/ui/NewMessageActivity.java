package com.franjo.smsapp.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.franjo.smsapp.R;


public class NewMessageActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private EditText etRecepient;
    private EditText etMessage;
    private TextView sms_count;
    private static final int CONTACT_PICKER_RESULT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        Toolbar toolbarNewMessage = (Toolbar) findViewById(R.id.toolbarNewMessage);
        setSupportActionBar(toolbarNewMessage);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Softkayboard on starting activity NewMessageActivity
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etMessage, InputMethodManager.SHOW_IMPLICIT);

        initWidgets();
        setupListeners();
        countSms();

    }


    private void initWidgets() {
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        etRecepient = (EditText) findViewById(R.id.etReceipent);
        etMessage = (EditText) findViewById(R.id.etMessageText);
        etMessage.requestFocus();
        sms_count = (TextView) findViewById(R.id.sms_count);

    }

    private void setupListeners() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasNumber() && hasText()) {
                    String phoneNumber = etRecepient.getText().toString();
                    String poruka = etMessage.getText().toString();
                    finish();

                    try {
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(phoneNumber, null, poruka, null, null);

                        Toast.makeText(getApplicationContext(), R.string.message_sent, Toast.LENGTH_LONG).show();

                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), R.string.sending_failed, Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

        });


    }

    private boolean hasText() {
        if (etMessage.getText().length() == 0) {
            Toast.makeText(this, R.string.message_is_missing, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean hasNumber() {
        if (etRecepient.getText().length() == 0) {
            Toast.makeText(this, R.string.enter_recipients, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void countSms() {

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int aft) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                // This will show characters remaining
                sms_count.setText(160 - s.toString().length() + getString(R.string.last_letter_count));
            }
        });

    }

    public void pickContacts(View view) {

        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            Uri contactData = data.getData();
            ContentResolver contentResolver = getContentResolver(); // Get Content Resolver object, which will deal with Content Provider
            Cursor cursor = contentResolver.query(contactData, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {

                String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                    if (pCur != null) {

                        while (pCur.moveToNext()) {
                            String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String name = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                            etRecepient.setText(phone);




                        }
                        cursor.close();
                        pCur.close();


                    }

                }

            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);

        }

        return super.onOptionsItemSelected(item);
    }


}














