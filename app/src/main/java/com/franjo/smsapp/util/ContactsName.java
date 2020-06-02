package com.franjo.smsapp.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

public class ContactsName {

    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToNext()) {
            contactName = cursor.getString(ColumnIndexCache.getColumnIndexOrThrow(cursor, ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        ColumnIndexCache.clear();
        return contactName;
    }
//
//    private String getContactByPhoneNumber(String phoneNumber) {
//        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
//        String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.NORMALIZED_NUMBER};
//        Cursor cursor = contentResolver.query(uri, projection, null, null, null);
//
//        String name = null;
//        String nPhoneNumber = phoneNumber;
//
//        if (cursor != null && cursor.moveToFirst()) {
//            nPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.NORMALIZED_NUMBER));
//            name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
//        }
//        if (cursor != null) {
//            cursor.close();
//        }
//
//        if (name != null) {
//            return name;
//        } else {
//            return nPhoneNumber;
//        }
//    }

}
