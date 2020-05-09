package com.franjo.smsapp.util;

import android.database.Cursor;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.data.model.Message;
import com.franjo.smsapp.ui.messages.MessagesAdapter;
import com.franjo.smsapp.ui.messages.search.SearchMessagesAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class BindingAdaptersMessages {

    @BindingAdapter("messagesList")
    public static void bindMessageListRecyclerView(RecyclerView recyclerView, List<Message> data) {
        MessagesAdapter adapter = (MessagesAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.submitList(data);
        }
    }

    @BindingAdapter("messagePhone")
    public static void bindMessagePhoneNumber(TextView textView, Message message) {
        textView.setText(message.getPhoneNumber());
    }

    @BindingAdapter("messageBody")
    public static void bindMessageBody(TextView textView, Message message) {
        textView.setText(message.getMessageBody());
    }

    @BindingAdapter("messageDate")
    public static void bindMessageDate(TextView textView, Message message) {
        textView.setText(message.getDate());
    }

    @BindingAdapter("messageImage")
    public static void randomChangeContactImage(ImageView imageView, Message message) {
        imageView.setBackground(ImageUtil.getRandomImage());
    }

    // Search
    @BindingAdapter("searchMessagesList")
    public static void bindSearchMessagesListView(ListView listView, Cursor cursor) {
        SearchMessagesAdapter searchMessagesAdapter = new SearchMessagesAdapter(App.getAppContext(), cursor, 0, new SearchMessagesAdapter.IClickListener() {
            @Override
            public void onClick(Message message) {
                Toast.makeText(App.getAppContext(), message.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onContactIconClicked(Message message) {
                Toast.makeText(App.getAppContext(), "icon", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(searchMessagesAdapter);
    }

}
