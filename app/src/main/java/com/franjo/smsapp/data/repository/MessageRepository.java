package com.franjo.smsapp.data.repository;

import androidx.lifecycle.LiveData;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.data.database.AppDatabase;
import com.franjo.smsapp.data.database.MMSMessageDao;
import com.franjo.smsapp.data.database.MessageDao;
import com.franjo.smsapp.data.device_storage.mms.IMMSDeviceStorageSource;
import com.franjo.smsapp.data.device_storage.mms.MMSDeviceStorageSource;
import com.franjo.smsapp.data.device_storage.sms.ISMSDeviceStorageSource;
import com.franjo.smsapp.data.device_storage.sms.SMSDeviceStorageSource;
import com.franjo.smsapp.data.model.entity.DatabaseMessage;
import com.franjo.smsapp.data.model.entity.MMSMessage;

import java.util.List;

public class MessageRepository {

    private static MessageRepository INSTANCE = null;

    private ISMSDeviceStorageSource smsDeviceStorageSource;
    private IMMSDeviceStorageSource mmsDeviceStorageSource;

    private MessageDao messageDao;
    private MMSMessageDao mmsMessageDao;

    private LiveData<List<DatabaseMessage>> allSMSMessages;
    private LiveData<List<MMSMessage>> allMMSMessages;

    private MessageRepository() {
        AppDatabase appDatabase = AppDatabase.getInstance(App.getAppContext());
        smsDeviceStorageSource = SMSDeviceStorageSource.getInstance();
        mmsDeviceStorageSource = MMSDeviceStorageSource.getInstance();
        messageDao = appDatabase.messageDao();
        mmsMessageDao = appDatabase.mmsMessageDao();
    }

    public static MessageRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MessageRepository();
        }
        return INSTANCE;
    }


    // Device storage data source SMS/MMS
    public void loadMessages() {
        smsDeviceStorageSource.getSMSMessages();
    }

    public void loadMMMSMessages(int messageType) {
        mmsDeviceStorageSource.getMMSMessages(messageType);
    }


    // Local database dta source
    public LiveData<List<DatabaseMessage>> getAllMessagesGroupedByName() {
        return messageDao.getAllMessagesGroupedByName();
    }

//    public void insertMessage(SMSMessage SMSMessage) {
//        SMSMessageDao.insertMessage(SMSMessage);
//    }
//
//    public void deleteMessage(SMSMessage SMSMessage) {
//        SMSMessageDao.deleteMessage(SMSMessage);
//    }
//
//    public SMSMessage loadMessageById(int id) {
//        return SMSMessageDao.loadMessageById(id);
//    }

}
