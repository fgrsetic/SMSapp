package com.franjo.smsapp.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.franjo.smsapp.data.database.DatabaseMessagesDataSource;

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
            contactName = cursor.getString(DatabaseMessagesDataSource.ColumnIndexCache.getColumnIndexOrThrow(cursor, ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        DatabaseMessagesDataSource.ColumnIndexCache.clear();
        return contactName;
    }
}
