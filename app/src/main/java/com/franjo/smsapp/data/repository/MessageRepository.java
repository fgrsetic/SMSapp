package com.franjo.smsapp.data.repository;

import androidx.lifecycle.LiveData;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.data.database.AppDatabase;
import com.franjo.smsapp.data.database.ConversationsDao;
import com.franjo.smsapp.data.database.MessageDao;
import com.franjo.smsapp.data.device_storage.conversations.IMessagesDeviceStorageSource;
import com.franjo.smsapp.data.device_storage.conversations.MessagesDeviceStorageSource;
import com.franjo.smsapp.data.model.entity.DatabaseConversation;
import com.franjo.smsapp.data.model.entity.DatabaseMessage;

import java.util.List;

public class MessageRepository {

    private static MessageRepository INSTANCE = null;

    private IMessagesDeviceStorageSource deviceStorageSource;

    private ConversationsDao conversationsDao;
    private MessageDao messageDao;

    private MessageRepository() {
        AppDatabase appDatabase = AppDatabase.getInstance(App.getAppContext());
        deviceStorageSource = MessagesDeviceStorageSource.getInstance();
        conversationsDao = appDatabase.conversationsDao();
        messageDao = appDatabase.messageDao();
    }

    public static MessageRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MessageRepository();
        }
        return INSTANCE;
    }


    // Device storage data source Conversations, SMS/MMS messages
    public void loadAndSaveStorageMessages() {
        deviceStorageSource.loadAndSaveStorageMessages();
    }


    // Local database source
    public LiveData<List<DatabaseConversation>> getConversations() {
        return conversationsDao.getConversations();
    }

    public LiveData<List<DatabaseMessage>> loadMessagesById(int id) {
        return messageDao.loadMessagesById(id);
    }

}
