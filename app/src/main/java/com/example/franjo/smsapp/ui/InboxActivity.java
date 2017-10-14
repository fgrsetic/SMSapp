package com.example.franjo.smsapp.ui;


import android.Manifest;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.franjo.smsapp.R;
import com.example.franjo.smsapp.adapters.InboxAdapter;
import com.example.franjo.smsapp.model.SMSData;
import com.example.franjo.smsapp.receiver.HeadlessSmsSendService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class InboxActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private ListView listViewInbox;
    private InboxAdapter arrayAdapter;
    private Cursor cursor;
    private FloatingActionButton fabInbox;
    private final ArrayList<SMSData> smsList = new ArrayList<>();
    private final ArrayList<SMSData> selection_list = new ArrayList<>();
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private String activityTitle;
    private static final String FULL_SMS = "full_sms";
    private static SharedPreferences sharedPreferences;
    private static InboxActivity inst;
    public static boolean active = false;

    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;


    public static InboxActivity instance() {
        return inst;
    }



    //Called when activity is first created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbarInbox = (Toolbar) findViewById(R.id.toolbarInbox);
        setSupportActionBar(toolbarInbox);

        this.startService(new Intent(this, HeadlessSmsSendService.class));

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        activityTitle = getTitle().toString();

        PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPermissionToReadContacts();
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPermissionToReadSMS();
            }

        } else {
            refreshSmsInbox();
        }

        initWidgets();
        setInboxAdapter();
        setUpListeners();
        setupDrawer();

    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
        inst = this;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToReadContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);

        }
    }


    // We’re checking to see whether the permission is granted already
    // if it’s not, we’re checking whether we need to explain the situation to the user.
    // If so, then we’re displaying a toast message and either way, we’re then actually doing the asking.
    public void getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_SMS)) {
                    Toast.makeText(this, "Please allow permission!", Toast.LENGTH_SHORT).show();
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_SMS},
                        READ_SMS_PERMISSIONS_REQUEST);
            }
        }
    }

    // We handle the response via onRequestPermissionResult.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Our toast message confirms the answer and if it is positive, we’re then using our next new method,
                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
                refreshSmsInbox();
            } else {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void initWidgets() {
        listViewInbox = (ListView) findViewById(R.id.listViewInbox); // reference of the listview, link with the XML file.
//        listViewInbox.setTextFilterEnabled(true); // use to enable search view popup text
        fabInbox = (FloatingActionButton) findViewById(R.id.fabInbox);
        listViewInbox.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

    }


    private void refreshSmsInbox() {
        // Uri to get messages from the inbox and we’re grabbing the body, address and date
        Uri uri = Uri.parse("content://sms/inbox");
        //Get Content Resolver object, which will deal with Content Provider
        ContentResolver cr = getContentResolver();
        //List required columns
        String[] projection = new String[]{"address", "body", "date"};
        // Fetch SMS Message from Built-in Content Provider
        Cursor smsInboxCursor = cr.query(uri, projection, null, null, "date DESC");

        // Read the sms data and store it in the list
        if (smsInboxCursor != null && smsInboxCursor.moveToFirst()) {
            // Cursor to go through each message
            for (int i = 0; i < smsInboxCursor.getCount(); i++) {
                SMSData sms = new SMSData();
                String number = smsInboxCursor.getString(smsInboxCursor.getColumnIndexOrThrow("address"));
                String name = getContactName(this, number);

                String body = smsInboxCursor.getString(smsInboxCursor.getColumnIndexOrThrow("body"));
                sms.setBody(body);

                if( name == null )
                    sms.setNumber(number);
                else
                    sms.setNumber(name);

                String date = smsInboxCursor.getString(smsInboxCursor.getColumnIndexOrThrow("date"));
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(Long.parseLong((date)));
                String format = "dd.MM.";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                String dateString = sdf.format(cal.getTime());
                sms.setDate(dateString);

                String minute = smsInboxCursor.getString(smsInboxCursor.getColumnIndexOrThrow("date"));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong((minute)));
                String formatMinute = "hh:mm";
                SimpleDateFormat sdfM = new SimpleDateFormat(formatMinute, Locale.getDefault());
                String dateStringMinute = sdfM.format(cal.getTime());
                sms.setMinute(dateStringMinute);

                sms.setContactImage();
                smsList.add(sms);

                smsInboxCursor.moveToNext();
            }
        }
    }


    private String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        if (cursor == null) {
            return phoneNumber;
        }
        String contactName = phoneNumber;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }

    private void setInboxAdapter() {
        // Passing data items to the constructor of the custom adapter
        arrayAdapter = new InboxAdapter(InboxActivity.this, smsList);
        // Setting the adapter on our listview
        listViewInbox.setAdapter(arrayAdapter);
    }

    public void updateInbox(SMSData newSms) {
        arrayAdapter.insert(newSms, 0);
        arrayAdapter.notifyDataSetChanged();

    }

    private void setUpListeners() {
        fabInbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(InboxActivity.this, NewMessage.class);
                startActivity(i);
            }
        });

        listViewInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(InboxActivity.this, SMSDetailsInbox.class);

                // move the cursor to required position
                if (cursor != null) {
                    cursor.moveToPosition(position);
                }

                String senderNumber = null;
                if (cursor != null) {
                    senderNumber = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                }
                intent.putExtra("broj", senderNumber);

                String senderNumberName = ((TextView) view.findViewById(R.id.txtBroj)).getText().toString();
                intent.putExtra("name", senderNumberName);

                String smsBody = ((TextView) view.findViewById(R.id.txtPoruka)).getText().toString();
                intent.putExtra("poruka", smsBody);

                String dateTime = ((TextView) view.findViewById(R.id.txtVrijeme)).getText().toString();
                intent.putExtra("vrijeme", dateTime);

                String minute = ((TextView) view.findViewById(R.id.txtMinute)).getText().toString();
                intent.putExtra("minute", minute);

                startActivity(intent);


        }

        });

        listViewInbox.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int position, long l, boolean checked) {

                int counter = listViewInbox.getCheckedItemCount();
                actionMode.setTitle(counter + " Odabrano");
                selection_list.add(smsList.get(position));

            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                // Inflate the options menu from XML
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.menu_delete, menu);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {

                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete:
                        AlertDialog.Builder alert = new AlertDialog.Builder(InboxActivity.this);
                        alert.setIconAttribute(android.R.attr.alertDialogIcon);
                        alert.setTitle(R.string.permanently_message_delete);
                        alert.setMessage(R.string.delete_message);
                        alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (final SMSData sms : selection_list) {
                                    arrayAdapter.remove(sms);
                                    arrayAdapter.notifyDataSetChanged();


                                }
                            }
                        });

                        alert.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });



                        alert.create();
                        alert.show();

                        actionMode.finish();


                        return true;

                    default:
                        return false;
                }

            }


            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                actionMode.finish();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_inbox, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
