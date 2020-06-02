package com.franjo.smsapp.util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.domain.Message;
import com.franjo.smsapp.ui.messages.conversations_details.DetailsAdapter;

import java.util.List;

import static com.franjo.smsapp.data.device_storage.conversations.MessagesDeviceStorageSource.getMmsImage;


public class BindingAdaptersDetails {

    @BindingAdapter("detailsList")
    public static void bindMessageListRecyclerView(RecyclerView recyclerView, List<Message> messageList) {
        DetailsAdapter adapter = (DetailsAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.submitMessagesList(messageList);
        }
    }

    @BindingAdapter("detailsBody")
    public static void bindMessageBody(TextView textView, Message message) {
        if ("application/vnd.wap.multipart.related".equals(message.getContentType())) {
            // MMS
            if (message.getBodyMessageAttachment() != null) {
                Bitmap bitmap = getMmsImage(String.valueOf(message.getId()));
                textView.setBackground(new BitmapDrawable(App.getAppContext().getResources(), bitmap));
            } else {
                textView.setText(message.getMessageBody());
            }
        } else {
            // SMS
            textView.setText(message.getMessageBody());
        }
    }

    @BindingAdapter("detailsDateReceived")
    public static void bindMessageDateReceived(TextView textView, Message message) {
        if (message.getDateMsgReceived() != null) {
            String dateFormatted = DateFormatting.formatDate(message.getDateMsgReceived());
            textView.setText(dateFormatted);
        }
    }

    @BindingAdapter("detailsDateSent")
    public static void bindMessageDateSent(TextView textView, Message message) {
        if (message.getDateMsgSent() != null) {
            String dateFormatted = DateFormatting.formatDate(message.getDateMsgSent());
            textView.setText(dateFormatted);
        }
    }

    // TODO
    @BindingAdapter("detailsHeaderDate")
    public static void bindMessageDateDetails(TextView textView, Message message) {
        if (message.getDateMsgSent() != null) {
            String dateFormatted = DateFormatting.formatDate(message.getDateMsgSent());
            textView.setText(dateFormatted);
        }
    }

    @BindingAdapter("conversationsDetailsTime")
    public static void bindMessageTime(TextView textView, Message message) {
        //    textView.setText(formattedTime);
    }


    // Search
//    @BindingAdapter("searchMessagesList")
//    public static void bindSearchMessagesListView(ListView listView, Cursor cursor) {
//        SearchMessagesAdapter searchMessagesAdapter = new SearchMessagesAdapter(App.getAppContext(), cursor, 0, new SearchMessagesAdapter.IClickListener() {
//            @Override
//            public void onClick(Conversation message) {
//                Toast.makeText(App.getAppContext(), message.getName(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onContactIconClicked(Conversation message) {
//                Toast.makeText(App.getAppContext(), "icon", Toast.LENGTH_SHORT).show();
//            }
//        });
//        listView.setAdapter(searchMessagesAdapter);
//    }

}
