package com.franjo.smsapp.util.binding_utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.domain.Message;
import com.franjo.smsapp.ui.messages.conversations_details.DetailsAdapter;
import com.franjo.smsapp.util.DateFormatting;

import java.util.List;

import static com.franjo.smsapp.data.device_storage.conversations.MessagesDeviceStorageSource.getMmsImage;
import static com.franjo.smsapp.util.DateFormatting.DATE_FORMAT_MESSAGES;
import static com.franjo.smsapp.util.DateFormatting.TIME_FORMAT_MESSAGES_DETAILS_;


public class BindingAdaptersDetails {

    @BindingAdapter("detailsList")
    public static void bindMessageListRecyclerView(RecyclerView recyclerView, List<Message> messageList) {
        DetailsAdapter adapter = (DetailsAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            if (messageList != null) {
                adapter.submitMessagesList(messageList);
            }
        }
    }

    @BindingAdapter("detailsBody")
    public static void bindMessageBody(TextView textView, Message message) {
        if ("application/vnd.wap.multipart.related".equals(message.getContentType())) {
            // MMS
            if (!message.getBodyMessageAttachment().isEmpty()) {
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

    @BindingAdapter("detailsHeaderDate")
    public static void bindMessageReceivedDateDetails(TextView textView, Message message) {
        if (message.getMessageType() == 1) {
            String dateFormatted = DateFormatting.formatDetailsDate(message.getDateMsgReceived(), DATE_FORMAT_MESSAGES);
            textView.setText(dateFormatted);
        }
        if (message.getMessageType() == 2) {
            String dateFormatted = DateFormatting.formatDetailsDate(message.getDateMsgSent(), DATE_FORMAT_MESSAGES);
            textView.setText(dateFormatted);
        }
    }

    @BindingAdapter("detailsTime")
    public static void bindMessageTime(TextView textView, Message message) {
        if (message.getMessageType() == 1) {
            String dateFormatted = DateFormatting.formatDetailsDate(message.getDateMsgReceived(), TIME_FORMAT_MESSAGES_DETAILS_);
            textView.setText(dateFormatted);
        }
        if (message.getMessageType() == 2) {
            String dateFormatted = DateFormatting.formatDetailsDate(message.getDateMsgSent(), TIME_FORMAT_MESSAGES_DETAILS_);
            textView.setText(dateFormatted);
        }
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
