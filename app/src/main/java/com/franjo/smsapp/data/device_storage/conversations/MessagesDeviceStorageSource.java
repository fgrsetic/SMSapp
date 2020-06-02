package com.franjo.smsapp.data.device_storage.conversations;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.data.database.AppDatabase;
import com.franjo.smsapp.data.model.entity.DatabaseConversation;
import com.franjo.smsapp.data.model.entity.DatabaseMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

import static android.provider.BaseColumns._ID;
import static android.provider.Telephony.BaseMmsColumns.CONTENT_TYPE;
import static android.provider.Telephony.ThreadsColumns.DATE;


public class MessagesDeviceStorageSource implements IMessagesDeviceStorageSource {

    private static MessagesDeviceStorageSource INSTANCE = null;

    private static ContentResolver contentResolver;
    private AppDatabase mDB;

    private MessagesDeviceStorageSource() {
        contentResolver = App.getAppContext().getContentResolver();
        mDB = AppDatabase.getInstance(App.getAppContext());

    }

    public static MessagesDeviceStorageSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MessagesDeviceStorageSource();
        }
        return INSTANCE;
    }

    @Override
    public void loadAndSaveStorageMessages() {
        loadAndSaveConversations();
        loadAndSaveSMSMessages();
        loadAndSaveMMSMessages();
    }

    // Conversations
    private void loadAndSaveConversations() {
        Uri uri = Uri.parse("content://mms-sms/conversations?simple=true");
        String[] ALL_THREADS_PROJECTION = {
                Telephony.Threads._ID,
                Telephony.Threads.DATE,
                Telephony.Threads.MESSAGE_COUNT,
                Telephony.Threads.RECIPIENT_IDS,
                Telephony.Threads.SNIPPET,
                Telephony.Threads.SNIPPET_CHARSET,
                Telephony.Threads.READ,
                Telephony.Threads.ERROR,
                Telephony.Threads.HAS_ATTACHMENT
        };

        Cursor cursor = contentResolver.query(uri, ALL_THREADS_PROJECTION, null, null, DATE);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int threadId = cursor.getInt(cursor.getColumnIndex(Telephony.Threads._ID));
                long timestamp = cursor.getLong(cursor.getColumnIndex(DATE));
                String recipientId = cursor.getString(cursor.getColumnIndex(Telephony.Threads.RECIPIENT_IDS));
                String snippet = cursor.getString(cursor.getColumnIndex(Telephony.Threads.SNIPPET));

                DatabaseConversation databaseConversation = new DatabaseConversation();
                databaseConversation.setThreadId(threadId);
                databaseConversation.setRecipient(getContactByRecipientId(Long.parseLong(recipientId)));
                databaseConversation.setSnippet(snippet); // last message
                databaseConversation.setDateMsgCreated(timestamp);

                mDB.conversationsDao().insertConversation(databaseConversation);

            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private String getContactByRecipientId(long recipientId) {
        String contact = "";
        Uri uri = ContentUris.withAppendedId(Uri.parse("content://mms-sms/canonical-address"), recipientId);
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            contact = getContactByPhoneNumber(cursor.getString(0));
        }
        if (cursor != null) {
            cursor.close();
        }
        return contact;
    }

    private String getContactByPhoneNumber(String phoneNumber) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.NORMALIZED_NUMBER};
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);

        String name = null;
        String nPhoneNumber = phoneNumber;

        if (cursor != null && cursor.moveToFirst()) {
            nPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.NORMALIZED_NUMBER));
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (cursor != null) {
            cursor.close();
        }

        if (name != null) {
            return name;
        } else {
            return nPhoneNumber;
        }
    }


    // SMS Messages
    private void loadAndSaveSMSMessages() {
//        String selection = "thread_id = ?";
//        String[] selectionArgs = {conversationThreadId};
        Cursor cursor = contentResolver.query(Telephony.Sms.CONTENT_URI, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int threadId = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.BaseMmsColumns.THREAD_ID));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                String messageBody = cursor.getString(cursor.getColumnIndex(Telephony.Sms.BODY));
                int messageType = cursor.getInt(cursor.getColumnIndex(Telephony.Sms.TYPE));
                long dateMsgReceived = cursor.getLong(cursor.getColumnIndex(Telephony.Sms.DATE));
                long dateMsgSent = cursor.getLong(cursor.getColumnIndex(Telephony.Sms.DATE_SENT));

                DatabaseMessage databaseSMSMessage = new DatabaseMessage();
                databaseSMSMessage.setThreadId(threadId);
                databaseSMSMessage.setAddress(address);
                databaseSMSMessage.setMessageBody(messageBody);
                databaseSMSMessage.setMessageType(messageType);
                databaseSMSMessage.setDateMsgReceived(dateMsgReceived);
                databaseSMSMessage.setDateMsgSent(dateMsgSent);
                mDB.messageDao().insertMessage(databaseSMSMessage);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }


    // MMS Messages
    private void loadAndSaveMMSMessages() {
        Cursor cursor = contentResolver.query(Telephony.Mms.CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(_ID));
                String contentType = cursor.getString(cursor.getColumnIndexOrThrow(CONTENT_TYPE));
                int threadId = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Mms.THREAD_ID));
                int messageType = cursor.getInt(cursor.getColumnIndexOrThrow(Telephony.Mms.MESSAGE_TYPE));
                String messageNumber = getAddressNumber(id);
                String mmsType = getMmsType(id);
                String mmsMessageBody = getMmsMessageBodyText(id);
                long dateMsgReceived = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Mms.DATE));
                long dateMsgSent = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Mms.DATE_SENT));

                DatabaseMessage databaseMMSMessage = new DatabaseMessage();
                databaseMMSMessage.setContentType(contentType);
                databaseMMSMessage.setThreadId(threadId);
                databaseMMSMessage.setAddress(messageNumber);
                databaseMMSMessage.setMessageBody(mmsMessageBody);
                databaseMMSMessage.setMessageType(messageType);
                databaseMMSMessage.setMmsType(mmsType);
                databaseMMSMessage.setDateMsgReceived(dateMsgReceived);
                databaseMMSMessage.setDateMsgSent(dateMsgSent);
                databaseMMSMessage.setBodyMessageAttachment(getMMSBodyImageType(id));
                mDB.messageDao().insertMessage(databaseMMSMessage);

            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private String getAddressNumber(String id) {
        String selectionAdd = "msg_id=" + id;
        String uriStr = MessageFormat.format("content://mms/{0}/addr", id);
        Uri uriAddress = Uri.parse(uriStr);
        String[] columns = {"address"};
        Cursor cursor = contentResolver.query(uriAddress, columns, selectionAdd, null, null);
        String name = "";
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String number = cursor.getString(cursor.getColumnIndex("address"));
                if (number != null) {
                    name = number;
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return name;
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

    private String getMmsMessageBodyText(String id) {
        String selectionPart = "mid=" + id;
        Uri uri = Uri.parse("content://mms/part");
        Cursor cursor = contentResolver.query(uri, null, selectionPart, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndex(Telephony.Mms.Part.CONTENT_TYPE));

                if ("text/plain".equals(type)) {
                    String data = cursor.getString(cursor.getColumnIndex(Telephony.Mms.Part._DATA));
                    String body;
                    if (data != null) {
                        body = getMmsText(id);
                    } else {
                        body = cursor.getString(cursor.getColumnIndex(Telephony.Mms.Part.TEXT));
                    }
                    return body;
                }
            } while (cursor.moveToNext());

            cursor.close();
        }
        return null;
    }

    private String getMmsText(String id) {
        Uri partURI = Uri.parse("content://mms/part/" + id);
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = contentResolver.openInputStream(partURI);
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(isr);
                String temp = reader.readLine();
                while (temp != null) {
                    sb.append(temp);
                    temp = reader.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    // Returns type of mms message text/image
    private String getMmsType(String id) {
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

    private String getMMSBodyImageType(String id) {
        String selectionPart = "mid=" + id;
        Uri uri = Uri.parse("content://mms/part");
        Cursor cursor = contentResolver.query(uri, null, selectionPart, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String type = cursor.getString(cursor.getColumnIndex(Telephony.Mms.Part.CONTENT_TYPE));
                if ("image/jpeg".equals(type) || "image/bmp".equals(type) ||
                        "image/gif".equals(type) || "image/jpg".equals(type) ||
                        "image/png".equals(type)) {
                    return type;
                }
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public static Bitmap getMmsImage(String mmsId) {
        Uri partURI = Uri.parse("content://mms/part/" + mmsId);
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = contentResolver.openInputStream(partURI);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException ignored) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
        return bitmap;
    }


}
