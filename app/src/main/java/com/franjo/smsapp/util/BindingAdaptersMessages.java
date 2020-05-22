package com.franjo.smsapp.util;

import android.database.Cursor;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.domain.Message;
import com.franjo.smsapp.ui.messages.inbox.InboxMessageAdapter;
import com.franjo.smsapp.ui.messages.search.SearchMessagesAdapter;

import java.util.List;

import static com.franjo.smsapp.util.DateFormatting.formatDate;

public class BindingAdaptersMessages {

    @BindingAdapter("messagesList")
    public static void bindMessageListRecyclerView(RecyclerView recyclerView, List<Message> data) {
        InboxMessageAdapter adapter = (InboxMessageAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.submitList(data);
        }
    }

//    private static List<Message> removeDuplicateList(List<Message> listWithDuplicates) {
//        Set<String> attributes = new HashSet<>();
//        List<Message> duplicates = new ArrayList<>();
//        for (Message message : listWithDuplicates) {
//            if (attributes.contains(message.getPhoneNumber())) {
//                duplicates.add(message);
//            }
//            attributes.add(message.getPhoneNumber());
//        }
//        listWithDuplicates.removeAll(duplicates);
//
//        return new ArrayList<>(listWithDuplicates);
//    }

    @BindingAdapter("messagePhone")
    public static void bindMessagePhoneNumber(TextView textView, Message message) {
        String name = ContactsName.getContactName(App.getAppContext(), message.getPhoneNumber());
        if (name != null) {
            message.setName(name);
        } else {
            message.setName(message.getPhoneNumber());
        }
        textView.setText(message.getName());
    }

    @BindingAdapter("messageBody")
    public static void bindMessageBody(TextView textView, Message message) {
        textView.setText(message.getMessageBody());
    }

    @BindingAdapter("messageDate")
    public static void bindMessageDate(TextView textView, Message message) {
        String dateFormatted = formatDate(message.getDate());
        textView.setText(dateFormatted);
    }

    @BindingAdapter("messageImage")
    public static void randomChangeContactImage(ImageView imageView, Message message) {
        String name = ContactsName.getContactName(App.getAppContext(), message.getPhoneNumber());
        if (name != null) {
            message.setName(name);
        } else {
            message.setName(message.getPhoneNumber());
        }
        imageView.setImageDrawable(ImageUtil.getRandomImage(message.getName()));
    }

    // Search
    @BindingAdapter("searchMessagesList")
    public static void bindSearchMessagesListView(ListView listView, Cursor cursor) {
        SearchMessagesAdapter searchMessagesAdapter = new SearchMessagesAdapter(App.getAppContext(), cursor, 0, new SearchMessagesAdapter.IClickListener() {
            @Override
            public void onClick(Message message) {
                Toast.makeText(App.getAppContext(), message.getPhoneNumber(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onContactIconClicked(Message message) {
                Toast.makeText(App.getAppContext(), "icon", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(searchMessagesAdapter);
    }

}
