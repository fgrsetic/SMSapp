package com.franjo.smsapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.util.ContactName;
import com.franjo.smsapp.util.DateFormatting;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MessagesDatabase implements IMessages {


    @Override
    public List<SmsData> getAllMessages(Context context) {
        // Get Content Resolver object, which will deal with Content Provider
        ContentResolver contentResolver = context.getContentResolver();
        List<SmsData> smsList = new ArrayList<>();
        // Uri to get messages from the inbox and we’re grabbing the body, address and date
        Uri uri = Uri.parse("content://sms/inbox");
        // List required columns
        String[] projection = new String[]{"address", "body", "date"};
        // Fetch SMS Message from Built-in Content Provider
        Cursor smsInboxCursor = contentResolver.query(uri, projection, null, null, "date DESC");

        // Read the sms data and store it in the list
        if (smsInboxCursor != null && smsInboxCursor.moveToFirst()) {
            // Cursor to go through each message
            for (int i = 0; i < smsInboxCursor.getCount(); i++) {
                SmsData sms = new SmsData();
                String number = smsInboxCursor.getString(smsInboxCursor.getColumnIndexOrThrow("address"));
                String name = ContactName.getContactName(context, number);
                String body = smsInboxCursor.getString(smsInboxCursor.getColumnIndexOrThrow("body"));
                sms.setMessageBody(body);
                if (name == null)
                    sms.setPhoneNumber(number);
                else
                    sms.setPhoneNumber(name);

                String date = smsInboxCursor.getString(smsInboxCursor.getColumnIndexOrThrow("date"));
                String dateString = DateFormatting.formatDate("dd.MM", Long.parseLong((date)));
                sms.setDate(dateString);

                smsList.add(sms);

                smsInboxCursor.moveToNext();
            }
            smsInboxCursor.close();
        }
        return smsList;
    }

    @Override
    public List<Contact> openContactList(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        List<Contact> contactList = new ArrayList<>();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToNext()) {
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            // Bitmap photo = loadContactPhoto(phoneNumber, context);
            Contact contact = new Contact();
            contact.setName(name);
            contact.setPhoneNumber(phoneNumber);
            // contact.setContactPicture(photo);
            contactList.add(contact);
        }
        if (cursor != null) {
            cursor.close();
        }
        return contactList;
    }

    @Override
    public Uri openContactDetails(SmsData data) {
        ContentResolver contentResolver = App.getAppContext().getContentResolver();
        Uri contactUri = null;
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, Uri.encode(data.getPhoneNumber()));
        String[] phoneNumberProjection = {
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts.LOOKUP_KEY
        };

        Cursor cursor = contentResolver.query(lookupUri, phoneNumberProjection, null, null, null);

        if (cursor != null && cursor.moveToNext()) {
            int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
            if (hasPhoneNumber > 0) {
                String lookupKey = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.LOOKUP_KEY));
                long contactId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                contactUri = ContactsContract.Contacts.getLookupUri(contactId, lookupKey);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return contactUri;

    }


    public Bitmap loadContactPhoto(String phoneNumber, Context context) {
        long contactID = getContactIDFromNumber(phoneNumber, context);
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactID);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = context.getContentResolver().query(
                photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO},
                null,
                null,
                null
        );
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
                } else {
                    Toast.makeText(context, "No image", Toast.LENGTH_SHORT).show();
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    // Get contact id by using the phone number
    private static long getContactIDFromNumber(String contactNumber, Context context) {
        String UriContactNumber = Uri.encode(contactNumber);
        long phoneContactID = new Random().nextInt();
        Cursor contactLookupCursor = context.getContentResolver().query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, UriContactNumber),
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID}, null, null, null);
        if (contactLookupCursor != null) {
            while (contactLookupCursor.moveToNext()) {
                phoneContactID = contactLookupCursor.getLong(contactLookupCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
        }
        if (contactLookupCursor != null) {
            contactLookupCursor.close();
        }
        return phoneContactID;
    }


    // Search
    @Override
    public  List<SmsData> performSearch(Context context, String query) {
        return querySmsMessages(context, query);
    }

    private Cursor getListOfContactNames(Context context, String searchText) {
        Cursor cursor;
        ContentResolver cr = context.getContentResolver();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] mProjection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
        String selection = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + searchText + "%"};
        cursor = cr.query(uri, mProjection, selection, selectionArgs, null);
//        if (cursor != null) {
//            cursor.close();
//        }
        return cursor;
    }

    private List<SmsData> querySmsMessages(Context context, String searchText) {
        List<SmsData> searchedList = new ArrayList<>();
        Cursor cursor;
        ContentResolver contentResolver = context.getContentResolver();
        // Uri to get messages from the inbox and we’re grabbing the body, address and date
        Uri uri = Uri.parse("content://sms/");
        // List required columns
        String[] projection = new String[]{"_id, address", "body", "date"};
        String selection = "body LIKE?";
        String[] selectionArgs = new String[]{"%" + searchText + "%"};
        // Fetch SMS Message from Built-in Content Provider
        cursor = contentResolver.query(uri, projection, selection, selectionArgs, "date DESC");
        // Read the sms data and store it in the list
        if (cursor != null && cursor.moveToFirst()) {
            // Cursor to go through each message
            for (int i = 0; i < cursor.getCount(); i++) {
                SmsData sms = new SmsData();
                String number = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String name = ContactName.getContactName(context, number);
                String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                sms.setMessageBody(body);
                if (name == null)
                    sms.setPhoneNumber(number);
                else
                    sms.setPhoneNumber(name);

                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String dateString = DateFormatting.formatDate("dd.MM", Long.parseLong((date)));
                sms.setDate(dateString);

                searchedList.add(sms);

                cursor.moveToNext();
            }
            cursor.close();
        }
        return searchedList;
    }


}
