//package com.franjo.smsapp.data.model.entity;
//
//import androidx.annotation.NonNull;
//import androidx.room.Entity;
//import androidx.room.Ignore;
//import androidx.room.PrimaryKey;
//
//import com.franjo.smsapp.domain.Message;
//
//import java.util.ArrayList;
//import java.util.List;
//
//// Model class for the DB table
//@Entity(tableName = "smsMessage_table")
//public class SMSMessage {
//

import androidx.room.Ignore;
import androidx.room.PrimaryKey;

//@PrimaryKey(autoGenerate = true)
//    private int id;
//    private int threadId;
//    private String address;
//    private String messageBody;
//    private int messageType; // inbox, sent, draft, outbox ..
//    private String date;
//
//
//    // If order to add new message in DB we need to create SMSMessage object
//    // We don't know the "id" that it will have since DB will be autogenerate "id" on first creation
//    @Ignore
//    public SMSMessage(int threadId, String address, String messageBody, int messageType, String date) {
//        this.threadId = threadId;
//        this.address = address;
//        this.messageBody = messageBody;
//        this.messageType = messageType;
//        this.date = date;
//    }
//
//    // When reading from the DB since there will already be "id" we will use this constructor on object creation
//    public SMSMessage(int id, int threadId, String address, String messageBody, int messageType, String date) {
//        this.id = id;
//        this.threadId = threadId;
//        this.address = address;
//        this.messageBody = messageBody;
//        this.messageType = messageType;
//        this.date = date;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public int getThreadId() {
//        return threadId;
//    }
//
//    public String getAddress() {
//        return address;
//    }
//
//    public String getMessageBody() {
//        return messageBody;
//    }
//
//    public int getMessageType() {
//        return messageType;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//
//    // Helper function that converts Database object to domain object
//    public static List<Message> convertToListOfMessages(List<SMSMessage> SMSMessageList) {
//        List<Message> messageList = new ArrayList<>();
//        for (SMSMessage smsMessage : SMSMessageList) {
//            Message message = new Message();
//            message.setPhoneNumber(smsMessage.getAddress());
//            message.setMessageBody(smsMessage.getMessageBody());
//            message.setDate(smsMessage.getDate());
//            message.setType(smsMessage.getMessageType());
//            messageList.add(message);
//        }
//        return messageList;
//    }
//
//    @NonNull
//    @Override
//    public String toString() {
//        return "MessageEntity{" +
//                "id=" + id +
//                ", threadId=" + threadId +
//                ", address='" + address + '\'' +
//                ", messageBody='" + messageBody + '\'' +
//                ", messageType=" + messageType +
//                ", date='" + date + '\'' +
//                '}';
//    }
//}
