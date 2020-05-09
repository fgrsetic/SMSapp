package com.franjo.smsapp.data.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.franjo.smsapp.data.model.Message;
import com.franjo.smsapp.util.ContactsName;
import com.franjo.smsapp.util.DateFormatting;

import java.util.ArrayList;
import java.util.List;


/* MyReceiver enables application to receive intents that are broadcast by the system
or by other applications, even when other components of the application are not running
*/
public class Receiver extends BroadcastReceiver implements IReceiver {

    // A PDU is a "protocol data unit". This is the industrial standard for SMS message
    private static final String SMS_BUNDLE = "pdus";
    // This notification ID can be used to access our notification after we've displayed it. This
    // can be handy when we need to cancel the notification, or perhaps update it. This number is
    // arbitrary and can be set to whatever we like
    private static final int SMS_NOTIFICATION_ID = 1138;
    // This pending intent id is used to uniquely reference the pending intent
    private static final int SMS_PENDING_INTENT = 3417;
    private static final String NOTIFICATION_CHANNEL_ID = "999";

    private List<Message> smsList;

    public Receiver() {
        smsList = new ArrayList<>();
    }

    // onReceive is called when this BroadcastReceiver receives an Intent broadcast
    @Override
    public void onReceive(Context context, Intent intent) {
        final Message message = new Message();
        // Get the SMS message received
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get(SMS_BUNDLE);
                if (pdusObj != null) {
                    for (Object pdus : pdusObj) {
                        // This will create an SmsMessage object from the received pdu
                        SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus);
                        // Get sender phone number
                        String address = sms.getOriginatingAddress();
                        String formattedDate = DateFormatting.formatDate("dd.MM.", System.currentTimeMillis());
                        String formattedMinute = DateFormatting.formatDate("hh:mm", System.currentTimeMillis());
                        String name = ContactsName.getContactName(context, address);
                        String smsBody = sms.getMessageBody();

                        if (name == null)
                            message.setPhoneNumber(address);
                        else
                           message.setPhoneNumber(name);

                        StringBuilder sb = new StringBuilder();
                        String smsMessageStr = sb.append(address).append("\n").toString();
//                        smsMessageStr += address + "\n";
//                        smsMessageStr += smsBody + "\n";

                        message.setMessageBody(smsBody);
                        message.setDate(formattedDate);
                        //smsData.setContactImage();
                        smsList.add(message);
                    }

               //     setNotification(context);
                }

            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }




    @Override
    public List<Message> getSMSList() {
        return smsList;
    }


}









