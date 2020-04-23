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

import com.franjo.smsapp.util.ContactName;
import com.franjo.smsapp.util.DateFormatting;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MessagesDatabase implements IMessages {

    private Context context;


    @Override
    public List<SmsData> getAllMessages(Context context) {
        this.context = context;
        List<SmsData> smsList = new ArrayList<>();
        // Uri to get messages from the inbox and we’re grabbing the body, address and date
        Uri uri = Uri.parse("content://sms/inbox");
        // Get Content Resolver object, which will deal with Content Provider
        ContentResolver cr = context.getContentResolver();
        // List required columns
        String[] projection = new String[]{"address", "body", "date"};
        // Fetch SMS Message from Built-in Content Provider
        Cursor smsInboxCursor = cr.query(uri, projection, null, null, "date DESC");

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
    public Bitmap loadContactPhoto(String phoneNumber) {
        long contactID = getContactIDFromNumber(phoneNumber, context);
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactID);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = context.getContentResolver().query(
                photoUri,
                new String[] {ContactsContract.Contacts.Photo.PHOTO},
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
