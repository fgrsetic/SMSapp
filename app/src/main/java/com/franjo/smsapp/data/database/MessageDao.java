package com.franjo.smsapp.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.franjo.smsapp.data.model.entity.DatabaseMessage;

import java.util.List;

// Interface for accessing DB with methods we need
@Dao
public interface MessageDao {

    @Query("SELECT * FROM DatabaseMessage ORDER by date DESC")
    LiveData<List<DatabaseMessage>> getAllMessages();

    @Query("SELECT * FROM DatabaseMessage GROUP BY threadId ORDER BY MAX(date) DESC")
    LiveData<List<DatabaseMessage>> getAllMessagesGroupedByName();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(DatabaseMessage message);

    @Delete
    void deleteMessage(DatabaseMessage message);

    @Query("SELECT * FROM DatabaseMessage WHERE id = :id")
    DatabaseMessage loadMessageById(int id);
}
