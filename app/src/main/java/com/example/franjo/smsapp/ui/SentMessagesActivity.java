package com.example.franjo.smsapp.ui;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
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

import com.example.franjo.smsapp.R;
import com.example.franjo.smsapp.adapters.SentAdapter;
import com.example.franjo.smsapp.model.SMSData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SentMessagesActivity extends AppCompatActivity {

    private ListView listViewOutbox;
    private Cursor cursor;
    private SentAdapter arrayAdapter2;
    private final ArrayList<SMSData> smsList = new ArrayList<>();
    private final ArrayList<SMSData> selection_list = new ArrayList<>();
    private static final String FULL_SMS = "full_sms";
    private static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent);

        Toolbar toolbarOutbox = (Toolbar) findViewById(R.id.toolbarOutboox);
        setSupportActionBar(toolbarOutbox);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        initWidgets();
        readSmsSent();
        setSentAdapter();
        setUpListeners();


    }

    private void initWidgets() {
        listViewOutbox = (ListView) findViewById(R.id.listViewOutbox);
        listViewOutbox.setTextFilterEnabled(true); // use to enable search view popup text
        listViewOutbox.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
    }

    private void readSmsSent() {
        Uri uri = Uri.parse("content://sms/sent");
        //Get Content Resolver object, which will deal with Content Provider
        ContentResolver cr = getContentResolver();
        //List required columns
        String[] projection = new String[]{"address", "body", "date"};
        // Fetch SMS Message from Built-in Content Provider
        cursor = cr.query(uri, projection, null, null, "date DESC");
        // Read the sms data and store it in the list

        if (cursor != null && cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                SMSData sms = new SMSData();
                String number = cursor.getString(cursor.getColumnIndex("address"));
                String name = getContactName(getApplicationContext(), number);
                String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                sms.setBody(body);

                if( name == null )
                    sms.setNumber(number);
                else
                    sms.setNumber(name);

                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(Long.parseLong((date)));
                String format = "dd.MM.";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                String dateString = sdf.format(cal.getTime());
                sms.setDate(dateString);

                String minute = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong((minute)));
                String formatMinute = "hh:mm";
                SimpleDateFormat sdfM = new SimpleDateFormat(formatMinute, Locale.getDefault());
                String dateStringMinute = sdfM.format(cal.getTime());
                sms.setMinute(dateStringMinute);

                sms.setContactImage();
                smsList.add(sms);

                cursor.moveToNext();
            }
        }
    }

    private String getContactName(Context context, String phoneNumber) {

        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }

    private void setSentAdapter() {

        arrayAdapter2 = new SentAdapter(SentMessagesActivity.this, smsList);
        listViewOutbox.setAdapter(arrayAdapter2);


//        listViewOutbox.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//
//                AlertDialog.Builder alert = new AlertDialog.Builder(SentMessagesActivity.this);
//                alert.setMessage("Are you sure you want to delete this message?");
//                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        arrayAdapter2.remove(smsList.get(position));
//                        arrayAdapter2.notifyDataSetChanged();
//
//                        Toast.makeText(getApplicationContext(), "Removed from list", Toast.LENGTH_SHORT).show();
//
//
//                    }
//                });
//
//                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                alert.create();
//                alert.show();
//
//                return true;
//            }
//        });
    }

    private void setUpListeners() {

        FloatingActionButton fabOutbox = (FloatingActionButton) findViewById(R.id.fabOutbox);
        fabOutbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(SentMessagesActivity.this, NewMessageActivity.class);
                startActivity(i);
            }
        });
        listViewOutbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(SentMessagesActivity.this, DetailsMessagesSent.class);

                // move the cursor to required position
                if (cursor != null) {
                    cursor.moveToPosition(position);
                }

                String senderNumber = null;
                if (cursor != null) {
                    senderNumber = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                }
                intent.putExtra("broj1", senderNumber);

                String senderNumberName = ((TextView) view.findViewById(R.id.txtBroj)).getText().toString();
                intent.putExtra("name1", senderNumberName);

                String smsBody = ((TextView)view.findViewById(R.id.txtPoruka)).getText().toString();
                intent.putExtra("poruka1", smsBody);

                String dateTime = ((TextView)view.findViewById(R.id.txtVrijeme)).getText().toString().trim();
                intent.putExtra("vrijeme1", dateTime);

                String minute = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                intent.putExtra("minute1", minute);


                startActivity(intent);
            }

        });

        listViewOutbox.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int position, long l, boolean checked) {

                int counter = listViewOutbox.getCheckedItemCount();
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
                        AlertDialog.Builder alert = new AlertDialog.Builder(SentMessagesActivity.this);
                        alert.setIconAttribute(android.R.attr.alertDialogIcon);
                        alert.setTitle(R.string.permanently_message_delete);
                        alert.setMessage(R.string.delete_message);
                        alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (final SMSData sms : selection_list) {
                                    arrayAdapter2.remove(sms);
                                    arrayAdapter2.notifyDataSetChanged();

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
        MenuInflater inflater = getMenuInflater(); // Inflate the options menu from XML
        inflater.inflate(R.menu.menu_sent, menu);

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
                arrayAdapter2.getFilter().filter(newText);

//                if (TextUtils.isEmpty(newText)) {
//                    listViewOutbox.clearTextFilter();
//                }
//                else {
//                    listViewOutbox.setFilterText(newText);
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
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
            default:

        }

        return super.onOptionsItemSelected(item);
    }


    public static boolean showFullText() {
        return sharedPreferences.getBoolean(FULL_SMS, true);
    }


}



