//package com.franjo.smsapp.util;
//
//import android.widget.TextView;
//
//import androidx.databinding.BindingAdapter;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.franjo.smsapp.persistence.model.Message;
//import com.franjo.smsapp.ui.messages.details.MessageDetailsAdapter;
//
//import java.util.List;
//
//import static com.franjo.smsapp.util.DateFormatting.HEADER_FORMAT_MESSAGES_DETAILS;
//import static com.franjo.smsapp.util.DateFormatting.TIME_FORMAT_MESSAGES_DETAILS_;
//import static com.franjo.smsapp.util.DateFormatting.formattedStringFromMillis;
//
//public class BindingAdaptersMessagesDetails {
//
//    @BindingAdapter("messagesDetailsList")
//    public static void bindMessageListRecyclerView(RecyclerView recyclerView, List<Message> data) {
//        MessageDetailsAdapter adapter = (MessageDetailsAdapter) recyclerView.getAdapter();
//        if (adapter != null) {
//            adapter.submitList(data);
//        }
//    }
//
//    @BindingAdapter("messageDetailsBody")
//    public static void bindMessageBody(TextView textView, Message message) {
//        textView.setText(message.getMessageBody());
//    }
//
//    @BindingAdapter("messageDetailsDate")
//    public static void bindMessageDate(TextView textView, Message message) {
//        textView.setText(message.getDate());
//    }
//
//    @BindingAdapter("messageDetailsHeaderDate")
//    public static void bindMessageDateDetails(TextView textView, Message message) {
//        String formattedDate = formattedStringFromMillis(HEADER_FORMAT_MESSAGES_DETAILS, Long.parseLong(message.getDate()));
//        textView.setText(formattedDate);
//    }
//
//    @BindingAdapter("messageDetailsTime")
//    public static void bindMessageTime(TextView textView, Message message) {
//        String formattedTime = DateFormatting.formattedStringFromMillis(TIME_FORMAT_MESSAGES_DETAILS_, Long.parseLong((message.getDate())));
//        textView.setText(formattedTime);
//    }
//
//
//    // Search
////    @BindingAdapter("searchMessagesList")
////    public static void bindSearchMessagesListView(ListView listView, Cursor cursor) {
////        SearchMessagesAdapter searchMessagesAdapter = new SearchMessagesAdapter(App.getAppContext(), cursor, 0, new SearchMessagesAdapter.IClickListener() {
////            @Override
////            public void onClick(Message message) {
////                Toast.makeText(App.getAppContext(), message.getName(), Toast.LENGTH_SHORT).show();
////            }
////
////            @Override
////            public void onContactIconClicked(Message message) {
////                Toast.makeText(App.getAppContext(), "icon", Toast.LENGTH_SHORT).show();
////            }
////        });
////        listView.setAdapter(searchMessagesAdapter);
////    }
//
//}
