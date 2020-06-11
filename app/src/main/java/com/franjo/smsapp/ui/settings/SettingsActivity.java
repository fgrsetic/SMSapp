//package com.franjo.smsapp.ui;
//
//import android.os.Bundle;
//import android.view.MenuItem;
//
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.NavUtils;
//
//import com.franjo.smsapp.R;
//
//
//public class SettingsActivity extends AppCompatActivity {
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);
//
//        Toolbar toolbarSettings = (Toolbar) findViewById(R.id.toolbarSettings);
//        setSupportActionBar(toolbarSettings);
//
//        ActionBar actionBar = this.getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if(id == android.R.id.home) {
//            NavUtils.navigateUpFromSameTask(this);
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//
//}
