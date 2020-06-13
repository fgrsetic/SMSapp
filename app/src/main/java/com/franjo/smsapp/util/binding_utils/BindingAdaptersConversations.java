package com.franjo.smsapp.util.binding_utils;

import android.database.Cursor;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.franjo.smsapp.app.App;
import com.franjo.smsapp.domain.Conversation;
import com.franjo.smsapp.ui.messages.conversations.ConversationsAdapter;
import com.franjo.smsapp.util.DateFormatting;
import com.franjo.smsapp.util.ImageUtil;

import java.util.List;

import static com.franjo.smsapp.util.DateFormatting.DATE_FORMAT_MESSAGES;
import static com.franjo.smsapp.util.DateFormatting.DATE_FORMAT_MESSAGES_CURRENT_YEAR;

public class BindingAdaptersConversations {

    @BindingAdapter("conversationList")
    public static void bindConversationList(RecyclerView recyclerView, List<Conversation> conversationList) {
        ConversationsAdapter adapter = (ConversationsAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.submitList(conversationList);
        }
    }

    @BindingAdapter("dateSnippetCreated")
    public static void bindDateSnippetCreated(TextView textView, Conversation conversation) {
        String dateFormatted = DateFormatting.formatConversationsDate(conversation.getDateMsgCreated(), DATE_FORMAT_MESSAGES_CURRENT_YEAR, DATE_FORMAT_MESSAGES);
        textView.setText(dateFormatted);
    }

    @BindingAdapter("randomChangeContactImage")
    public static void randomChangeContactImage(ImageView imageView, Conversation conversation) {
        imageView.setImageDrawable(ImageUtil.getRandomImage(conversation.getRecipient()));
    }

//    // Search
//    @BindingAdapter("searchMessagesList")
//    public static void bindSearchMessagesListView(ListView listView, Cursor cursor) {
//        SearchMessagesAdapter searchMessagesAdapter = new SearchMessagesAdapter(App.getAppContext(), cursor, 0, new SearchMessagesAdapter.IClickListener() {
//            @Override
//            public void onClick(Conversation message) {
//                Toast.makeText(App.getAppContext(), message.getRecipient(), Toast.LENGTH_SHORT).show();
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
