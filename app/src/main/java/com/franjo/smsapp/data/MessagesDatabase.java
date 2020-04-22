package com.franjo.smsapp.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.franjo.smsapp.util.ContactName;
import com.franjo.smsapp.util.DateFormatting;

import java.util.ArrayList;
import java.util.List;

public class MessagesDatabase implements IMessages {


    public MessagesDatabase() {

    }

    @Override
    public List<SmsData> getAllMessages(Context context) {
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

                sms.setContactImage(sms.getContactImage());
                smsList.add(sms);

                smsInboxCursor.moveToNext();
            }
            smsInboxCursor.close();
        }
        return smsList;
    }
}
