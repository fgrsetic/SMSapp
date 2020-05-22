package com.franjo.smsapp.data.model.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "mmsMessage_table")
public class MMSMessage {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int threadId;
    private String mmsType;
    private String mmsPart;
    private String messageBody;
    private String date;


    // If order to add new message in DB we need to create MMSMessage object
    // We don't know the "id" that it will have since DB will be autogenerate "id" on first creation
    @Ignore
    public MMSMessage(int threadId, String mmsType, String mmsPart, String messageBody, String date) {
        this.threadId = threadId;
        this.mmsType = mmsType;
        this.mmsPart = mmsPart;
        this.messageBody = messageBody;
        this.date = date;
    }

    // When reading from the DB since there will already be "id" we will use this constructor on object creation
    public MMSMessage(int id, int threadId, String mmsType, String mmsPart, String messageBody, String date) {
        this.id = id;
        this.threadId = threadId;
        this.mmsType = mmsType;
        this.mmsPart = mmsPart;
        this.messageBody = messageBody;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public String getMmsType() {
        return mmsType;
    }

    public void setMmsType(String mmsType) {
        this.mmsType = mmsType;
    }

    public String getMmsPart() {
        return mmsPart;
    }

    public void setMmsPart(String mmsPart) {
        this.mmsPart = mmsPart;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
