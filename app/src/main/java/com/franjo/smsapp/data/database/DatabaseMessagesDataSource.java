package com.franjo.smsapp.data.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Telephony;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.data.model.Message;
import com.franjo.smsapp.util.ContactsName;
import com.franjo.smsapp.util.DateFormatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseMessagesDataSource implements IMessagesDataSource {

    private static final Uri SMS_URI = Uri.parse("content://sms/inbox");

    public static final String ID_COLUMN = Telephony.Sms._ID;
    private static final String ADDRESS_COLUMN = Telephony.Sms.ADDRESS;
    private static final String BODY_COLUMN = Telephony.Sms.BODY;
    private static final String DATE_COLUMN = Telephony.Sms.DATE;

    private static final String[] PROJECTION_SMS = new String[]{
            Telephony.Sms._ID,
            Telephony.Sms.ADDRESS,
            Telephony.Sms.BODY,
            Telephony.Sms.DATE
    };

    private static final String SELECTION_SEARCH_SMS = Telephony.Sms.BODY + " LIKE ?";
    private static final String SORT_ORDER_SMS = "date DESC";

    private Context context;
    private ContentResolver contentResolver;

    public DatabaseMessagesDataSource() {
        context = App.getAppContext();
        contentResolver = context.getContentResolver();
    }

    // Messages
    @Override
    public List<Message> getReceivedMessages() {
        // Get Content Resolver object, which will deal with Content Provider
        List<Message> smsList = new ArrayList<>();

        // List required columns
        // Fetch SMS Message from Built-in Content Provider
        Cursor cursor = contentResolver.query(SMS_URI, PROJECTION_SMS, null, null, SORT_ORDER_SMS);

        // Read the sms data and store it in the list
        if (cursor != null && cursor.moveToFirst()) {
            // Cursor to go through each message
            for (int i = 0; i < cursor.getCount(); i++) {
                Message message = new Message();
                String number = cursor.getString(ColumnIndexCache.getColumnIndexOrThrow(cursor, ADDRESS_COLUMN));
                String name = ContactsName.getContactName(context, number);
                String body = cursor.getString(ColumnIndexCache.getColumnIndexOrThrow(cursor, BODY_COLUMN));
                message.setMessageBody(body);
                if (name == null)
                    message.setPhoneNumber(number);
                else
                    message.setPhoneNumber(name);

                String date = cursor.getString(ColumnIndexCache.getColumnIndexOrThrow(cursor, DATE_COLUMN));
                String dateString = DateFormatting.formatDate("dd.MM", Long.parseLong((date)));
                message.setDate(dateString);
                smsList.add(message);
                cursor.moveToNext();

//                String type;
//                switch (Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.TYPE)))) {
//                    case Telephony.Sms.MESSAGE_TYPE_INBOX:
//                        type = "inbox";
//                        break;
//                    case Telephony.Sms.MESSAGE_TYPE_SENT:
//                        type = "sent";
//                        break;
//                    case Telephony.Sms.MESSAGE_TYPE_OUTBOX:
//                        type = "outbox";
//                        break;
//                    default:
//                        break;
//                }
            }
            cursor.close();
            ColumnIndexCache.clear();
        }
        return smsList;
    }



    // Search
    @Override
    public Cursor performSearch(String searchText) {
        return getCursor(searchText);
    }

    private Cursor getCursor(String searchText) {
        String[] selectionArgs = new String[]{"%" + searchText + "%"};
        Cursor cursor = contentResolver.query(SMS_URI, PROJECTION_SMS, SELECTION_SEARCH_SMS, selectionArgs, SORT_ORDER_SMS);
        if (searchText.isEmpty()) {
            return null;
        } else {
            return cursor;
        }
    }



    // Contact details
    @Override
    public Uri openContactDetails(Message data) {
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




    public static class ColumnIndexCache {
        private static Map<String, Integer> map = new HashMap<>();

        public static Integer getColumnIndexOrThrow(Cursor cursor, String columnName) {
            Integer value = cursor.getColumnIndex(columnName);
            if (!map.containsKey(columnName)) {
                map.put(columnName, value);
            }
            return map.get(columnName);
        }

        public static void clear() {
            map.clear();
        }
    }


}
