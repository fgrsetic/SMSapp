package com.franjo.smsapp.data;


import androidx.annotation.NonNull;

import java.util.Objects;

public class SmsData {

    private String phoneNumber;
    private String name;
    private String messageBody;
    private String date;
    private String minute;
    private int contactImage;
    private boolean isMine;

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

    public int getContactImage() {
        return contactImage;
    }

    public void setContactImage(int contactImage) {
        this.contactImage = contactImage;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    @NonNull
    @Override
    public String toString() {
        return "SMSData{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", name='" + name + '\'' +
                ", messageBody='" + messageBody + '\'' +
                ", date='" + date + '\'' +
                ", minute='" + minute + '\'' +
                ", contactImage=" + contactImage +
                ", isMine=" + isMine +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmsData smsData = (SmsData) o;
        return contactImage == smsData.contactImage &&
                isMine == smsData.isMine &&
                Objects.equals(phoneNumber, smsData.phoneNumber) &&
                Objects.equals(name, smsData.name) &&
                Objects.equals(messageBody, smsData.messageBody) &&
                Objects.equals(date, smsData.date) &&
                Objects.equals(minute, smsData.minute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumber, name, messageBody, date, minute, contactImage, isMine);
    }
}
