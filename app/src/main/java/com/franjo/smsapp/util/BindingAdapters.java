package com.franjo.smsapp.util;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.data.SmsData;
import com.franjo.smsapp.ui.messages.MessagesAdapter;

import java.util.List;
import java.util.Random;

public class BindingAdapters {

    @BindingAdapter("smsPhoneText")
    public static void bindSmsPhoneNumber(TextView textView, SmsData smsData) {
        textView.setText(smsData.getPhoneNumber());
    }

    @BindingAdapter("smsMessageText")
    public static void bindSmsMessage(TextView textView, SmsData smsData) {
        textView.setText(smsData.getMessageBody());
    }

    @BindingAdapter("smsDateText")
    public static void bindSmsDate(TextView textView, SmsData smsData) {
        textView.setText(smsData.getDate());
    }

    @BindingAdapter("smsMinuteText")
    public static void bindSmsMinutes(TextView textView, SmsData smsData) {
        textView.setText(smsData.getMinute());
    }

    @BindingAdapter("listData")
    public static void bindRecyclerView(RecyclerView recyclerView, List<SmsData> data) {
        MessagesAdapter adapter = (MessagesAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.submitList(data);
        }
    }

    @BindingAdapter("backgroundImage")
    public static void randomChangeContactImage(ImageView imageView, SmsData smsData) {
        imageView.setBackground(ImageUtil.getRandomImage());
    }

}
