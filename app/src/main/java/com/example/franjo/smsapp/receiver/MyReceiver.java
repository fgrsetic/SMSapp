package com.example.franjo.smsapp.receiver;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.franjo.smsapp.R;
import com.example.franjo.smsapp.model.SMSData;
import com.example.franjo.smsapp.ui.InboxActivity;
import com.example.franjo.smsapp.ui.SMSDetailsInbox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MyReceiver extends BroadcastReceiver {

    // A PDU is a "protocol data unit". This is the industrial standard for SMS message
    private static final String SMS_BUNDLE = "pdus";
    protected final ArrayList<SMSData> smsList = new ArrayList<>();
    SMSData smsData = new SMSData();

    public MyReceiver() {
    }
    // This method is called when this BroadcastReceiver receives an Intent broadcast
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message received
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                // A PDU is a "protocol data unit". This is the industrial standard for SMS message
                final Object[] pdusObj = (Object[]) bundle.get(SMS_BUNDLE);

                String smsMessageStr = "";

                if (pdusObj != null) {
                    for (Object aPdusObj : pdusObj) {
                        // This will create an SmsMessage object from the received pdu
                        SmsMessage sms = SmsMessage.createFromPdu((byte[]) aPdusObj);

                        // Get sender phone number
                        String address = sms.getOriginatingAddress();

                        Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(System.currentTimeMillis());
                        String format = "dd.MM.";
                        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
                        String dateString = sdf.format(cal.getTime());

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        String formatMinute = "hh:mm";
                        SimpleDateFormat sdfM = new SimpleDateFormat(formatMinute, Locale.getDefault());
                        String dateStringMinute = sdfM.format(cal.getTime());

                        String name = getContactName(context, address);
                        // Get sender message
                        String smsBody = sms.getMessageBody();

                        if( name == null )
                            smsData.setNumber(address);
                        else
                            smsData.setNumber(name);

                        smsMessageStr += address + "\n";
//                        smsMessageStr += smsBody + "\n";

                        smsData.setBody(smsBody);
                        smsData.setDate(dateString);
                        smsData.setMinute(dateStringMinute);
                        smsData.setContactImage();
                        smsList.add(smsData);

                        if (InboxActivity.active) {
                            InboxActivity inst = InboxActivity.instance();
                            inst.updateInbox(smsData);
                        } else {
                            Intent i = new Intent(context, InboxActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);

                        }

                    }
//                    //show alert
//                    Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(context, SMSDetailsInbox.class);
                    i.putExtra("broj", smsMessageStr);
                    i.putExtra("name", smsData.getNumber());
                    i.putExtra("poruka", smsData.getBody());
                    i.putExtra("vrijeme", smsData.getDate());
                    i.putExtra("minute", smsData.getMinute());

                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                    builder.setAutoCancel(true)
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.drawable.ic_mail_outline)
                            .setContentTitle(smsData.getNumber())
                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                            .setContentIntent(pendingIntent)
                            .setContentText(smsData.getBody());

                    Notification notification = builder.build();
                    NotificationManagerCompat.from(context).notify(0, notification);

                }


            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }





    private String getContactName(Context context, String phoneNumber) {

        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);

        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }

}









