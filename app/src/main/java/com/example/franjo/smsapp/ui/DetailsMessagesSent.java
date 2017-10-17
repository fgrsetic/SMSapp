package com.example.franjo.smsapp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.franjo.smsapp.R;

import java.util.Random;

public class DetailsMessagesSent extends AppCompatActivity {

    private TextView txtPorukaSent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_sms_details);

        Toolbar toolbarOutboxDetails = (Toolbar) findViewById(R.id.toolbarOutboxDetails);
        setSupportActionBar(toolbarOutboxDetails);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent i = getIntent();

        txtPorukaSent = (TextView) findViewById(R.id.txtPorukaSent);
        TextView txtVrijemeSent = (TextView) findViewById(R.id.txtVrijemeSent);
        TextView txtMinuteSent = (TextView) findViewById(R.id.txtMinuteSent);
        ImageView imageViewSent = (ImageView) findViewById(R.id.contactImageSent);


        if (i.hasExtra("name1")) {
            String name1 = i.getStringExtra("name1");
            setTitle(name1);
        }
        if (i.hasExtra("poruka1")) {
            String poruka1 = i.getStringExtra("poruka1");
            txtPorukaSent.setText(poruka1);
        }
        if(i.hasExtra("vrijeme1")) {
            String vrijeme1 = i.getStringExtra("vrijeme1");
            txtVrijemeSent.setText(vrijeme1);
        }
        if(i.hasExtra("minute1")) {
            String minute1 = i.getStringExtra("minute1");
            txtMinuteSent.setText(minute1);
        }

        Random rand = new Random();
        imageViewSent.setImageResource(R.drawable.ic_action_person_pin);

        // generate the random integers for r, g and b value
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);

        int randomColor = Color.rgb(r, g, b);
        imageViewSent.setColorFilter(randomColor, PorterDuff.Mode.MULTIPLY);


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

        }

        return super.onOptionsItemSelected(item);
    }

    public void share(MenuItem item) {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, txtPorukaSent.getText().toString());
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_via)));

    }
}

