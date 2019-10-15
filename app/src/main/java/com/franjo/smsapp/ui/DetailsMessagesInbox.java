package com.franjo.smsapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.franjo.smsapp.R;


public class DetailsMessagesInbox extends AppCompatActivity {

    private TextView txtPorukaReceived;
    private EditText etReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_sms_details);


        Toolbar toolbarInboxDetails = (Toolbar) findViewById(R.id.toolbarInboxDetails);
        setSupportActionBar(toolbarInboxDetails);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();

        txtPorukaReceived = (TextView) findViewById(R.id.txtPorukaReceived);
        TextView txtVrijemeReceived = (TextView) findViewById(R.id.txtVrijemeReceived);
        TextView txtMinuteReceived = (TextView) findViewById(R.id.txtMinuteReceived);
        ImageView imageViewReceived = (ImageView) findViewById(R.id.contactImageReceived);
        etReply = (EditText) findViewById(R.id.etReply);


        if (intent.hasExtra("name")) {
            String name = intent.getStringExtra("name");
            setTitle(name);
        }

        if (intent.hasExtra("poruka")) {
            String poruka = intent.getStringExtra("poruka");
            txtPorukaReceived.setText(poruka);
        }

        if (intent.hasExtra("vrijeme")) {
            String vrijeme = intent.getStringExtra("vrijeme");
            txtVrijemeReceived.setText(vrijeme);
        }

        if (intent.hasExtra("minute")) {
            String minute = intent.getStringExtra("minute");
            txtMinuteReceived.setText(minute);
        }

        imageViewReceived.setImageResource(R.drawable.ic_action_person_pin);

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater(); // Inflate the options menu from XML
        inflater.inflate(R.menu.menu_details, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.share:
                break;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void share(MenuItem item) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, txtPorukaReceived.getText().toString());
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));

    }

    public void reply(View view) {

        Intent intent = getIntent();

        String broj = "";

        if (intent.hasExtra("broj")) {
            broj = intent.getStringExtra("broj");

        }

        if (hasText()) {
            String poruka = etReply.getText().toString();
            finish();


            try {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(broj, null, poruka, null, null);

                Toast.makeText(getApplicationContext(), R.string.message_sent, Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), R.string.sending_failed, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }


    private boolean hasText() {
        if (etReply.getText().length() == 0) {
            Toast.makeText(this, R.string.message_is_missing, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


}


