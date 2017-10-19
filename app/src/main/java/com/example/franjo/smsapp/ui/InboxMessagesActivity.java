package com.example.franjo.smsapp.ui;


import android.Manifest;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Locale;

public class InboxMessagesActivity extends AppCompatActivity {

    private static final String LOG_TAG = InboxMessagesActivity.class.getSimpleName();

    private ListView listViewInbox;
    private List<SMSData> smsList;
    private List<SMSData> selection_list;
    private InboxAdapter arrayAdapter;
    private FloatingActionButton fabInbox;

    private ImageView photoImage;

    Cursor cursor;

    private static final String FULL_SMS = "full_sms";
    private static SharedPreferences sharedPreferences;
    // This will help us to communicate between classes
    private static InboxMessagesActivity inst;
    private static final int READ_SMS_PERMISSIONS_REQUEST = 1;
    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;

    public static InboxMessagesActivity instance() {
        return inst;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOG_TAG, "onCreate");

        Toolbar toolbarInbox = (Toolbar) findViewById(R.id.toolbarInbox);
        setSupportActionBar(toolbarInbox);

        // Initialize the lists
        smsList = new ArrayList<>();
        selection_list = new ArrayList<>();

        this.startService(new Intent(this, HeadlessSmsSendService.class));


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
        setupSharedPreferences();

    }

    private void setupSharedPreferences() {
        // A reference to the default shared preferences from the PreferenceManager class
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
        setInboxAdapter();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToReadContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, R.string.allow_permission, Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_PERMISSIONS_REQUEST);

        }
    }

    // We’re checking to see whether the permission is granted already
    // if it’s not, we’re checking whether we need to explain the situation to the user.
    // If so, then we’re displaying a toast message and either way, we’re then actually doing the asking.
    public void getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
                    Toast.makeText(this, R.string.allow_permission, Toast.LENGTH_SHORT).show();
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_SMS}, READ_SMS_PERMISSIONS_REQUEST);
            }
        }
    }

    // We handle the response via onRequestPermissionResult.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Our toast message confirms the answer and if it is positive, we’re then using our next new method,
                Toast.makeText(this, R.string.sms_permission_granted, Toast.LENGTH_SHORT).show();
                refreshSmsInbox();
            } else {
                Toast.makeText(this, R.string.sms_permission_denied, Toast.LENGTH_SHORT).show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void initWidgets() {
        listViewInbox = (ListView) findViewById(R.id.listViewInbox);
//        listViewInbox.setTextFilterEnabled(true); // use to enable search view popup text
        fabInbox = (FloatingActionButton) findViewById(R.id.fabInbox);
        listViewInbox.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);


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

                if (name == null)
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

                sms.setContactImage(sms.getContactImage());
                smsList.add(sms);

                smsInboxCursor.moveToNext();
            }
            smsInboxCursor.close();
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
        arrayAdapter = new InboxAdapter(InboxMessagesActivity.this, smsList);
        // Setting the adapter on our listview
        listViewInbox.setAdapter(arrayAdapter);
    }

    // Method to refresh the messages
    public void updateInbox(SMSData newSms) {
        arrayAdapter.insert(newSms, 0);
        arrayAdapter.notifyDataSetChanged();

    }

    private void setUpListeners() {
        fabInbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent(InboxMessagesActivity.this, NewMessageActivity.class);
                startActivity(i);
            }
        });

        listViewInbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(InboxMessagesActivity.this, DetailsMessagesInbox.class);

                // Move the cursor to required position
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
                actionMode.setTitle(counter + "");
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
                        AlertDialog.Builder alert = new AlertDialog.Builder(InboxMessagesActivity.this);
                        alert.setIconAttribute(android.R.attr.alertDialogIcon);
                        alert.setTitle(R.string.permanently_message_delete);
                        alert.setMessage(R.string.delete_message);
                        alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (final SMSData sms : selection_list) {
                                    arrayAdapter.remove(sms);
                                    // Notifies the attached observers that the underlying data has been changed
                                    // and any View reflecting the data set should refresh itself.
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
        inflater.inflate(R.menu.settings_inbox, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
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
        int id = item.getItemId();
        switch (id) {
            case R.id.settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                break;
            case R.id.sent_messages:
                Intent startSentActivity = new Intent(this, SentMessagesActivity.class);
                startActivity(startSentActivity);
                break;
            default:
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public static boolean showFullText() {
       // Getting the value of the full sms checkbox preference
       return sharedPreferences.getBoolean(FULL_SMS, true);
    }

//
//    public void pickPhoto(View view) {
//        Intent intent = new Intent();
//        // Show only images, no videos or anything else
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        // Always show the chooser (if there are multiple options available)
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
//
//    }


//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Bitmap bitmap = null;
//        String imagePath = null;
//
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//            Uri uri = data.getData();
//            String[] filePath = { MediaStore.Images.Media.DATA };
//            Cursor cursor = getContentResolver().query(uri, filePath, null, null, null);
//
//            if (cursor != null) {
//                cursor.moveToFirst();
//            }
//            if (cursor != null) {
//                imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
//            }
//
//            if (cursor != null) {
//                cursor.close();
//            }
//
//            photoImage = (ImageView) findViewById(R.id.contactPhotoPick);
//            // Resample the saved image to fit the ImageView
//            bitmap = resamplePic(this, imagePath);
//            photoImage.setImageBitmap(bitmap);
//
//
//        }
//    }

//    static Bitmap resamplePic(Context context, String imagePath) {
//
//        // Get device screen size information
//        DisplayMetrics metrics = new DisplayMetrics();
//        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        manager.getDefaultDisplay().getMetrics(metrics);
//
//        int targetH = metrics.heightPixels;
//        int targetW = metrics.widthPixels;
//
//        // Get the dimensions of the original bitmap
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(imagePath, bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
//        // Determine how much to scale down the image
//        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
//
//        // Decode the image file into a Bitmap sized to fill the View
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//
//        return BitmapFactory.decodeFile(imagePath);
//    }



}





