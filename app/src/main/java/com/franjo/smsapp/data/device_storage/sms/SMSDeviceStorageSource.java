package com.franjo.smsapp.data.device_storage.sms;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.Telephony;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.data.database.AppDatabase;
import com.franjo.smsapp.data.model.entity.DatabaseMessage;

public class SMSDeviceStorageSource implements ISMSDeviceStorageSource {

    private static SMSDeviceStorageSource INSTANCE = null;

    private ContentResolver contentResolver;
    private AppDatabase mDB;

    private SMSDeviceStorageSource() {
        contentResolver = App.getAppContext().getContentResolver();
        mDB = AppDatabase.getInstance(App.getAppContext());
    }

    public static SMSDeviceStorageSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SMSDeviceStorageSource();
        }
        return INSTANCE;
    }

    @Override
    public void getSMSMessages() {
        Cursor cursor = contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int threadId = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.THREAD_ID));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                String messageBody = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
                int messageType = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.TYPE));
                String date = cursor.getString(cursor.getColumnIndex(Telephony.Sms.DATE));

                DatabaseMessage databaseMessage = new DatabaseMessage();
                databaseMessage.setThreadId(threadId);
                databaseMessage.setAddress(address);
                databaseMessage.setMessageBody(messageBody);
                databaseMessage.setMessageType(messageType);
                databaseMessage.setDate(date);

                mDB.messageDao().insertMessage(databaseMessage);

            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
    }
}
