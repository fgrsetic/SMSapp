package com.franjo.smsapp.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.franjo.smsapp.R;
import com.franjo.smsapp.model.SMSData;
import com.franjo.smsapp.ui.DetailsMessagesInbox;
import com.franjo.smsapp.ui.InboxMessagesActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


// Broadcast receivers enable applications to receive intents that are broadcast by the system or by other applications,
// even when other components of the application are not running.
public class MyReceiver extends BroadcastReceiver {

    // A PDU is a "protocol data unit". This is the industrial standard for SMS message
    private static final String SMS_BUNDLE = "pdus";
    protected final List<SMSData> smsList = new ArrayList<>();
    SMSData smsData = new SMSData();
    String smsMessageStr = "";

    /*
     * This notification ID can be used to access our notification after we've displayed it. This
     * can be handy when we need to cancel the notification, or perhaps update it. This number is
     * arbitrary and can be set to whatever you like. 1138 is in no way significant.
     */
    private static final int SMS_NOTIFICATION_ID = 1138;
    /**
     * This pending intent id is used to uniquely reference the pending intent
     */
    private static final int SMS_PENDING_INTENT = 3417;


    public MyReceiver() {

    }

    // This method is called when this BroadcastReceiver receives an Intent broadcast
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS message received
        final Bundle bundle = intent.getExtras();

        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get(SMS_BUNDLE);

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

                        if (name == null)
                            smsData.setNumber(address);
                        else
                            smsData.setNumber(name);

                        smsMessageStr += address + "\n";
//                        smsMessageStr += smsBody + "\n";

                        smsData.setBody(smsBody);
                        smsData.setDate(dateString);
                        smsData.setMinute(dateStringMinute);
                        //smsData.setContactImage();
                        smsList.add(smsData);


                    }
                    InboxMessagesActivity inst = InboxMessagesActivity.instance();
                    inst.updateInbox(smsData);

                    incomingSms(context);

                }

            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }

    public void incomingSms(Context context) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(smsData.getNumber())
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setContentText(smsData.getBody());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }


        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        /* SMS_REMINDER_NOTIFICATION_ID allows you to update or cancel the notification later on */
        notificationManager.notify(SMS_NOTIFICATION_ID, builder.build());
    }


    private PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, DetailsMessagesInbox.class);

        startActivityIntent.putExtra("broj", smsMessageStr);
        startActivityIntent.putExtra("name", smsData.getNumber());
        startActivityIntent.putExtra("poruka", smsData.getBody());
        startActivityIntent.putExtra("vrijeme", smsData.getDate());
        startActivityIntent.putExtra("minute", smsData.getMinute());

        return PendingIntent.getActivity(context, SMS_PENDING_INTENT, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);


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









