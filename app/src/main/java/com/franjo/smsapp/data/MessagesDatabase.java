package com.franjo.smsapp.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.franjo.smsapp.util.ContactName;
import com.franjo.smsapp.util.DateFormatting;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.provider.ContactsContract.Contacts.CONTENT_LOOKUP_URI;

public class MessagesDatabase implements IMessages {

    private Context context;
    private ContentResolver contentResolver;

    @Override
    public List<SmsData> getAllMessages(Context context) {
        this.context = context;
        // Get Content Resolver object, which will deal with Content Provider
        contentResolver = context.getContentResolver();
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

                String minute = smsInboxCursor.getString(smsInboxCursor.getColumnIndexOrThrow("date"));
                String minuteString = DateFormatting.formatDate("hh:mm", Long.parseLong((minute)));
                sms.setMinute(minuteString);

                smsList.add(sms);

                smsInboxCursor.moveToNext();
            }
            smsInboxCursor.close();
        }
        return smsList;
    }

    @Override
    public Uri openContactDetails(SmsData data) {
        Cursor cursor;
        Uri contactUri = null;
        Uri lookupUri = Uri.withAppendedPath(ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI, Uri.encode(data.getPhoneNumber()));
        String[] phoneNumberProjection = {
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
                ContactsContract.Contacts.LOOKUP_KEY
        };

        cursor = contentResolver.query(lookupUri, phoneNumberProjection, null, null, null);

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


    public Bitmap loadContactPhoto(String phoneNumber) {
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

}
