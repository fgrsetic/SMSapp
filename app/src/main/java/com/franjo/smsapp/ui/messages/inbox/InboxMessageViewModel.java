//package com.franjo.smsapp.ui.messages.inbox;
//
//import android.net.Uri;
//import android.provider.Telephony;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.Transformations;
//import androidx.lifecycle.ViewModel;
//
//import com.franjo.smsapp.app.AppExecutors;
//import com.franjo.smsapp.domain.Message;
//import com.franjo.smsapp.data.repository.MessageRepository;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//
//public class InboxMessageViewModel extends ViewModel {
//
//    private MessageRepository messageRepository;
//
//    private MutableLiveData<List<Message>> notifiedSmsList;
//    private MutableLiveData<Message> navigateToMessageDetails;
//    private MutableLiveData<Boolean> navigateToNewMessage;
//    private MutableLiveData<Uri> navigateToContactDetails;
//
//    public InboxMessageViewModel() {
//        messageRepository = MessageRepository.getInstance();
//        refreshMessages();
//    }
//
//    private void refreshMessages() {
//        AppExecutors.getInstance().diskIO().execute(() -> {
//            messageRepository.loadSMSMessages();
//            messageRepository.loadMMMSMessages(Telephony.Mms.MESSAGE_BOX_INBOX);
//            messageRepository.loadMMMSMessages(Telephony.Mms.MESSAGE_BOX_SENT);
//        });
//    }
//
//    // 1) Show all messages (sms and mms converted to domain object)
//    public LiveData<List<Message>> showMessages() {
//        return Transformations.map(messageRepository.getAllMessages(), databaseMessageList -> {
//            return databaseMessageList.asD
//        });
//
//
//    }
//
//    private List<Message> removedDuplicatesList(List<Message> listWithDuplicates) {
//        Set<String> attributes = new HashSet<>();
//        List<Message> duplicates = new ArrayList<>();
//
//        for (Message message : listWithDuplicates) {
//            if (attributes.contains(message.getPhoneNumber())) {
//                duplicates.add(message);
//            }
//            attributes.add(message.getPhoneNumber());
//        }
//        listWithDuplicates.removeAll(duplicates);
//        return new ArrayList<>(listWithDuplicates);
//    }
//
//
//    // 2) Notification
//    public LiveData<List<Message>> showSmsListReceived() {
//        if (notifiedSmsList == null) {
//            notifiedSmsList = new MutableLiveData<>();
//            //   loadNotifiedSmsList();
//        }
//        return notifiedSmsList;
//    }
//
////    private void loadNotifiedSmsList() {
////        notifiedSmsList.postValue(messageRepository.getSMSList());
////    }
//
//
//    // 3) Message details
//    LiveData<Message> navigateToMessageDetails() {
//        if (navigateToMessageDetails == null) {
//            navigateToMessageDetails = new MutableLiveData<>();
//        }
//        return navigateToMessageDetails;
//    }
//
//    void toMessageDetailsNavigated(Message message) {
//        navigateToMessageDetails.setValue(message);
//    }
//
//    void onMessageDetailsNavigated() {
//        navigateToMessageDetails.setValue(null);
//    }
//
//
//    // 4) New message
//    LiveData<Boolean> navigateToNewMessage() {
//        if (navigateToNewMessage == null) {
//            navigateToNewMessage = new MutableLiveData<>();
//        }
//        return navigateToNewMessage;
//    }
//
//    public void toNewMessageNavigated() {
//        navigateToNewMessage.setValue(true);
//    }
//
//    void doneNavigationToNewMessage() {
//        navigateToNewMessage.setValue(false);
//    }
//
//
//    // 5) Contact details
//    LiveData<Uri> navigateToContactDetails() {
//        if (navigateToContactDetails == null) {
//            navigateToContactDetails = new MutableLiveData<>();
//        }
//        return navigateToContactDetails;
//    }
//
////    void loadContactDetails(Message message) {
////        navigateToContactDetails.postValue(databaseContacts.openContactDetails(message));
////    }
//
//    void doneNavigationToContactDetails() {
//        navigateToContactDetails.postValue(null);
//    }
//
//
////    public LiveData<Bitmap> loadContactPhoto() {
////        if(loadContactPhoto == null) {
////            loadContactPhoto = new MutableLiveData<>();
////            openPhoto();
////        }
////        return loadContactPhoto;
////    }
////
////    private void openPhoto() {
////        loadContactPhoto.setValue(databaseMessages.loadContactPhoto(contactPhoneNumber));
////    }
//
////    public void setContactPhoneNumber(SmsData smsData) {
////        contactPhoneNumber = smsData.getPhoneNumber();
////    }
//
//
//    //    private PendingIntent contentIntent(Context context) {
////        Intent startActivityIntent = new Intent(context, DetailsMessagesInbox.class);
////        startActivityIntent.putExtra("broj", smsMessageStr);
////        startActivityIntent.putExtra("name", smsData.getPhoneNumber());
////        startActivityIntent.putExtra("poruka", smsData.getMessageBody());
////        startActivityIntent.putExtra("vrijeme", smsData.getDate());
////        startActivityIntent.putExtra("minute", smsData.getMinute());
////        return PendingIntent.getActivity(context, SMS_PENDING_INTENT, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
////    }
//
//
////    @Override
////    public void setNotification(Context context) {
////        NotificationManager notificationManager =
////                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
////        createNotificationChannel(notificationManager);
////        buildNotification(context, notificationManager);
////    }
//
//
////    private void buildNotification(Context context, NotificationManager notificationManager) {
////        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
////        builder.setAutoCancel(true)
////                .setWhen(System.currentTimeMillis())
////                .setSmallIcon(R.drawable.text_sms_black_24dp)
////                .setContentTitle(smsData.getPhoneNumber())
////                .setDefaults(Notification.DEFAULT_VIBRATE)
////                //    .setContentIntent(contentIntent(context))
////                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
////                .setContentText(smsData.getMessageBody());
////
////        builder.setPriority(Notification.PRIORITY_HIGH);
////
////        // SMS_REMINDER_NOTIFICATION_ID allows us to update or cancel the notification later on
////        if (notificationManager != null) {
////            notificationManager.notify(SMS_NOTIFICATION_ID, builder.build());
////        }
////    }
//
////    private void createNotificationChannel(NotificationManager notificationManager) {
////        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
////            NotificationChannel notificationChannel = new NotificationChannel(
////                    NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);
////            notificationChannel.setDescription("Channel description");
////            notificationChannel.enableLights(true);
////            notificationChannel.setLightColor(Color.RED);
////            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
////            notificationChannel.enableVibration(true);
////            if (notificationManager != null) {
////                notificationManager.createNotificationChannel(notificationChannel);
////            }
////        }
////    }
//
//
//}
