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
import com.franjo.smsapp.ui.messages.search.SearchMessagesAdapter;
import com.franjo.smsapp.util.DateFormatting;
import com.franjo.smsapp.util.ImageUtil;

import java.util.List;

public class BindingAdaptersConversations {

    @BindingAdapter("conversationsList")
    public static void bindConversationsListRecyclerView(RecyclerView recyclerView, List<Conversation> conversationList) {
        ConversationsAdapter adapter = (ConversationsAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.submitList(conversationList);
        }
    }

    @BindingAdapter("conversationPhoneNumber")
    public static void bindPhoneNumber(TextView textView, Conversation conversation) {
        textView.setText(conversation.getRecipient());
    }

    @BindingAdapter("snippet")
    public static void bindSnippet(TextView textView, Conversation conversation) {
        if (conversation.getSnippet() != null) {
            textView.setText(conversation.getSnippet());
        }
    }

    @BindingAdapter("dateCreated")
    public static void bindConversationsDate(TextView textView, Conversation conversation) {
        if (conversation.getDateMsgCreated() != null) {
            String dateFormatted = DateFormatting.formatDate(conversation.getDateMsgCreated());
            textView.setText(dateFormatted);
        }
    }

    @BindingAdapter("conversationContactImage")
    public static void randomChangeContactImage(ImageView imageView, Conversation conversation) {
        imageView.setImageDrawable(ImageUtil.getRandomImage(conversation.getRecipient()));
    }

    // Search
    @BindingAdapter("searchMessagesList")
    public static void bindSearchMessagesListView(ListView listView, Cursor cursor) {
        SearchMessagesAdapter searchMessagesAdapter = new SearchMessagesAdapter(App.getAppContext(), cursor, 0, new SearchMessagesAdapter.IClickListener() {
            @Override
            public void onClick(Conversation message) {
                Toast.makeText(App.getAppContext(), message.getRecipient(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onContactIconClicked(Conversation message) {
                Toast.makeText(App.getAppContext(), "icon", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(searchMessagesAdapter);
    }

}
