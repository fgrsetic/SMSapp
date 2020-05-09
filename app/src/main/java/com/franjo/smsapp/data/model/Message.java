package com.franjo.smsapp.data.model;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.util.Objects;

@Keep
public class Message implements Parcelable {

    private String phoneNumber;
    private String name;
    private String messageBody;
    private String date;
    private String time;
    private Bitmap contactImage;
    private boolean isMessageSendByMe;

    public Message() {

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Bitmap getContactImage() {
        return contactImage;
    }

    public void setContactImage(Bitmap contactImage) {
        this.contactImage = contactImage;
    }

    public boolean isMessageSendByMe() {
        return isMessageSendByMe;
    }

    public void setMessageSendByMe(boolean messageSendByMe) {
        isMessageSendByMe = messageSendByMe;
    }

    protected Message(Parcel in) {
        phoneNumber = in.readString();
        name = in.readString();
        messageBody = in.readString();
        date = in.readString();
        time = in.readString();
        contactImage = in.readParcelable(Bitmap.class.getClassLoader());
        isMessageSendByMe = in.readBoolean();
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phoneNumber);
        dest.writeString(name);
        dest.writeString(messageBody);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeParcelable(contactImage, flags);
        dest.writeBoolean(isMessageSendByMe);
    }

    @NonNull
    @Override
    public String toString() {
        return "SmsData{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", name='" + name + '\'' +
                ", messageBody='" + messageBody + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", contactImage=" + contactImage +
                ", isMessageSendByMe=" + isMessageSendByMe +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(phoneNumber, message.phoneNumber) &&
                Objects.equals(name, message.name) &&
                Objects.equals(messageBody, message.messageBody) &&
                Objects.equals(date, message.date) &&
                Objects.equals(time, message.time) &&
                Objects.equals(contactImage, message.contactImage) &&
                Objects.equals(isMessageSendByMe, message.isMessageSendByMe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber, name, messageBody, date, time, contactImage, isMessageSendByMe);
    }

}
