package com.franjo.smsapp.ui.messages;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.franjo.smsapp.data.IMessages;
import com.franjo.smsapp.data.ISmsReceiver;
import com.franjo.smsapp.data.MessagesDatabase;
import com.franjo.smsapp.data.SmsData;
import com.franjo.smsapp.data.SmsReceiver;

import java.util.List;

public class MessagesViewModel extends AndroidViewModel {

    private Context context;
    private IMessages databaseMessages;
    private ISmsReceiver receiver;
    private MutableLiveData<List<SmsData>> notifiedSmsList;
    private MutableLiveData<List<SmsData>> databaseMessageList;


    public MessagesViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
        databaseMessages = new MessagesDatabase();
        receiver = new SmsReceiver();
    }

    public LiveData<List<SmsData>> showDatabaseSmsList() {
        if (databaseMessageList == null) {
            databaseMessageList = new MutableLiveData<>();
            loadDatabaseMessages();
        }
        return databaseMessageList;
    }

    private void loadDatabaseMessages() {
        databaseMessageList.setValue(databaseMessages.getAllMessages(context));
    }

    public LiveData<List<SmsData>> showSmsListReceived() {
        if (notifiedSmsList == null) {
            notifiedSmsList = new MutableLiveData<>();
            loadNotifiedSmsList();
        }
        return notifiedSmsList;
    }

    private void loadNotifiedSmsList() {
        notifiedSmsList.setValue(receiver.getSMSList());
    }

    public void showSmsDetails(SmsData smsData) {
    }


    //    private PendingIntent contentIntent(Context context) {
//        Intent startActivityIntent = new Intent(context, DetailsMessagesInbox.class);
//        startActivityIntent.putExtra("broj", smsMessageStr);
//        startActivityIntent.putExtra("name", smsData.getPhoneNumber());
//        startActivityIntent.putExtra("poruka", smsData.getMessageBody());
//        startActivityIntent.putExtra("vrijeme", smsData.getDate());
//        startActivityIntent.putExtra("minute", smsData.getMinute());
//        return PendingIntent.getActivity(context, SMS_PENDING_INTENT, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//    }


//    @Override
//    public void setNotification(Context context) {
//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        createNotificationChannel(notificationManager);
//        buildNotification(context, notificationManager);
//    }


//    private void buildNotification(Context context, NotificationManager notificationManager) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
//        builder.setAutoCancel(true)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.sms_black_24dp)
//                .setContentTitle(smsData.getPhoneNumber())
//                .setDefaults(Notification.DEFAULT_VIBRATE)
//                //    .setContentIntent(contentIntent(context))
//                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
//                .setContentText(smsData.getMessageBody());
//
//        builder.setPriority(Notification.PRIORITY_HIGH);
//
//        // SMS_REMINDER_NOTIFICATION_ID allows us to update or cancel the notification later on
//        if (notificationManager != null) {
//            notificationManager.notify(SMS_NOTIFICATION_ID, builder.build());
//        }
//    }

//    private void createNotificationChannel(NotificationManager notificationManager) {
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(
//                    NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
//            notificationChannel.setDescription("Channel description");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableVibration(true);
//            if (notificationManager != null) {
//                notificationManager.createNotificationChannel(notificationChannel);
//            }
//        }
//    }


}
