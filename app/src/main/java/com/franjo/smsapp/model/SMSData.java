package com.franjo.smsapp.model;

/**
 * Created by Franjo on 20.12.2016..
 */
public class SMSData {

    private String number; // Number from witch the sms was send
    private String name;
    private String body; // SMS text body
    private String date; //SMS date
    private String minute;
    private int contactImage;
    private boolean isMine; //Boolean to determine, who is sender of this message


    //Setter, Getter

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

}
