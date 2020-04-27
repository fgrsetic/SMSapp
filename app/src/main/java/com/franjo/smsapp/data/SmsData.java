package com.franjo.smsapp.data;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

import java.util.Objects;

@Keep
public class SmsData implements Parcelable {

    private String phoneNumber;
    private String name;
    private String messageBody;
    private String date;
    private String minute;
    private Bitmap contactImage;

    public SmsData() {

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

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public Bitmap getContactImage() {
        return contactImage;
    }

    public void setContactImage(Bitmap contactImage) {
        this.contactImage = contactImage;
    }


    protected SmsData(Parcel in) {
        phoneNumber = in.readString();
        name = in.readString();
        messageBody = in.readString();
        date = in.readString();
        minute = in.readString();
        contactImage = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<SmsData> CREATOR = new Creator<SmsData>() {
        @Override
        public SmsData createFromParcel(Parcel in) {
            return new SmsData(in);
        }

        @Override
        public SmsData[] newArray(int size) {
            return new SmsData[size];
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
        dest.writeString(minute);
        dest.writeParcelable(contactImage, flags);
    }

    @Override
    public String toString() {
        return "SmsData{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", name='" + name + '\'' +
                ", messageBody='" + messageBody + '\'' +
                ", date='" + date + '\'' +
                ", minute='" + minute + '\'' +
                ", contactImage=" + contactImage +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmsData smsData = (SmsData) o;
        return  Objects.equals(phoneNumber, smsData.phoneNumber) &&
                Objects.equals(name, smsData.name) &&
                Objects.equals(messageBody, smsData.messageBody) &&
                Objects.equals(date, smsData.date) &&
                Objects.equals(minute, smsData.minute) &&
                Objects.equals(contactImage, smsData.contactImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber, name, messageBody, date, minute, contactImage);
    }


}
