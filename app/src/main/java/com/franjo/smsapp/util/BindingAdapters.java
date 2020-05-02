package com.franjo.smsapp.util;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.R;
import com.franjo.smsapp.app.App;
import com.franjo.smsapp.data.Contact;
import com.franjo.smsapp.data.SmsData;
import com.franjo.smsapp.ui.contacts.ContactsAdapter;
import com.franjo.smsapp.ui.messages.MessagesAdapter;
import com.franjo.smsapp.ui.search.SearchAdapter;

import java.util.List;
import java.util.Random;

public class BindingAdapters {

    // Sms
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

    @BindingAdapter("smsList")
    public static void bindSmsListRecyclerView(RecyclerView recyclerView, List<SmsData> data) {
        MessagesAdapter adapter = (MessagesAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.submitList(data);
        }
    }

    @BindingAdapter("backgroundImage")
    public static void randomChangeContactImage(ImageView imageView, SmsData smsData) {
        imageView.setBackground(ImageUtil.getRandomImage());
    }

    // Search
    @BindingAdapter("searchedList")
    public static void bindSearchListRecyclerView(RecyclerView recyclerView, List<SmsData> data) {
        SearchAdapter adapter = (SearchAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.submitList(data);
        }
    }


    // Contacts
    @BindingAdapter("contactsList")
    public static void bindContactsListRecyclerView(RecyclerView recyclerView, List<Contact> data) {
        ContactsAdapter adapter = (ContactsAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.submitList(data);
        }
    }

    @BindingAdapter("contactsName")
    public static void bindContactsName(TextView textView, Contact contact) {
        textView.setText(contact.getName());
    }

    @BindingAdapter("backgroundImage")
    public static void randomChangeContactImage(ImageView imageView, Contact Contact) {
        imageView.setBackground(ImageUtil.getRandomImage());
    }

}
