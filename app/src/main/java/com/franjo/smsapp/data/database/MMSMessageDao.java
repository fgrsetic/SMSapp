package com.franjo.smsapp.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.franjo.smsapp.data.model.entity.MMSMessage;

import java.util.List;

// Interface for accessing DB with methods we need
@Dao
public interface MMSMessageDao {

    @Query("SELECT * FROM mmsMessage_table ")
    LiveData<List<MMSMessage>> getAllMMSMessages();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMMSMessage(MMSMessage mmsMessage);

    @Delete
    void deleteMMSMessage(MMSMessage mmsMessage);

    @Query("SELECT * FROM mmsMessage_table WHERE id = :id")
    MMSMessage loadMMSMessageById(int id);
}
