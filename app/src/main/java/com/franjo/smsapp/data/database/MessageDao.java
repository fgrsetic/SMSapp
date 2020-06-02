package com.franjo.smsapp.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.franjo.smsapp.data.model.entity.DatabaseMessage;

import java.util.List;

@Dao
public interface MessageDao {

    @Query("SELECT * FROM MessageTable ")
    LiveData<List<DatabaseMessage>> getAllMessages();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(DatabaseMessage message);

    @Query("SELECT * FROM MessageTable WHERE threadId = :threadId")
    LiveData<List<DatabaseMessage>> loadMessagesById(int threadId);

    @Delete
    void deleteMessage(DatabaseMessage message);

}
