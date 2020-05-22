//package com.franjo.smsapp.domain;
//
//import androidx.annotation.NonNull;
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import androidx.annotation.Keep;
//
//import java.util.Objects;
//
//@Keep
//public class Message implements Parcelable {
//
//    private int type;
//    private String phoneNumber;
//    private String name;
//    private String messageBody;
//    private String date;
//
//    public Message() {
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getMessageBody() {
//        return messageBody;
//    }
//
//    public void setMessageBody(String messageBody) {
//        this.messageBody = messageBody;
//    }
//
//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Message message = (Message) o;
//        return type == message.type &&
//                Objects.equals(phoneNumber, message.phoneNumber) &&
//                Objects.equals(name, message.name) &&
//                Objects.equals(messageBody, message.messageBody) &&
//                Objects.equals(date, message.date);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(type, phoneNumber, name, messageBody, date);
//    }
//
//    @NonNull
//    @Override
//    public String toString() {
//        return "Message{" +
//                "type=" + type +
//                ", phoneNumber='" + phoneNumber + '\'' +
//                ", name='" + name + '\'' +
//                ", messageBody='" + messageBody + '\'' +
//                ", date='" + date + '\'' +
//                '}';
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(type);
//        dest.writeString(phoneNumber);
//        dest.writeString(name);
//        dest.writeString(messageBody);
//        dest.writeString(date);
//    }
//
//    protected Message(Parcel in) {
//        type = in.readInt();
//        phoneNumber = in.readString();
//        name = in.readString();
//        messageBody = in.readString();
//        date = in.readString();
//    }
//
//    public static final Creator<Message> CREATOR = new Creator<Message>() {
//        @Override
//        public Message createFromParcel(Parcel in) {
//            return new Message(in);
//        }
//
//        @Override
//        public Message[] newArray(int size) {
//            return new Message[size];
//        }
//    };
//}
