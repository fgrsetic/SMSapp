package com.franjo.smsapp.data.device_storage.mms;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.data.database.AppDatabase;
import com.franjo.smsapp.data.model.entity.MMSMessage;

import static android.provider.Telephony.TextBasedSmsColumns.MESSAGE_TYPE_INBOX;

public class MMSDeviceStorageSource implements IMMSDeviceStorageSource {

    private static MMSDeviceStorageSource INSTANCE = null;

    private ContentResolver contentResolver;
    private AppDatabase mDB;

    private MMSDeviceStorageSource() {
        contentResolver = App.getAppContext().getContentResolver();
        mDB = AppDatabase.getInstance(App.getAppContext());
    }

    public static MMSDeviceStorageSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MMSDeviceStorageSource();
        }
        return INSTANCE;
    }

    @Override
    public void getMMSMessages(int messageType) {
        Cursor cursor = contentResolver.query(Telephony.Mms.Sent.CONTENT_URI, null, null, null, null);
        if (messageType == MESSAGE_TYPE_INBOX) {
            cursor = contentResolver.query(Telephony.Mms.Inbox.CONTENT_URI, null, null, null, null);
        }

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(Telephony.Mms._ID));
                int mmsThreadId = cursor.getInt(cursor.getColumnIndex(Telephony.Mms.THREAD_ID));
                String mmsMessageId = cursor.getString(cursor.getColumnIndex(Telephony.Mms.MESSAGE_ID));
                String mmsType = getMmsType(id);
                String mmsPart = getPartOfMMS(mmsMessageId);
                String mmsMessageBody = getMmsText(id);
                String date = cursor.getString(cursor.getColumnIndex(Telephony.Mms.DATE));

                MMSMessage mmsMessage = new MMSMessage(mmsThreadId, mmsType, mmsPart, mmsMessageBody, date);
                mDB.mmsMessageDao().insertMMSMessage(mmsMessage);

            } while (cursor.moveToNext());

            cursor.close();
        }

    }

    // MMS content. It’s accessible through media files in the Uri format
    private String getPartOfMMS(String mmsID) {
        String selectionPart = "mid=" + mmsID;
        Uri uri = Uri.parse("content://mms/part");
        Cursor cursor = contentResolver.query(uri, null, selectionPart, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String path = cursor.getString(cursor.getColumnIndex(Telephony.Mms.Part._DATA));
                if (path != null) {
                    return path;
                }
            } while (cursor.moveToNext());

            cursor.close();
        }
        return null;
    }

    private String getMmsText(int id) {
        String selectionPart = "mid=" + id;
        Uri uri = Uri.parse("content://mms/part");
        Cursor cursor = contentResolver.query(uri, null, selectionPart, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndex(Telephony.Mms.Part.CONTENT_TYPE));
                if ("text/plain".equals(type)) {
                    String path = cursor.getString(cursor.getColumnIndex(Telephony.Mms.Part.TEXT));
                    if (path != null) {
                        return path;
                    }
                }
            } while (cursor.moveToNext());

            cursor.close();
        }
        return null;
    }

    private String getMmsType(int id) {
        String selectionPart = "mid=" + id;
        Uri uri = Uri.parse("content://mms/part");
        Cursor cursor = contentResolver.query(uri, null, selectionPart, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndex(Telephony.Mms.Part.CONTENT_TYPE));
                //  SMIL is a file format containing xml that helps an MMS viewer to know how to display the MMS
                if (!type.equals("application/smil")) {
                    return type;
                }
            } while (cursor.moveToNext());

            cursor.close();
        }
        return null;
    }

}
