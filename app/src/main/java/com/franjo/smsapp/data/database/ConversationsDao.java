package com.franjo.smsapp.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.franjo.smsapp.data.model.entity.DatabaseConversation;

import java.util.List;

@Dao
public interface ConversationsDao {

    @Query("SELECT id, threadId, recipient, snippet, dateMsgCreated FROM ConversationTable ORDER BY dateMsgCreated DESC")
    LiveData<List<DatabaseConversation>> getConversations();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertConversation(DatabaseConversation conversation);

    @Delete
    void deleteConversation(DatabaseConversation conversation);

}