//
//                if (TextUtils.isEmpty(newText)) {
//                    listViewInbox.clearTextFilter();
//                }
//                else {
//                    listViewInbox.setFilterText(newText);
//                }
                return true;
            }
        });


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.PostavkeTelefona:
                break;

            default:
                break;

        }
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.

        switch (item.getItemId()) {
            case R.id.nav_poslano:

                Intent i = new Intent(InboxActivity.this, SentActivity.class);
                startActivity(i);
                drawerLayout.closeDrawer(GravityCompat.START);

                break;
                    case R.id.nav_settings:

                       Intent in = new Intent(InboxActivity.this, SettingsActivity.class);
                       startActivity(in);
                       drawerLayout.closeDrawer(GravityCompat.START);

                        break;

                    default:
                        break;

                }
        return true;
    }

    private void setupDrawer() {

        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /**
             * Called when a drawer has settled in a completely closed state.
             */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //noinspection ConstantConditions
                getSupportActionBar().setTitle(activityTitle);
                // creates call to onPrepareOptionsMenu()
                invalidateOptionsMenu();
            }

        };

        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(toggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
        }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
        }

    //Closing drawer on backpress
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setSettings(MenuItem item) {
        startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);

    }

    @Override
    public void onResume(){
        super.onResume();
        setInboxAdapter();

    }

    public static boolean showFullText() {
        return sharedPreferences.getBoolean(FULL_SMS, true);
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }

    }

}